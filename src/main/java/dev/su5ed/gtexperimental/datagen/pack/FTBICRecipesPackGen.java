package dev.su5ed.gtexperimental.datagen.pack;

import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.ftb.mods.ftbic.world.ResourceElements;
import dev.ftb.mods.ftbic.world.ResourceType;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipeName;
import io.alwa.mods.myrtrees.common.item.MyrtreesItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

import static dev.su5ed.gtexperimental.datagen.RecipeGen.*;
import static dev.su5ed.gtexperimental.recipe.gen.ModRecipeBuilders.*;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.MERCURY;
import static dev.su5ed.gtexperimental.recipe.type.RecipeUtil.WATER;

public class FTBICRecipesPackGen extends RecipeProvider {
    public static final String NAME = "ftbic_compat";
    private static final String NAMESPACE = Reference.MODID + "_" + NAME;

    public FTBICRecipesPackGen(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> finishedRecipeConsumer) {
        // Assembler
        assembler(ModRecipeIngredientTypes.ITEM.of(GregTechTags.UNIVERSAL_IRON_PLATE, 8), ModRecipeIngredientTypes.ITEM.of(Component.MACHINE_PARTS), new ItemStack(FTBICItems.MACHINE_BLOCK.get()), 400, 8)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, assemblerId("machine"));
        assembler(ModRecipeIngredientTypes.ITEM.of(FTBICItems.CARBON_FIBERS.item.get(), 4), new ItemStack(FTBICItems.CARBON_FIBER_MESH.item.get()), 800, 2)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, assemblerId("carbon_mesh"));
        assembler(ModRecipeIngredientTypes.ITEM.of(FTBICItems.REINFORCED_STONE.get(), 4), ModRecipeIngredientTypes.ITEM.of(Items.GLASS, 4), new ItemStack(FTBICItems.REINFORCED_GLASS.get(), 4), 400, 4)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, assemblerId("reinforced_glass"));
        assembler(ModRecipeIngredientTypes.ITEM.of(FTBICItems.ADVANCED_ALLOY.item.get()), ModRecipeIngredientTypes.ITEM.of(Tags.Items.STONE, 8), new ItemStack(FTBICItems.REINFORCED_STONE.get(), 4), 400, 4)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, assemblerId("reinforced_stone"));

        // Bender
        bender(ModRecipeIngredientTypes.ITEM.of(GregTechTags.ingot("mixed_metal")), new ItemStack(FTBICItems.ADVANCED_ALLOY.item.get()), 100, 8)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, benderId("alloy"));

        // Fusion
        fusionSolid(ModRecipeIngredientTypes.FLUID.of(ModFluid.WOLFRAMIUM.getTag()), ModRecipeIngredientTypes.FLUID.of(ModFluid.LITHIUM.getTag()), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.INGOT).orElseThrow().get()), 512, 32768, 150000000)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, fluidSolidId("iridium_chunk"));

        // Implosion
        implosion(ModRecipeIngredientTypes.ITEM.of(Dust.DIAMOND.getTag(), 4), new ItemStack(Items.DIAMOND, 3), Dust.DARK_ASHES.getItemStack(16), 32)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, implosionId("diamond"));
        implosion(ModRecipeIngredientTypes.ITEM.of(Ingot.IRIDIUM_ALLOY.getTag()), new ItemStack(FTBICItems.IRIDIUM_ALLOY.item.get()), Dust.DARK_ASHES.getItemStack(4), 8)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, implosionId("iridium_alloy"));

        // Industrial Centrifuge
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(GregTechTags.material("logs", "rubber"), 16), new ItemStack(MyrtreesItems.LATEX.get(), 8), new ItemStack(Items.VINE, 6), ModFluid.METHANE.getBuckets(1), ModFluid.CARBON.getBuckets(4), 5000)
            .addConditions(MYRTREES_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("rubber_log"));
        industrialCentrifuge(ModRecipeIngredientTypes.HYBRID.of(MyrtreesItems.LATEX.get(), 4), new ItemStack(FTBICItems.RUBBER.item.get(), 14), new ItemStack(Items.VINE), new ItemStack(Items.GRASS), 1300)
            .addConditions(MYRTREES_LOADED)
            .build(finishedRecipeConsumer, industrialCentrifugeId("latex"));

        // Industrial Grinder
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), WATER, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.DUST).orElseThrow().get(), 2), Smalldust.PLATINUM.getItemStack(2))
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, industrialGrinderId("iridium_ore_water"));
        industrialGrinder(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), MERCURY, new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.DUST).orElseThrow().get(), 2), Dust.PLATINUM.getItemStack(2))
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, industrialGrinderId("iridium_ore_mercury"));

        // Pulverizer
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Ore.IRIDIUM.getTag()), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.DUST).orElseThrow().get(), 2), Dust.PLATINUM.getItemStack())
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("ores_iridium"));
        pulverizer(ModRecipeIngredientTypes.ITEM.ofTags(Ore.SHELDONITE.getTag(), GregTechTags.ore("platinum")), Dust.PLATINUM.getItemStack(2), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.DUST).orElseThrow().get()))
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("ores_sheldonite"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(Component.IRIDIUM_GEAR), new ItemStack(FTBICItems.getResourceFromType(ResourceElements.IRIDIUM, ResourceType.DUST).orElseThrow().get(), 6))
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("iridium_gear"));
        pulverizer(ModRecipeIngredientTypes.ITEM.of(FTBICItems.MACHINE_BLOCK.get()), Dust.IRON.getItemStack(8))
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, pulverizerId("machine_block"));

        // Vacuum Freezer
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.ofDamaged(FTBICItems.SMALL_COOLANT_CELL.get()), new ItemStack(FTBICItems.SMALL_COOLANT_CELL.get()), 100)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, vacuumFreezerId("small_coolant_cell"));
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.ofDamaged(FTBICItems.MEDIUM_COOLANT_CELL.get()), new ItemStack(FTBICItems.MEDIUM_COOLANT_CELL.get()), 300)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, vacuumFreezerId("medium_coolant_cell"));
        vacuumFreezerSolid(ModRecipeIngredientTypes.ITEM.ofDamaged(FTBICItems.LARGE_COOLANT_CELL.get()), new ItemStack(FTBICItems.LARGE_COOLANT_CELL.get()), 600)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, vacuumFreezerId("large_coolant_cell"));

        // Wiremill
        wiremill(ModRecipeIngredientTypes.ITEM.of(Dust.COAL.getTag(), 4), new ItemStack(FTBICItems.CARBON_FIBERS.item.get()), 400, 2)
            .addConditions(FTBIC_LOADED)
            .build(finishedRecipeConsumer, wiremillId("carbon_fibre"));
    }

    private static RecipeName assemblerId(String name) {
        return RecipeName.common(NAMESPACE, "assembler", name);
    }

    private static RecipeName benderId(String name) {
        return RecipeName.common(NAMESPACE, "bender", name);
    }

    private static RecipeName fluidSolidId(String name) {
        return RecipeName.common(NAMESPACE, "fusion_solid", name);
    }

    protected RecipeName implosionId(String name) {
        return RecipeName.common(NAMESPACE, "implosion", name);
    }

    protected RecipeName industrialCentrifugeId(String name) {
        return RecipeName.common(NAMESPACE, "industrial_centrifuge", name);
    }

    protected RecipeName industrialGrinderId(String name) {
        return RecipeName.common(NAMESPACE, "industrial_grinder", name);
    }

    protected RecipeName pulverizerId(String name) {
        return RecipeName.common(NAMESPACE, "pulverizer", name);
    }

    protected RecipeName vacuumFreezerId(String name) {
        return RecipeName.common(NAMESPACE, "vacuum_freezer", name);
    }

    protected RecipeName wiremillId(String name) {
        return RecipeName.common(NAMESPACE, "wiremill", name);
    }
}
