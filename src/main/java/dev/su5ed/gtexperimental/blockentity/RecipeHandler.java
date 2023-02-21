package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.GregTechMod;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.blockentity.base.SimpleMachineBlockEntity;
import dev.su5ed.gtexperimental.blockentity.component.GtComponentBase;
import dev.su5ed.gtexperimental.network.NetworkHandler;
import dev.su5ed.gtexperimental.network.Networked;
import dev.su5ed.gtexperimental.recipe.type.ModRecipeProperty;
import dev.su5ed.gtexperimental.recipe.type.SIMORecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public abstract class RecipeHandler<T extends BaseBlockEntity, R extends BaseRecipe<?, IN, OUT, ? super R>, IN, OUT> extends GtComponentBase<T> {
    private final RecipeManager<R, IN> manager;
    private final RecipeOutputType<IN> inputSerializer;
    private final RecipeOutputType<OUT> outputSerializer;

    @Nullable
    @Networked
    private PendingRecipe<R, IN, OUT> pendingRecipe;
    @Networked
    private int progress;

    static {
        NetworkHandler.registerHandler(RecipeHandler.class, "pendingRecipe", new PendingRecipeNetworkSerializer<>());
    }

    protected RecipeHandler(T parent, RecipeManager<R, IN> manager, RecipeOutputType<IN> inputSerializer, RecipeOutputType<OUT> outputSerializer) {
        super(parent);

        this.manager = manager;
        this.inputSerializer = inputSerializer;
        this.outputSerializer = outputSerializer;
    }

    protected abstract IN getInput();

    protected boolean canProcessRecipe(R recipe) {
        // TODO Check parent energy && can add output
        return true;
    }

    protected abstract void consumeInput(R recipe);

    protected abstract void addOutput(R recipe);

    public int getProgress() {
        return this.progress;
    }

    public int getMaxProgress() {
        return this.pendingRecipe == null ? 0 : this.pendingRecipe.recipe.getProperty(ModRecipeProperty.DURATION);
    }

    public void checkRecipe() {
        if (this.pendingRecipe == null) {
            IN input = getInput();
            R recipe = this.manager.getRecipeFor(this.parent.getLevel(), input);
            if (recipe != null && canProcessRecipe(recipe)) {
                this.pendingRecipe = new PendingRecipe<>(this.inputSerializer.copy(input), this.inputSerializer, recipe.getOutput(), this.outputSerializer, recipe);
                consumeInput(recipe);
                this.parent.setActive(true);
            }
        }
    }

    @Override
    public void tickServer() {
        super.tickServer();

        if (this.pendingRecipe != null) {
            int maxProgress = getMaxProgress();
            if (this.progress++ >= maxProgress) {
                addOutput(this.pendingRecipe.recipe);
                this.pendingRecipe = null;
                this.parent.setActive(false);
                this.progress = 0;
                checkRecipe();
            }
            else {
                this.parent.setChanged();
            }
        }
    }

    @Override
    public void onFieldUpdate(String name) {}

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
            CompoundTag pendingRecipeTag = tag.getCompound("pendingRecipe");
            this.pendingRecipe = PendingRecipe.fromNBT(pendingRecipeTag, this.inputSerializer, this.outputSerializer, this.parent.getLevel(), this.manager);
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

    public static class SIMO<R extends SIMORecipe<ItemStack, List<ItemStack>>> extends RecipeHandler<SimpleMachineBlockEntity, R, ItemStack, List<ItemStack>> {

        protected SIMO(SimpleMachineBlockEntity parent, RecipeManager<R, ItemStack> manager, RecipeOutputType<ItemStack> inputSerializer, RecipeOutputType<List<ItemStack>> outputSerializer) {
            super(parent, manager, inputSerializer, outputSerializer);
        }

        @Override
        protected ItemStack getInput() {
            return this.parent.inputSlot.get();
        }

        @Override
        protected void consumeInput(R recipe) {
            this.parent.inputSlot.extract(0, recipe.getInput().getCount());
        }

        @Override
        protected void addOutput(R recipe) {
            // TODO add() method
            this.parent.outputSlot.setItem(0, recipe.getOutput().get(0).copy());
        }

        @Override
        public ResourceLocation getName() {
            return location("simo_recipe_handler");
        }
    }
}
