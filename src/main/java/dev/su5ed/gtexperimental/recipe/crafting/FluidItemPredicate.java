package dev.su5ed.gtexperimental.recipe.crafting;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.IntStreamEx;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class FluidItemPredicate extends ItemPredicate {
    public static final ResourceLocation NAME = location("fluid");

    public static FluidItemPredicate of(TagKey<Fluid> tag) {
        return new FluidItemPredicate(tag);
    }
    
    public static FluidItemPredicate fromJson(JsonObject json) {
        String tagName = GsonHelper.getAsString(json, "fluid");
        TagKey<Fluid> tag = TagKey.create(ForgeRegistries.Keys.FLUIDS, new ResourceLocation(tagName));
        return new FluidItemPredicate(tag);
    }
    
    private final TagKey<Fluid> tag;
    
    private FluidItemPredicate(TagKey<Fluid> tag) {
        this.tag = tag;
    }

    @Override
    public boolean matches(@NotNull ItemStack stack) {
        return Optional.ofNullable(stack)
            .flatMap(s -> s.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM).resolve())
            .map(handler -> IntStreamEx.range(handler.getTanks())
                .mapToObj(handler::getFluidInTank)
                .map(FluidStack::getFluid)
                .anyMatch(f -> f.is(this.tag)))
            .orElse(false);
    }

    @NotNull
    @Override
    public JsonObject serializeToJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", NAME.toString());
        json.addProperty("fluid", this.tag.location().toString());
        return json;
    }
}
