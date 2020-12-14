package mods.gregtechmod.recipe;

import com.fasterxml.jackson.annotation.JsonProperty;
import mods.gregtechmod.api.GregTechAPI;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class RecipeAssembler extends Recipe<List<ItemStack>, ItemStack> {

    private RecipeAssembler(List<ItemStack> input, ItemStack output, double energyCost, int duration) {
        super(input, output, energyCost, duration);
    }

    public static RecipeAssembler create(@JsonProperty(value = "input", required = true) List<ItemStack> input,
                                         @JsonProperty(value = "output", required = true) ItemStack output,
                                         @JsonProperty(value = "energyCost", required = true) double energyCost,
                                         @JsonProperty(value = "duration", required = true) int duration) {
        if (input.size() > 2) {
            List<String> names = input.stream().map(ItemStack::getDisplayName).collect(Collectors.toList());
            GregTechAPI.logger.error("Tried to add an assembler recipe with way too many inputs! Listing the input items: "+String.join(", ", names));
            input.forEach(GregTechAPI.logger::info);
        }
        return new RecipeAssembler(input, output, energyCost, duration);
    }
}
