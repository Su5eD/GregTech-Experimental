package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import mods.gregtechmod.api.recipe.IRecipeBlastFurnace;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.recipe.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;

public class RecipeBlastFurnace extends Recipe<List<IRecipeIngredient>, List<ItemStack>> implements IRecipeBlastFurnace {
    private final int heat;
    private final boolean universal;

    private RecipeBlastFurnace(List<IRecipeIngredient> input, List<ItemStack> output, int duration, double energyCost, int heat, boolean universal) {
        super(input, output, duration, energyCost);
        this.heat = heat;
        this.universal = universal;
    }

    @JsonCreator
    public static RecipeBlastFurnace create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                            @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                            @JsonProperty(value = "duration", required = true) int duration,
                                            @JsonProperty(value = "energyCost") double energyCost,
                                            @JsonProperty(value = "heat", required = true) int heat,
                                            @JsonProperty(value = "universal") boolean universal) {
        List<IRecipeIngredient> adjustedInput = RecipeUtil.adjustInputCount("blast furnace", input, output, 2);
        List<ItemStack> adjustedOutput = RecipeUtil.adjustOutputCount("blast furnace", output, 2);

        RecipeBlastFurnace recipe = new RecipeBlastFurnace(adjustedInput, adjustedOutput, duration, energyCost <= 0 ? 128 : energyCost, heat, universal);

        if (!RecipeUtil.validateRecipeIO("blast furnace", adjustedInput, adjustedOutput)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public int getHeat() {
        return this.heat;
    }

    @Override
    public boolean isUniversal() {
        return this.universal;
    }

    @Override
    protected MoreObjects.ToStringHelper toStringHelper() {
        return super.toStringHelper()
                .add("heat", heat)
                .add("universal", universal);
    }
}
