package dev.su5ed.gtexperimental.datagen.pack;

import dev.ftb.mods.ftbic.block.FTBICElectricBlocks;
import dev.ftb.mods.ftbic.item.FTBICItems;
import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import io.alwa.mods.myrtrees.common.item.MyrtreesItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class FTBICTagsGen extends ItemTagsProvider {

    public FTBICTagsGen(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(GregTechTags.SMALL_EU_STORE).add(FTBICItems.MV_BATTERY.get());
        tag(GregTechTags.MEDIUM_EU_STORE).add(FTBICItems.HV_BATTERY.get());

        tag(GregTechTags.ADVANCED_CIRCUIT).add(FTBICItems.ADVANCED_CIRCUIT.item.get());
        tag(GregTechTags.ADVANCED_HEAT_VENT).add(FTBICItems.ADVANCED_HEAT_VENT.get());
        tag(GregTechTags.CARBON_MESH).add(FTBICItems.CARBON_FIBER_MESH.item.get());
        tag(GregTechTags.CARBON_PLATE).add(FTBICItems.CARBON_PLATE.item.get());
        tag(GregTechTags.CIRCUIT).add(FTBICItems.ELECTRONIC_CIRCUIT.item.get());
        tag(GregTechTags.COAL_CHUNK).add(FTBICItems.GRAPHENE.item.get());
        tag(GregTechTags.COMPONENT_HEAT_VENT).add(FTBICItems.COMPONENT_HEAT_VENT.get());
        tag(GregTechTags.COMPRESSED_COAL_BALL).add(FTBICItems.COMPRESSED_COAL_BALL.item.get());
        tag(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1).add(FTBICItems.MACHINE_BLOCK.get());
        tag(GregTechTags.DENSE_COPPER_PLATE).add(FTBICItems.DENSE_COPPER_PLATE.item.get());
        tag(GregTechTags.DOUBLE_INSULATED_GOLD_CABLE).add(FTBICItems.HV_CABLE.get());
        tag(GregTechTags.EMPTY_FLUID_CELL).add(FTBICItems.FLUID_CELL.get());
        tag(GregTechTags.EMPTY_FUEL_CAN);
        tag(GregTechTags.ENERGY_CRYSTAL).add(FTBICItems.MV_BATTERY.get());
        tag(GregTechTags.GENERATOR).add(FTBICElectricBlocks.BASIC_GENERATOR.item.get());
        tag(GregTechTags.HEAT_VENT).add(FTBICItems.HEAT_VENT.get());
        tag(GregTechTags.HV_TRANSFORMER).add(FTBICElectricBlocks.HV_TRANSFORMER.item.get());
//        tag(GregTechTags.ILLUMINATOR_FLAT).add(FTBICElectricBlocks.ILLUMINATOR);
        tag(GregTechTags.INSULATED_COPPER_CABLE).add(FTBICItems.LV_CABLE.get());
        tag(GregTechTags.IRIDIUM_ALLOY).add(FTBICItems.IRIDIUM_ALLOY.item.get());
        tag(GregTechTags.IRIDIUM_NEUTRON_REFLECTOR).add(FTBICItems.IRIDIUM_NEUTRON_REFLECTOR.get());
        tag(GregTechTags.LAPOTRON_CRYSTAL).add(FTBICItems.HV_BATTERY.get());
//        tag(GregTechTags.LAPPACK).add(FTBICElectricBlocks.LAPPACK);
        tag(GregTechTags.OVERCLOCKED_HEAT_VENT).add(FTBICItems.OVERCLOCKED_HEAT_VENT.get());
        tag(GregTechTags.PUMP).add(FTBICElectricBlocks.PUMP.item.get());
        tag(GregTechTags.REACTOR_COOLANT_CELL).add(FTBICItems.SMALL_COOLANT_CELL.get());
        tag(GregTechTags.REINFORCED_STONE).add(FTBICItems.REINFORCED_STONE.get());
        tag(GregTechTags.RESIN).addTag(Tags.Items.SLIMEBALLS).add(MyrtreesItems.LATEX.get());
        tag(GregTechTags.RE_BATTERY).add(FTBICItems.LV_BATTERY.get());
        tag(GregTechTags.RUBBER).add(FTBICItems.RUBBER.item.get());
        tag(GregTechTags.SEXTUPLE_REACTOR_COOLANT_CELL).add(FTBICItems.LARGE_COOLANT_CELL.get());
        tag(GregTechTags.SOLAR_GENERATOR).add(FTBICElectricBlocks.LV_SOLAR_PANEL.item.get());
//        tag(GregTechTags.SOLAR_HELMET).add(FTBICItems.SOLAR_HELMET);
        tag(GregTechTags.TELEPORTER).add(FTBICElectricBlocks.TELEPORTER.item.get());
        tag(GregTechTags.TRANSFORMER_UPGRADE).add(FTBICItems.TRANSFORMER_UPGRADE.get());
    }
}
