package dev.su5ed.gtexperimental.blockentity.component;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.GregTechMod;
import dev.su5ed.gtexperimental.api.machine.MachineController;
import dev.su5ed.gtexperimental.api.machine.PowerHandler;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.upgrade.UpgradeCategory;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.blockentity.base.SimpleMachineBlockEntity;
import dev.su5ed.gtexperimental.network.NetworkHandler;
import dev.su5ed.gtexperimental.network.Networked;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.SISORecipe;
import dev.su5ed.gtexperimental.util.GtLocale;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public abstract class RecipeHandler<T extends BaseBlockEntity, R extends BaseRecipe<?, IN, OUT, ? super R>, IN, OUT> extends GtComponentBase<T> {
    private final PowerHandler energy;
    private final UpgradeManager<?> upgrades;
    private final MachineController controller;
    protected final RecipeManager<R, IN> manager;
    private final RecipeOutputType<IN> inputSerializer;
    private final RecipeOutputType<OUT> outputSerializer;
    private final boolean needsConstantEnergy;

    private CompoundTag pendingRecipeInitTag;
    private R availableRecipe;
    @Networked
    protected PendingRecipe<R, IN, OUT> pendingRecipe;
    @Networked
    private int progress;
    private boolean outputBlocked;

    static {
        NetworkHandler.registerHandler(RecipeHandler.class, "pendingRecipe", new PendingRecipeNetworkSerializer<>());
    }

    protected RecipeHandler(T parent, RecipeManager<R, IN> manager, RecipeOutputType<IN> inputSerializer, RecipeOutputType<OUT> outputSerializer) {
        this(parent, manager, inputSerializer, outputSerializer, true);
    }

    protected RecipeHandler(T parent, RecipeManager<R, IN> manager, RecipeOutputType<IN> inputSerializer, RecipeOutputType<OUT> outputSerializer, boolean needsConstantEnergy) {
        super(parent);

        this.energy = parent.getCapability(Capabilities.ENERGY_HANDLER).orElseThrow(() -> new IllegalStateException("Required PowerHandler capability not found"));
        this.upgrades = (UpgradeManager<?>) parent.getComponent(UpgradeManager.NAME).orElseThrow(() -> new IllegalStateException("Required UpgradeManager component not found"));
        this.controller = parent.getCapability(Capabilities.MACHINE_CONTROLLER).orElseThrow(() -> new IllegalStateException("Required MachineController capability not found"));
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
        if (this.controller.isAllowedToWork() && this.energy.canUseEnergy(getEnergyCost(recipe))) {
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

    protected abstract void addOutput(R recipe);

    public int getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return this.pendingRecipe == null ? 0 : this.pendingRecipe.recipe.getProperty(ModRecipeProperty.DURATION);
    }

    public double getEnergyCost(R recipe) {
        int multiplier = (int) Math.pow(4, this.upgrades.getUpgradeCount(UpgradeCategory.OVERCLOCKER));
        return recipe.getProperty(ModRecipeProperty.ENERGY_COST) * multiplier;
    }

    public void checkRecipe() {
        if (this.pendingRecipe == null && !this.parent.getLevel().isClientSide) {
            IN input = getInput();
            this.availableRecipe = this.manager.getRecipeFor(this.parent.getLevel(), input);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.pendingRecipeInitTag != null) {
            this.pendingRecipe = PendingRecipe.fromNBT(this.pendingRecipeInitTag, this.inputSerializer, this.outputSerializer, this.parent.getLevel(), this.manager);
        }
        else {
            checkRecipe();
        }
    }

    @Override
    public void tickServer() {
        super.tickServer();

        if (this.pendingRecipe != null) {
            double energyCost = getEnergyCost(this.pendingRecipe.recipe);
            if (this.controller.isAllowedToWork() && this.energy.tryUseEnergy(energyCost)) {
                int maxProgress = getMaxProgress();
                this.parent.setActive(true);
                this.progress += 1 << this.upgrades.getUpgradeCount(UpgradeCategory.OVERCLOCKER);
                if (this.progress >= maxProgress) {
                    addOutput(this.pendingRecipe.recipe);
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
            if (checkProcessRecipe(this.availableRecipe)) {
                IN input = getInput();
                this.pendingRecipe = new PendingRecipe<>(this.inputSerializer.copy(input), this.inputSerializer, this.availableRecipe.getOutput(), this.outputSerializer, this.availableRecipe);
                consumeInput(this.availableRecipe);
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
            tag.putInt("progress", this.progress);
        }
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);

        if (tag.contains("pendingRecipe", Tag.TAG_COMPOUND)) {
            // Init the pending recipe later, after the level has been loaded
            this.pendingRecipeInitTag = tag.getCompound("pendingRecipe");
            this.progress = tag.getInt("progress");
        }
    }

    public record PendingRecipe<R extends BaseRecipe<?, IN, OUT, ? super R>, IN, OUT>(IN input, RecipeOutputType<IN> inputSerializer, OUT output, RecipeOutputType<OUT> outputSerializer, R recipe) {
        public CompoundTag serializeNBT() {
            FriendlyCompoundTag tag = new FriendlyCompoundTag();
            tag.put("input", this.inputSerializer.toNBT(this.input));
            tag.put("output", this.outputSerializer.toNBT(this.output));
            tag.putString("recipe", this.recipe.getId().toString());
            return tag;
        }

        @Nullable
        public static <R extends BaseRecipe<?, IN, OUT, ? super R>, IN, OUT> PendingRecipe<R, IN, OUT> fromNBT(CompoundTag nbt, RecipeOutputType<IN> inputSerializer, RecipeOutputType<OUT> outputSerializer, Level level, RecipeManager<R, IN> manager) {
            FriendlyCompoundTag tag = new FriendlyCompoundTag(nbt);
            String key = tag.getString("recipe");
            if (!key.isEmpty()) {
                ResourceLocation recipeKey = new ResourceLocation(key);
                R recipe = manager.getById(level, recipeKey);
                if (recipe != null) {
                    IN input = inputSerializer.fromNBT(tag.get("input"));
                    OUT output = outputSerializer.fromNBT(tag.get("output"));
                    return new PendingRecipe<>(input, inputSerializer, output, outputSerializer, recipe);
                }
                GregTechMod.LOGGER.warn("Cannot find recipe '{}', abandoning process", recipeKey);
            }
            return null;
        }
    }

    public static class PendingRecipeNetworkSerializer<R extends BaseRecipe<?, IN, OUT, ? super R>, IN, OUT> implements NetworkHandler.SerializationHandler<RecipeHandler<?, R, IN, OUT>, PendingRecipe<R, IN, OUT>> {
        @Override
        public void toNetwork(RecipeHandler<?, R, IN, OUT> parent, FriendlyByteBuf buf, PendingRecipe<R, IN, OUT> instance) {
            parent.inputSerializer.toNetwork(buf, instance.input);
            parent.outputSerializer.toNetwork(buf, instance.output);
            buf.writeResourceLocation(instance.recipe.getId());
        }

        @Override
        public PendingRecipe<R, IN, OUT> fromNetwork(RecipeHandler<?, R, IN, OUT> parent, FriendlyByteBuf buf, Class<?> cls) {
            IN input = parent.inputSerializer.fromNetwork(buf);
            OUT output = parent.outputSerializer.fromNetwork(buf);
            ResourceLocation id = buf.readResourceLocation();
            R recipe = parent.manager.getById(parent.parent.getLevel(), id);
            if (recipe != null) {
                return new PendingRecipe<>(input, parent.inputSerializer, output, parent.outputSerializer, recipe);
            }
            GregTechMod.LOGGER.warn("Cannot find recipe '{}', abandoning process", id);
            return null;
        }
    }

    public static class SISO extends RecipeHandler<SimpleMachineBlockEntity, SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack> {
        private static final ResourceLocation NAME = location("siso_recipe_handler");

        public SISO(SimpleMachineBlockEntity parent, RecipeManager<SISORecipe<ItemStack, ItemStack>, ItemStack> manager, RecipeOutputType<ItemStack> inputSerializer, RecipeOutputType<ItemStack> outputSerializer) {
            super(parent, manager, inputSerializer, outputSerializer);
        }

        @Override
        public ResourceLocation getName() {
            return NAME;
        }

        @Override
        public boolean accepts(ItemStack input) {
            return !this.parent.getLevel().isClientSide && this.manager.hasRecipeFor(this.parent.getLevel(), input);
        }

        @Override
        protected ItemStack getInput() {
            return this.parent.inputSlot.get();
        }

        @Override
        protected boolean canAddOutput(SISORecipe<ItemStack, ItemStack> recipe) {
            ItemStack remainder = this.parent.outputSlot.add(0, recipe.getOutput(), true);
            return remainder.isEmpty() || this.parent.queueOutputSlot.add(0, remainder, true).isEmpty();
        }

        @Override
        protected void consumeInput(SISORecipe<ItemStack, ItemStack> recipe) {
            this.parent.inputSlot.shrink(0, recipe.getInput().getCount());
        }

        @Override
        protected void addOutput(SISORecipe<ItemStack, ItemStack> recipe) {
            ItemStack remainder = this.parent.outputSlot.add(0, recipe.getOutput().copy());
            this.parent.queueOutputSlot.add(0, remainder);
            this.parent.ejectOutput();
        }
    }
}
