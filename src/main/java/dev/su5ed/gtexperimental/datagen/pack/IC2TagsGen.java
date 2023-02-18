package dev.su5ed.gtexperimental.datagen.pack;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import ic2.core.ref.Ic2Items;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class IC2TagsGen extends ItemTagsProvider {

    public IC2TagsGen(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(GregTechTags.SMALL_EU_STORE).add(Ic2Items.ENERGY_CRYSTAL);
        tag(GregTechTags.MEDIUM_EU_STORE).add(Ic2Items.LAPOTRON_CRYSTAL);

        tag(GregTechTags.ADVANCED_CIRCUIT).add(Ic2Items.ADVANCED_CIRCUIT);
        tag(GregTechTags.ADVANCED_HEAT_VENT).add(Ic2Items.ADVANCED_HEAT_VENT);
        tag(GregTechTags.CARBON_MESH).add(Ic2Items.CARBON_MESH);
        tag(GregTechTags.CARBON_PLATE).add(Ic2Items.CARBON_PLATE);
        tag(GregTechTags.CIRCUIT).add(Ic2Items.CIRCUIT);
        tag(GregTechTags.COAL_CHUNK).add(Ic2Items.COAL_CHUNK);
        tag(GregTechTags.COMPONENT_HEAT_VENT).add(Ic2Items.COMPONENT_HEAT_VENT);
        tag(GregTechTags.COMPRESSED_COAL_BALL).add(Ic2Items.COAL_BLOCK);
        tag(GregTechTags.CRAFTING_RAW_MACHINE_TIER_1).add(Ic2Items.MACHINE);
        tag(GregTechTags.DENSE_COPPER_PLATE).add(Ic2Items.DENSE_COPPER_PLATE);
        tag(GregTechTags.DOUBLE_INSULATED_GOLD_CABLE).add(Ic2Items.DOUBLE_INSULATED_GOLD_CABLE);
        tag(GregTechTags.EMPTY_FLUID_CELL).add(Ic2Items.EMPTY_CELL);
        tag(GregTechTags.EMPTY_FUEL_CAN).add(Ic2Items.EMPTY_FUEL_CAN);
        tag(GregTechTags.ENERGY_CRYSTAL).add(Ic2Items.ENERGY_CRYSTAL);
        tag(GregTechTags.GENERATOR).add(Ic2Items.GENERATOR);
        tag(GregTechTags.HEAT_VENT).add(Ic2Items.HEAT_VENT);
        tag(GregTechTags.HV_TRANSFORMER).add(Ic2Items.HV_TRANSFORMER);
//        tag(GregTechTags.ILLUMINATOR_FLAT).add(Ic2Items.ILLUMINATOR);
        tag(GregTechTags.INSULATED_COPPER_CABLE).add(Ic2Items.INSULATED_COPPER_CABLE);
        tag(GregTechTags.IRIDIUM_ALLOY).add(Ic2Items.IRIDIUM);
        tag(GregTechTags.IRIDIUM_NEUTRON_REFLECTOR).add(Ic2Items.IRIDIUM_NEUTRON_REFLECTOR);
        tag(GregTechTags.LAPOTRON_CRYSTAL).add(Ic2Items.LAPOTRON_CRYSTAL);
//        tag(GregTechTags.LAPPACK).add(Ic2Items.LAPPACK);
        tag(GregTechTags.OVERCLOCKED_HEAT_VENT).add(Ic2Items.OVERCLOCKED_HEAT_VENT);
        tag(GregTechTags.PUMP).add(Ic2Items.PUMP);
        tag(GregTechTags.REACTOR_COOLANT_CELL).add(Ic2Items.REACTOR_COOLANT_CELL);
        tag(GregTechTags.REINFORCED_STONE).add(Ic2Items.REINFORCED_STONE);
        tag(GregTechTags.RESIN).add(Ic2Items.RESIN);
        tag(GregTechTags.RE_BATTERY).add(Ic2Items.RE_BATTERY);
        tag(GregTechTags.ingot("refined_iron")).add(Ic2Items.REFINED_IRON_INGOT);
        tag(GregTechTags.RUBBER).add(Ic2Items.RUBBER);
//        tag(GregTechTags.SAW).add(Ic2Items.CHAINSAW);
        tag(GregTechTags.SEXTUPLE_REACTOR_COOLANT_CELL).add(Ic2Items.SEXTUPLE_REACTOR_COOLANT_CELL);
        tag(GregTechTags.SOLAR_GENERATOR).add(Ic2Items.SOLAR_GENERATOR);
//        tag(GregTechTags.SOLAR_HELMET).add(Ic2Items.SOLAR_HELMET);
        tag(GregTechTags.TELEPORTER).add(Ic2Items.TELEPORTER);
        tag(GregTechTags.TRANSFORMER_UPGRADE).add(Ic2Items.TRANSFORMER_UPGRADE);
        tag(GregTechTags.WRENCH).add(Ic2Items.WRENCH, Ic2Items.ELECTRIC_WRENCH);
    }

    public static class Classic extends IC2TagsGen {

        public Classic(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(generator, blockTagsProvider, existingFileHelper);
        }

        @Override
        protected void addTags() {
            super.addTags();

            tag(GregTechTags.ANY_GEMS_DIAMOND)
                .add(Ic2Items.INDUTRIAL_DIAMOND);
            // TODO Fix tag replace serialization in forge
//            tag(GregTechTags.UNIVERSAL_IRON_INGOT)
//                .addTag(GregTechTags.ingot("refined_iron"))
//                .replace();
//            tag(GregTechTags.UNIVERSAL_IRON_PLATE)
//                .addTag(Plate.REFINED_IRON.getTag())
//                .replace();
//            tag(GregTechTags.UNIVERSAL_IRON_ROD)
//                .addTag(Rod.REFINED_IRON.getTag())
//                .replace();
        }
    }
}
