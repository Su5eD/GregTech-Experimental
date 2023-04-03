package dev.su5ed.gtexperimental.recipe.type;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.BaseRecipeType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeProperties;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public abstract class BaseRecipeImpl<T extends BaseRecipeType<?, ?, ?, OUT>, RIN extends RecipeIngredient<IN>, IN, OUT, C extends BaseRecipeImpl<T, RIN, IN, OUT, C>> implements BaseRecipe<T, RIN, IN, OUT, C> {
    protected final T type;
    protected final RecipeSerializer<?> serializer;
    protected final ResourceLocation id;

    protected final RIN input;
    protected final OUT output;
    protected final RecipePropertyMap properties;

    public BaseRecipeImpl(T type, RecipeSerializer<?> serializer, ResourceLocation id, RIN input, OUT output, int duration, double energyCost) {
        this(type, serializer, id, input, output, RecipePropertyMap.builder()
            .duration(duration)
            .energyCost(energyCost)
            .build());
    }

    public BaseRecipeImpl(T type, RecipeSerializer<?> serializer, ResourceLocation id, RIN input, OUT output, RecipePropertyMap properties) {
        this(type, serializer, id, input, output, properties, false);
    }

    public BaseRecipeImpl(T type, RecipeSerializer<?> serializer, ResourceLocation id, RIN input, OUT output, RecipePropertyMap properties, boolean allowEmptyOutput) {
        this.type = type;
        this.serializer = serializer;
        this.id = id;
        this.input = input;
        this.output = output;
        this.properties = properties;

        this.properties.validate(this.id, this.type.getProperties());
        this.input.validate(this.id, "input");
        this.type.getOutputType().validate(this.id, "output", this.output, allowEmptyOutput);
    }

    @Override
    public boolean matches(Container container, Level level) {
        return false;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ItemStack assemble(Container container) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public T getType() {
        return this.type;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return this.serializer;
    }

    @Override
    public RIN getInput() {
        return this.input;
    }

    @Override
    public OUT getOutput() {
        return this.output;
    }

    @Override
    public RecipeProperties getProperties() {
        return this.properties;
    }

    @Override
    public boolean matches(IN input) {
        return this.input.test(input);
    }

    @Override
    public boolean matchesPartial(IN input) {
        return this.input.testPartial(input);
    }

    @Override
    public int compareInputCount(C other) {
        return other.getInput().getCount() - getInput().getCount();
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        this.input.toNetwork(buffer);
        this.type.getOutputType().toNetwork(buffer, this.output);
        this.properties.toNetwork(buffer);
    }
}
