package dev.su5ed.gtexperimental.datagen.recipe;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.ftb.mods.ftbic.world.ResourceElements;
import dev.ftb.mods.ftbic.world.ResourceType;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import twilightforest.init.TFItems;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.datagen.RecipeGen.*;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.pulverizer;

public final class PulverizerRecipesGen implements ModRecipeProvider {
    public static final PulverizerRecipesGen INSTANCE = new PulverizerRecipesGen();

    private PulverizerRecipesGen() {}

    @Override
    public void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Tags.Items.SANDSTONE), new ItemStack(Items.SAND))
            .build(finishedRecipeConsumer, id("sandstone")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Tags.Items.END_STONES), Dust.ENDSTONE.getItemStack(), Dust.ENDSTONE.getItemStack())
            .build(finishedRecipeConsumer, id("end_stones"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Tags.Items.OBSIDIAN), Dust.OBSIDIAN.getItemStack())
            .build(finishedRecipeConsumer, id("obsidian")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Tags.Items.NETHERRACK), Dust.NETHERRACK.getItemStack(), Dust.NETHERRACK.getItemStack())
            .build(finishedRecipeConsumer, id("netherrack")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.RED_SANDSTONE), Dust.REDROCK.getItemStack(), Dust.REDROCK.getItemStack())
            .build(finishedRecipeConsumer, id("red_sandstone"));
        pulverizer(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.stone("marble"), GregTechTags.stone("quarried")), Dust.MARBLE.getItemStack(), Dust.MARBLE.getItemStack())
            .build(finishedRecipeConsumer, id("marble"));
        pulverizer(ModRecipeIngredientTypes.ITEM.ofTags(GregTechTags.stone("basalt"), GregTechTags.stone("abyssal")), Dust.BASALT.getItemStack(), Dust.BASALT.getItemStack())
            .build(finishedRecipeConsumer, id("basalt"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_LAPIS), new ItemStack(Items.LAPIS_LAZULI, 12), Dust.LAZURITE.getItemStack())
            .build(finishedRecipeConsumer, id("ores_lapis")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ore.SODALITE.getTag()), Dust.SODALITE.getItemStack(12), Dust.ALUMINIUM.getItemStack())
            .build(finishedRecipeConsumer, id("ores_sodalite"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_REDSTONE), new ItemStack(Items.REDSTONE, 10), new ItemStack(Items.GLOWSTONE_DUST))
            .build(finishedRecipeConsumer, id("ores_redstone")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Tags.Items.ORES_QUARTZ), new ItemStack(Items.QUARTZ, 3), Dust.NETHERRACK.getItemStack())
            .build(finishedRecipeConsumer, id("ores_quartz"));

        oreProcessing(Tags.Items.ORES_IRON, Dust.IRON, Dust.NICKEL, finishedRecipeConsumer); // Overwrite
        oreProcessing(Tags.Items.ORES_GOLD, Dust.GOLD, Dust.COPPER, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "silver"), Dust.SILVER, Dust.LEAD, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "lead"), Dust.LEAD, Dust.SILVER, finishedRecipeConsumer); // Overwrite
        oreProcessing(Ore.GALENA.getTag(), Dust.GALENA, Dust.SULFUR, 50, finishedRecipeConsumer);
        oreProcessing(Tags.Items.ORES_DIAMOND, Dust.DIAMOND, Dust.COAL, finishedRecipeConsumer); // Overwrite
        oreProcessing(Tags.Items.ORES_EMERALD, Dust.EMERALD, Dust.OLIVINE, finishedRecipeConsumer); // Overwrite
        oreProcessing(Ore.RUBY.getTag(), Dust.RUBY, Dust.RED_GARNET, finishedRecipeConsumer); // Overwrite
        oreProcessing(Ore.SAPPHIRE.getTag(), Dust.SAPPHIRE, Dust.GREEN_SAPPHIRE, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "green_sapphire"), Dust.GREEN_SAPPHIRE, Dust.SAPPHIRE, finishedRecipeConsumer); // Overwrite
        oreProcessing(Ore.OLIVINE.getTag(), Dust.OLIVINE, Dust.EMERALD, finishedRecipeConsumer); // Overwrite
        oreProcessing(Tags.Items.ORES_COAL, Dust.COAL, Dust.THORIUM, finishedRecipeConsumer); // Overwrite
        oreProcessing(Tags.Items.ORES_COPPER, Dust.COPPER, Dust.GOLD, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "tin"), Dust.TIN, Dust.IRON, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "zinc"), Dust.ZINC, Dust.TIN, finishedRecipeConsumer);
        oreProcessing(Ore.CASSITERITE.getTag(), Dust.TIN, 3, Dust.TIN, 10, finishedRecipeConsumer);
        oreProcessing(Ore.TETRAHEDRITE.getTag(), Dust.COPPER, Dust.ZINC, finishedRecipeConsumer);
        oreProcessing(GregTechTags.material("ores", "antimony"), Dust.ANTIMONY, Dust.ZINC, finishedRecipeConsumer);
        oreProcessing(GregTechTags.material("ores", "nickel"), Dust.NICKEL, Dust.TIN, finishedRecipeConsumer);
        oreProcessing(Ore.PYRITE.getTag(), Dust.PYRITE, 3, Dust.IRON, 10, finishedRecipeConsumer); // Overwrite
        oreProcessing(Ore.CINNABAR.getTag(), Dust.CINNABAR, 3, Items.REDSTONE, 10, finishedRecipeConsumer); // Overwrite
        oreProcessing(Ore.SPHALERITE.getTag(), Dust.SPHALERITE, 3, Dust.ZINC, 10, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "aluminium"), Dust.ALUMINIUM, Dust.TITANIUM, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "steel"), Dust.STEEL, 2, Dust.NICKEL, 2, 10, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "titanium"), Dust.TITANIUM, Dust.TITANIUM, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "chromium"), Dust.CHROME, Dust.RUBY, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "electrum"), Dust.ELECTRUM, Dust.GOLD, finishedRecipeConsumer); // Overwrite
        oreProcessing(Ore.TUNGSTATE.getTag(), Dust.TUNGSTEN, Dust.MANGANESE, finishedRecipeConsumer); // Overwrite
        oreProcessing(Ore.BAUXITE.getTag(), Dust.BAUXITE, 4, Dust.ALUMINIUM, 10, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "sulfur"), Dust.SULFUR, 8, Dust.SULFUR, 10, finishedRecipeConsumer);
        oreProcessing(GregTechTags.material("ores", "saltpeter"), Dust.SALTPETER, 5, Dust.SALTPETER, 10, finishedRecipeConsumer);
        oreProcessing(GregTechTags.material("ores", "phosphorite"), Dust.PHOSPHORUS, 3, Dust.PHOSPHORUS, 10, finishedRecipeConsumer);
        oreProcessing(GregTechTags.material("ores", "magnesium"), Dust.MAGNESIUM, Dust.IRON, finishedRecipeConsumer);
        oreProcessing(GregTechTags.material("ores", "thorium"), Dust.THORIUM, Dust.TITANIUM, finishedRecipeConsumer); // Overwrite
        oreProcessing(GregTechTags.material("ores", "plutonium"), Dust.PLUTONIUM, Dust.URANIUM, finishedRecipeConsumer); // Overwrite

        simple(GregTechTags.ingot("uranium"), Dust.URANIUM, finishedRecipeConsumer);
        simple(Ingot.PLUTONIUM, Dust.PLUTONIUM, finishedRecipeConsumer);
        simple(Ingot.THORIUM, Dust.THORIUM, finishedRecipeConsumer);
        simple(GregTechTags.ingot("manganese"), Dust.MANGANESE, finishedRecipeConsumer);
        simple(GregTechTags.ingot("magnesium"), Dust.MAGNESIUM, finishedRecipeConsumer);
        simple(Ingot.SILVER, Dust.SILVER, finishedRecipeConsumer); // Overwrite
        simple(GregTechTags.ingot("bronze"), Dust.BRONZE, finishedRecipeConsumer);
        simple(Tags.Items.INGOTS_COPPER, Dust.COPPER, finishedRecipeConsumer); // Overwrite
        simple(GregTechTags.ingot("tin"), Dust.TIN, finishedRecipeConsumer); // Overwrite
        simple(Tags.Items.INGOTS_IRON, Dust.IRON, finishedRecipeConsumer); // Overwrite
        simple(Tags.Items.INGOTS_GOLD, Dust.GOLD, finishedRecipeConsumer); // Overwrite
        simple(Ingot.ALUMINIUM, Dust.ALUMINIUM, finishedRecipeConsumer);
        simple(Ingot.TITANIUM, Dust.TITANIUM, finishedRecipeConsumer);
        simple(Ingot.CHROME, Dust.CHROME, finishedRecipeConsumer);
        simple(Ingot.ELECTRUM, Dust.ELECTRUM, finishedRecipeConsumer);
        simple(Ingot.TUNGSTEN, Dust.TUNGSTEN, finishedRecipeConsumer);
        simple(Ingot.STEEL, Dust.STEEL, finishedRecipeConsumer); // Overwrite
        simple(Ingot.LEAD, Dust.LEAD, finishedRecipeConsumer); // Overwrite
        simple(Ingot.ZINC, Dust.ZINC, finishedRecipeConsumer);
        simple(Ingot.BRASS, Dust.BRASS, finishedRecipeConsumer);
        simple(Ingot.PLATINUM, Dust.PLATINUM, finishedRecipeConsumer);
        simple(Ingot.NICKEL, Dust.NICKEL, finishedRecipeConsumer);
        simple(Miscellaneous.RUBY, Dust.RUBY, finishedRecipeConsumer);
        simple(Miscellaneous.SAPPHIRE, Dust.SAPPHIRE, finishedRecipeConsumer);
        simple(Miscellaneous.GREEN_SAPPHIRE, Dust.GREEN_SAPPHIRE, finishedRecipeConsumer);
        simple(Tags.Items.ENDER_PEARLS, Dust.ENDER_PEARL, finishedRecipeConsumer);
        simple(Tags.Items.GEMS_DIAMOND, Dust.DIAMOND, finishedRecipeConsumer); // Overwrite
        simple(Tags.Items.GEMS_EMERALD, Dust.EMERALD, finishedRecipeConsumer); // Overwrite
        simple(Miscellaneous.OLIVINE, Dust.OLIVINE, finishedRecipeConsumer);
        simple(Miscellaneous.RED_GARNET, Dust.RED_GARNET, finishedRecipeConsumer);
        simple(Miscellaneous.YELLOW_GARNET, Dust.YELLOW_GARNET, finishedRecipeConsumer);
        simple(Items.CHAINMAIL_HELMET, Smalldust.STEEL, 5, finishedRecipeConsumer); // Overwrite
        simple(Items.CHAINMAIL_CHESTPLATE, Smalldust.STEEL, 8, finishedRecipeConsumer); // Overwrite
        simple(Items.CHAINMAIL_LEGGINGS, Smalldust.STEEL, 7, finishedRecipeConsumer); // Overwrite
        simple(Items.CHAINMAIL_BOOTS, Smalldust.STEEL, 4, finishedRecipeConsumer); // Overwrite
        simple(Items.LIGHT_WEIGHTED_PRESSURE_PLATE, Dust.GOLD, 2, finishedRecipeConsumer); // Overwrite
        simple(Items.HEAVY_WEIGHTED_PRESSURE_PLATE, Dust.IRON, 2, finishedRecipeConsumer); // Overwrite
        simple(Component.ALUMINIUM_HULL, Dust.ALUMINIUM, 6, finishedRecipeConsumer);
        simple(Component.BRONZE_HULL, Dust.BRONZE, 6, finishedRecipeConsumer);
        simple(Component.BRASS_HULL, Dust.BRASS, 6, finishedRecipeConsumer);
        simple(Component.STEEL_HULL, Dust.STEEL, 6, finishedRecipeConsumer);
        simple(Component.TITANIUM_HULL, Dust.TITANIUM, 6, finishedRecipeConsumer);
        simple(Component.BRONZE_GEAR, Dust.BRONZE, 6, finishedRecipeConsumer);
        simple(Component.STEEL_GEAR, Dust.STEEL, 6, finishedRecipeConsumer);
        simple(Component.TITANIUM_GEAR, Dust.TITANIUM, 6, finishedRecipeConsumer);
        simple(Items.FURNACE, Items.SAND, 7, finishedRecipeConsumer);
        simple(Items.STONE_BUTTON, Items.SAND, 1, finishedRecipeConsumer);
        simple(ItemTags.SIGNS, Dust.WOOD, 2, finishedRecipeConsumer);
        simple(ItemTags.WOODEN_DOORS, Dust.WOOD, 6, finishedRecipeConsumer);
        simple(Tags.Items.CHESTS_WOODEN, Dust.WOOD, 8, finishedRecipeConsumer);
        simple(ItemTags.WOODEN_BUTTONS, Dust.WOOD, finishedRecipeConsumer);
        simple(Items.STONE_PRESSURE_PLATE, Items.SAND, 2, finishedRecipeConsumer);
        simple(ItemTags.WOODEN_PRESSURE_PLATES, Dust.WOOD, 2, finishedRecipeConsumer);
        simple(Items.LADDER, Dust.WOOD, 1, finishedRecipeConsumer);
        simple(Items.GLOWSTONE, Items.GLOWSTONE_DUST, 4, finishedRecipeConsumer); // Overwrite
        simple(Items.SUGAR_CANE, Items.SUGAR, 2, finishedRecipeConsumer); // Overwrite
        simple(Items.CLAY, Dust.CLAY, 2, finishedRecipeConsumer);
        simple(Items.SNOW, Items.SNOWBALL, 4, finishedRecipeConsumer);
        simple(Items.CHARCOAL, Dust.CHARCOAL, 1, finishedRecipeConsumer); // Overwrite
        simple(Items.PUMPKIN, Items.PUMPKIN_SEEDS, 4, finishedRecipeConsumer); // Overwrite
        simple(Items.MELON_SLICE, Items.MELON_SEEDS, 1, finishedRecipeConsumer); // Overwrite
        simple(Items.WHEAT, Miscellaneous.FLOUR, 1, finishedRecipeConsumer); // Overwrite
        simple(Items.ENDER_EYE, Dust.ENDER_EYE, 2, finishedRecipeConsumer); // Overwrite

        pulverizer(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("gems", "apatite")), new ItemStack(Ic2Items.FERTILIZER, 4), Dust.PHOSPHORUS.getItemStack(), 50)
            .build(finishedRecipeConsumer, id("gems_apatite"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.COAL), Dust.COAL.getItemStack())
            .build(finishedRecipeConsumer, id("coal")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.CLOCK), Dust.GOLD.getItemStack(4), new ItemStack(Items.REDSTONE), 95)
            .build(finishedRecipeConsumer, id("clock"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.COMPASS), Dust.IRON.getItemStack(4), new ItemStack(Items.REDSTONE), 95)
            .build(finishedRecipeConsumer, id("compass"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.SHEARS), Dust.IRON.getItemStack(2))
            .build(finishedRecipeConsumer, id("shears"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.MINECART), Dust.IRON.getItemStack(5))
            .build(finishedRecipeConsumer, id("minecart")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.BUCKET), Dust.IRON.getItemStack(3))
            .build(finishedRecipeConsumer, id("bucket")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.IRON_DOOR), Dust.IRON.getItemStack(6))
            .build(finishedRecipeConsumer, id("iron_door")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.CAULDRON), Dust.IRON.getItemStack(7))
            .build(finishedRecipeConsumer, id("cauldron")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.IRON_BARS, 2), Smalldust.IRON.getItemStack(3))
            .build(finishedRecipeConsumer, id("iron_bars")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.ENCHANTING_TABLE), Dust.DIAMOND.getItemStack(2), Dust.OBSIDIAN.getItemStack(4), 95)
            .build(finishedRecipeConsumer, id("enchanting_table"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.REDSTONE_TORCH), Smalldust.WOOD.getItemStack(2), new ItemStack(Items.REDSTONE), 95)
            .build(finishedRecipeConsumer, id("redstone_torch"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.LEVER), new ItemStack(Items.SAND), Smalldust.WOOD.getItemStack(2), 95)
            .build(finishedRecipeConsumer, id("lever"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.ITEM_FRAME), new ItemStack(Items.LEATHER), Dust.WOOD.getItemStack(4), 95)
            .build(finishedRecipeConsumer, id("item_frame"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.BOW), new ItemStack(Items.STRING, 3), Smalldust.WOOD.getItemStack(3), 95)
            .build(finishedRecipeConsumer, id("bow"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.REDSTONE_LAMP), new ItemStack(Items.GLOWSTONE_DUST, 4), new ItemStack(Items.REDSTONE, 4), 90)
            .build(finishedRecipeConsumer, id("redstone_lamp"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.MELON), new ItemStack(Items.MELON, 8), new ItemStack(Items.MELON_SEEDS), 80)
            .build(finishedRecipeConsumer, id("melon"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Items.FLINT), Smalldust.FLINT.getItemStack(2), Smalldust.FLINT.getItemStack())
            .build(finishedRecipeConsumer, id("flint")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(ItemTags.WOOL), new ItemStack(Items.STRING, 2), new ItemStack(Items.STRING), 50)
            .build(finishedRecipeConsumer, id("wool")); // Overwrite

        buildOtherModRecipes(finishedRecipeConsumer);
    }

    private void buildOtherModRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        pulverizer(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "uranium")), Dust.URANIUM.getItemStack(2), Dust.PLUTONIUM.getItemStack())
            .addConditions(NOT_IC2_LOADED)
            .build(finishedRecipeConsumer, id("ores_uranium")); // Overwrite

        // IC2
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM), new ItemStack(Ic2Items.IRIDIUM_ORE, 2), Dust.PLATINUM.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/ores_iridium")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.ofTags(Ore.SHELDONITE.getTag(), GregTechTags.material("ores", "platinum")), Dust.PLATINUM.getItemStack(2), new ItemStack(Ic2Items.IRIDIUM_ORE))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/ores_sheldonite")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(GregTechTags.material("ores", "uranium")), new ItemStack(Ic2Items.CRUSHED_URANIUM, 2), Dust.PLUTONIUM.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/ores_uranium")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_FENCE, 2), Smalldust.IRON.getItemStack(3))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/iron_fence")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.EMPTY_CELL), Dust.TIN.getItemStack(2))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/empty_cell")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.IRON_FURNACE), Dust.TIN.getItemStack(5))
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/iron_furnace")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ic2Items.CROP_STICK), Dust.WOOD.getItemStack())
            .addConditions(IC2_LOADED)
            .build(finishedRecipeConsumer, id("ic2/crop_stick"));

        // FTBIC
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.DUST).orElseThrow().get(), 2), Dust.PLATINUM.getItemStack())
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/ores_iridium")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.ofTags(Ore.SHELDONITE.getTag(), GregTechTags.material("ores", "platinum")), Dust.PLATINUM.getItemStack(2), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.DUST).orElseThrow().get()))
            .addConditions(NOT_IC2_LOADED, FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/ores_sheldonite")); // Overwrite
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Component.IRIDIUM_GEAR), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.DUST).orElseThrow().get(), 6))
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, id("ftbic/iridium_gear"));

        // Twilight Forest
        pulverizer(ModRecipeIngredientTypes.ITEM.of(TFItems.LIVEROOT.get()), new ItemStack(Items.STICK, 2), new ItemStack(Items.STICK), 30)
            .addConditions(TWILIGHT_FOREST_LOADED)
            .build(finishedRecipeConsumer, id("twilightforest/liveroot"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(TFItems.DIAMOND_MINOTAUR_AXE.get()), Dust.DIAMOND.getItemStack(), Dust.WOOD.getItemStack(), 50)
            .addConditions(TWILIGHT_FOREST_LOADED)
            .build(finishedRecipeConsumer, id("twilightforest/diamond_minotaur_axe"));
    }

    private static void simple(TaggedItemProvider tag, ItemLike output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        simple(tag.getTag(), output, finishedRecipeConsumer);
    }

    private static void simple(ItemLike input, ItemLike output, int count, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        pulverizer(ModRecipeIngredientTypes.ITEM.of(input), new ItemStack(output, count))
            .build(finishedRecipeConsumer, id(GtUtil.itemName(input)));
    }

    private static void simple(TagKey<Item> tag, ItemLike output, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        simple(tag, output, 1, finishedRecipeConsumer);
    }

    private static void simple(TagKey<Item> tag, ItemLike output, int count, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        pulverizer(ModRecipeIngredientTypes.ITEM.of(tag), new ItemStack(output))
            .build(finishedRecipeConsumer, id(GtUtil.tagName(tag)));
    }

    private static void oreProcessing(TagKey<Item> tag, ItemLike primaryOutput, ItemLike secondaryOutput, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        oreProcessing(tag, primaryOutput, secondaryOutput, 10, finishedRecipeConsumer);
    }

    private static void oreProcessing(TagKey<Item> tag, ItemLike primaryOutput, ItemLike secondaryOutput, int chance, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        oreProcessing(tag, primaryOutput, 2, secondaryOutput, chance, finishedRecipeConsumer);
    }

    private static void oreProcessing(TagKey<Item> tag, ItemLike primaryOutput, int outputCount, ItemLike secondaryOutput, int chance, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        oreProcessing(tag, primaryOutput, outputCount, secondaryOutput, 1, chance, finishedRecipeConsumer);
    }

    private static void oreProcessing(TagKey<Item> tag, ItemLike primaryOutput, int outputCount, ItemLike secondaryOutput, int secondaryOutputCount, int chance, Consumer<FinishedRecipe> finishedRecipeConsumer) {
        pulverizer(ModRecipeIngredientTypes.ITEM.of(tag), new ItemStack(primaryOutput, outputCount), new ItemStack(secondaryOutput, secondaryOutputCount), chance)
            .build(finishedRecipeConsumer, id(GtUtil.tagName(tag))); // Overwrite
    }

    private static ResourceLocation id(String name) {
        return location("pulverizer/" + name);
    }
}
