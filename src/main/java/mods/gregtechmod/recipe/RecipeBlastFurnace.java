package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.recipe.IRecipeBlastFurnace;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.util.RecipeUtil;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeBlastFurnace extends Recipe<List<IRecipeIngredient>, List<ItemStack>> implements IRecipeBlastFurnace {
    private final int heat;

    private RecipeBlastFurnace(List<IRecipeIngredient> input, List<ItemStack> output, int heat, int duration) {
        super(input, output, 128, duration);
        this.heat = heat;
    }

    @JsonCreator
    public static RecipeBlastFurnace create(@JsonProperty(value = "input", required = true) List<IRecipeIngredient> input,
                                            @JsonProperty(value = "output", required = true) List<ItemStack> output,
                                            @JsonProperty(value = "heat", required = true) int heat,
                                            @JsonProperty(value = "duration", required = true) int duration) {
        if (output.size() > 2) {
            GregTechAPI.logger.error("Tried to add a blast furnace recipe for " + output.stream().map(ItemStack::getTranslationKey).collect(Collectors.joining(", ")) + " with way too many outputs! Reducing them to 2");
            output = output.subList(0, 2);
        }

        RecipeBlastFurnace recipe = new RecipeBlastFurnace(input, output, heat, duration);

        if (!RecipeUtil.validateRecipeIO("grinder", input, output)) recipe.invalid = true;

        return recipe;
    }

    @Override
    public int getHeat() {
        return this.heat;
    }

    @Override
    public String toString() {
        return "RecipeBlastFurnace{input="+this.input+",output="+this.output+",duration="+this.duration+",energyCost="+this.energyCost+",heat="+this.heat+"}";
    }
}
