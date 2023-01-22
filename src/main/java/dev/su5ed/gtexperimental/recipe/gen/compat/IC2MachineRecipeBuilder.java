package dev.su5ed.gtexperimental.recipe.gen.compat;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.datagen.RecipeGen;
import dev.su5ed.gtexperimental.recipe.gen.BaseRecipeBuilder;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

public class IC2MachineRecipeBuilder extends BaseRecipeBuilder {
    public static final ResourceLocation COMPRESSOR = new ResourceLocation(ModHandler.IC2_MODID, "compressor");

    private final ResourceLocation type;
    private final Value ingredient;
    private final int count;
    private final ItemStack result;

    public IC2MachineRecipeBuilder(ResourceLocation type, TagKey<Item> tag, int count, ItemStack result) {
        this(type, new TagValue(tag), count, result);
    }

    public IC2MachineRecipeBuilder(ResourceLocation type, ItemLike item, int count, ItemStack result) {
        this(type, new ItemValue(item), count, result);
    }

    private IC2MachineRecipeBuilder(ResourceLocation type, Value ingredient, int count, ItemStack result) {
        this.type = type;
        this.ingredient = ingredient;
        this.count = count;
        this.result = result;
        
        addConditions(RecipeGen.IC2_LOADED);
    }

    @Override
    public void serializeRecipe(JsonObject json) {
        json.addProperty("type", this.type.toString());
    }

    @Override
    public void serializeRecipeData(JsonObject json) {
        super.serializeRecipeData(json);

        JsonObject ingredient = new JsonObject();
        ingredient.add(this.ingredient.name(), this.ingredient.toJson());
        ingredient.addProperty("count", this.count);
        json.add("ingredient", ingredient);
        json.add("result", ModRecipeOutputTypes.ITEM.toJson(this.result));
    }

    @Override
    public RecipeSerializer<?> getType() {
        throw new UnsupportedOperationException();
    }

    public interface Value {
        String name();

        JsonElement toJson();
    }

    private record ItemValue(ItemLike item) implements Value {

        @Override
        public String name() {
            return "item";
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(GtUtil.itemId(this.item));
        }
    }

    private record TagValue(TagKey<Item> tag) implements Value {

        @Override
        public String name() {
            return "tag";
        }

        @Override
        public JsonElement toJson() {
            return new JsonPrimitive(this.tag.location().toString());
        }
    }
}
