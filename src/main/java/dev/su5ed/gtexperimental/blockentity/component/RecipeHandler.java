package dev.su5ed.gtexperimental.blockentity.component;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.machine.MachineController;
import dev.su5ed.gtexperimental.api.machine.MachineProgress;
import dev.su5ed.gtexperimental.api.machine.PowerHandler;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperties;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperty;
import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.network.NetworkHandler;
import dev.su5ed.gtexperimental.network.Networked;
import dev.su5ed.gtexperimental.network.SynchronizedData;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public abstract class RecipeHandler<T extends BaseBlockEntity, R extends BaseRecipe<?, ?, IN, OUT, ? super R>, IN, OUT> extends GtComponentBase<T> implements MachineProgress {
    private final PowerHandler energy;
    private final UpgradeManager<?> upgrades;
    private final MachineController controller;
    protected final RecipeProvider<R, IN> manager;
    private final RecipeOutputType<IN> inputSerializer;
    private final RecipeOutputType<OUT> outputSerializer;
    private final boolean needsConstantEnergy;
    private final LazyOptional<MachineProgress> machineProgressOptional = LazyOptional.of(() -> this);

    private CompoundTag pendingRecipeInitTag;
    private AvailableRecipe<R, IN> availableRecipe;
    @Networked
    protected PendingRecipe<IN, OUT> pendingRecipe;
    @Networked
    private double progress;
    private boolean outputBlocked;

    private final SynchronizedData.Key pendingRecipeKey = SynchronizedData.Key.component(this, "pendingRecipe");
    private final SynchronizedData.Key progressKey = SynchronizedData.Key.component(this, "progress");

    static {
        NetworkHandler.registerHandler(RecipeHandler.class, "pendingRecipe", new PendingRecipeNetworkSerializer<>());
    }

    protected RecipeHandler(T parent, RecipeProvider<R, IN> manager, RecipeOutputType<IN> inputSerializer, RecipeOutputType<OUT> outputSerializer, boolean needsConstantEnergy) {
        super(parent);

        this.energy = GtUtil.getRequiredCapability(parent, Capabilities.ENERGY_HANDLER);
        this.upgrades = (UpgradeManager<?>) parent.getComponent(UpgradeManager.NAME).orElseThrow(() -> new IllegalStateException("Required UpgradeManager component not found"));
        this.controller = GtUtil.getRequiredCapability(parent, Capabilities.MACHINE_CONTROLLER);
        this.manager = manager;
        this.inputSerializer = inputSerializer;
        this.outputSerializer = outputSerializer;
        this.needsConstantEnergy = needsConstantEnergy;
    }

    public abstract boolean accepts(ItemStack input);

    public boolean isOutputBlocked() {
        return this.outputBlocked;
    }

    protected abstract IN getInput();

    protected boolean checkProcessRecipe(R recipe) {
        if (this.controller.isAllowedToWork() && this.energy.canUseEnergy(getEnergyCost(recipe.getProperty(ModRecipeProperty.ENERGY_COST)))) {
            if (canAddOutput(recipe)) {
                this.outputBlocked = false;
                return true;
            }
            this.outputBlocked = true;
        }
        return false;
    }

    protected abstract boolean canAddOutput(R recipe);

    protected abstract void consumeInput(R recipe);

    protected abstract void addOutput(PendingRecipe<IN, OUT> recipe);

    public double getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return this.pendingRecipe == null ? 0 : this.pendingRecipe.properties.get(ModRecipeProperty.DURATION);
    }

    public double getEnergyCost(double base) {
        int multiplier = (int) Math.pow(4, this.upgrades.getUpgradeCount(UpgradeCategory.OVERCLOCKER));
        return base * multiplier;
    }

    public void checkRecipe() {
        if (this.pendingRecipe == null && !this.parent.getLevel().isClientSide) {
            IN input = getInput();
            R recipe = this.manager.getRecipeFor(this.parent.getLevel(), input);
            if (recipe != null) {
                this.availableRecipe = new AvailableRecipe<>(recipe, input);
            }
            else {
                this.availableRecipe = null;
            }
        }
    }

    @Override
    public void addSyncedData(Set<? super SynchronizedData.Key> keys) {
        super.addSyncedData(keys);
        keys.add(this.pendingRecipeKey);
        keys.add(this.progressKey);
    }

    @Override
    public boolean isActive() {
        return this.parent.isActive();
    }

    @Override
    public void increaseProgress(double amount) {
        this.progress += amount;
    }

    @Override
    public <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if (cap == Capabilities.MACHINE_PROGRESS) {
            return this.machineProgressOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.pendingRecipeInitTag != null) {
            this.pendingRecipe = PendingRecipe.fromNBT(this.pendingRecipeInitTag, this.inputSerializer, this.outputSerializer);
        }
        else {
            checkRecipe();
        }
    }

    @Override
    public void tickServer() {
        super.tickServer();

        if (this.pendingRecipe != null) {
            double energyCost = getEnergyCost(this.pendingRecipe.properties.get(ModRecipeProperty.ENERGY_COST));
            if (this.controller.isAllowedToWork() && this.energy.tryUseEnergy(energyCost)) {
                int maxProgress = getMaxProgress();
                this.parent.setActive(true);
                increaseProgress(1 << this.upgrades.getUpgradeCount(UpgradeCategory.OVERCLOCKER));
                if (this.progress >= maxProgress) {
                    addOutput(this.pendingRecipe);
                    this.pendingRecipe = null;
                    this.progress = 0;
                    checkRecipe();
                }
                else {
                    this.parent.setChanged();
                }
            }
            else {
                this.parent.setActive(false);
                if (this.needsConstantEnergy) {
                    this.progress = 0;
                }
            }
        }
        else if (this.availableRecipe != null) {
            if (checkProcessRecipe(this.availableRecipe.recipe)) {
                this.pendingRecipe = new PendingRecipe<>(this.availableRecipe.recipe.getId(), this.inputSerializer.copy(this.availableRecipe.input), this.inputSerializer,
                    this.availableRecipe.recipe.getOutput(), this.outputSerializer, this.availableRecipe.recipe.getType().getProperties(), this.availableRecipe.recipe.getProperties());
                consumeInput(this.availableRecipe.recipe);
            }
        }
        else {
            this.parent.setActive(false);
            this.outputBlocked = false;
        }
    }

    @Override
    public void onFieldUpdate(String name) {}

    @Override
    public void getScanInfo(List<Component> scan, Player player, BlockPos pos, int scanLevel) {
        super.getScanInfo(scan, player, pos, scanLevel);

        if (scanLevel > 0) {
            String key = this.parent.isActive() ? "active" : "inactive";
            scan.add(GtLocale.key("info", "machine_" + key).toComponent());
        }
    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);

        if (this.pendingRecipe != null && !this.parent.getLevel().isClientSide) {
            tag.put("pendingRecipe", this.pendingRecipe.serializeNBT());
            tag.putDouble("progress", this.progress);
        }
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);

        if (tag.contains("pendingRecipe", Tag.TAG_COMPOUND)) {
            // Init the pending recipe later, after the level has been loaded
            this.pendingRecipeInitTag = tag.getCompound("pendingRecipe");
            this.progress = tag.getDouble("progress");
        }
    }

    public record AvailableRecipe<R extends BaseRecipe<?, ?, IN, ?, ? super R>, IN>(R recipe, IN input) {}

    public record PendingRecipe<IN, OUT>(ResourceLocation id, IN input, RecipeOutputType<IN> inputSerializer, OUT output, RecipeOutputType<OUT> outputSerializer, List<RecipeProperty<?>> propertyKeys, RecipeProperties properties) {
        public CompoundTag serializeNBT() {
            FriendlyCompoundTag tag = new FriendlyCompoundTag();
            tag.putString("id", this.id.toString());
            tag.put("input", this.inputSerializer.toNBT(this.input));
            tag.put("output", this.outputSerializer.toNBT(this.output));
            tag.put("propertyKeys", this.propertyKeys, ModRecipeProperty.CODEC.listOf());
            tag.put("properties", this.properties.toNBT());
            return tag;
        }

        @Nullable
        public static <IN, OUT> PendingRecipe<IN, OUT> fromNBT(CompoundTag nbt, RecipeOutputType<IN> inputSerializer, RecipeOutputType<OUT> outputSerializer) {
            FriendlyCompoundTag tag = new FriendlyCompoundTag(nbt);
            ResourceLocation id = new ResourceLocation(tag.getString("id"));
            IN input = inputSerializer.fromNBT(tag.get("input"));
            OUT output = outputSerializer.fromNBT(tag.get("output"));
            List<RecipeProperty<?>> propertyKeys = tag.get("propertyKeys", ModRecipeProperty.CODEC.listOf());
            RecipeProperties properties = RecipePropertyMap.fromNBT(id, propertyKeys, tag.getCompound("properties"));
            return new PendingRecipe<>(id, input, inputSerializer, output, outputSerializer, propertyKeys, properties);
        }
    }

    public static class PendingRecipeNetworkSerializer<R extends BaseRecipe<?, ?, IN, OUT, ? super R>, IN, OUT> implements NetworkHandler.SerializationHandler<RecipeHandler<?, R, IN, OUT>, PendingRecipe<IN, OUT>> {
        @Override
        public void toNetwork(RecipeHandler<?, R, IN, OUT> parent, FriendlyByteBuf buf, PendingRecipe<IN, OUT> instance) {
            buf.writeResourceLocation(instance.id);
            parent.inputSerializer.toNetwork(buf, instance.input);
            parent.outputSerializer.toNetwork(buf, instance.output);
            buf.writeCollection(instance.propertyKeys, (b, p) -> b.writeUtf(p.getName()));
            instance.properties.toNetwork(buf);
        }

        @Override
        public PendingRecipe<IN, OUT> fromNetwork(RecipeHandler<?, R, IN, OUT> parent, FriendlyByteBuf buf, Class<?> cls) {
            ResourceLocation id = buf.readResourceLocation();
            IN input = parent.inputSerializer.fromNetwork(buf);
            OUT output = parent.outputSerializer.fromNetwork(buf);
            List<RecipeProperty<?>> propertyKeys = buf.readList(b -> ModRecipeProperty.getByName(b.readUtf()));
            RecipeProperties properties = RecipePropertyMap.fromNetwork(id, propertyKeys, buf);
            return new PendingRecipe<>(id, input, parent.inputSerializer, output, parent.outputSerializer, propertyKeys, properties);
        }
    }
}
