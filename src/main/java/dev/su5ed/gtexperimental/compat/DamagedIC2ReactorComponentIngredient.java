package dev.su5ed.gtexperimental.compat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.recipe.type.SimpleIngredientSerializer;
import dev.su5ed.gtexperimental.recipe.type.VanillaRecipeIngredient;
import ic2.api.reactor.IReactorComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.stream.Stream;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class DamagedIC2ReactorComponentIngredient extends AbstractIngredient {
    public static final ResourceLocation NAME = location("damaged_ic2_reactor_component");
    public static final IIngredientSerializer<DamagedIC2ReactorComponentIngredient> SERIALIZER = new SimpleIngredientSerializer<>(DamagedIC2ReactorComponentIngredient::new);

    public DamagedIC2ReactorComponentIngredient(ItemStack stack) {
        this(Stream.of(new ItemValue(stack)));
    }

    private DamagedIC2ReactorComponentIngredient(Stream<? extends Value> values) {
        super(values);
    }

    @Override
    public boolean isSimple() {
        return true;
    }

    @Override
    public IIngredientSerializer<? extends Ingredient> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public JsonElement toJson() {
        JsonObject json = this.values[0].serialize();
        json.addProperty("type", CraftingHelper.getID(SERIALIZER).toString());
        return json;
    }

    @Override
    public boolean test(@Nullable ItemStack stack) {
        if (stack.getItem() instanceof IReactorComponent component) {
            boolean hasStoredHeat = false;
            try {
                hasStoredHeat = component.getCurrentHeat(stack, null, 0, 0) > 0;
            } catch (Throwable ignored) {

            }
            return hasStoredHeat && super.test(stack);
        }
        return false;
    }

    public static RecipeIngredient<ItemStack> recipeIngredient(ItemLike item) {
        return new VanillaRecipeIngredient(new DamagedIC2ReactorComponentIngredient(new ItemStack(item)));
    }
}
