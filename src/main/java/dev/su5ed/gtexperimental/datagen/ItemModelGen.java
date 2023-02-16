package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.item.LithiumBatteryItem;
import dev.su5ed.gtexperimental.object.Armor;
import dev.su5ed.gtexperimental.object.ColorSpray;
import dev.su5ed.gtexperimental.object.Component;
import dev.su5ed.gtexperimental.object.Dust;
import dev.su5ed.gtexperimental.object.File;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.object.Hammer;
import dev.su5ed.gtexperimental.object.Ingot;
import dev.su5ed.gtexperimental.object.JackHammer;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.object.ModBlock;
import dev.su5ed.gtexperimental.object.ModCoverItem;
import dev.su5ed.gtexperimental.object.ModFluid;
import dev.su5ed.gtexperimental.object.NuclearCoolantPack;
import dev.su5ed.gtexperimental.object.NuclearFuelRod;
import dev.su5ed.gtexperimental.object.Nugget;
import dev.su5ed.gtexperimental.object.Ore;
import dev.su5ed.gtexperimental.object.Plate;
import dev.su5ed.gtexperimental.object.Rod;
import dev.su5ed.gtexperimental.object.Saw;
import dev.su5ed.gtexperimental.object.Smalldust;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.object.TurbineRotor;
import dev.su5ed.gtexperimental.object.Upgrade;
import dev.su5ed.gtexperimental.object.Wrench;
import dev.su5ed.gtexperimental.util.FluidProvider;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.ItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import static dev.su5ed.gtexperimental.api.Reference.location;
import static dev.su5ed.gtexperimental.api.Reference.locationNullable;

class ItemModelGen extends ItemModelProvider {
    private static final ResourceLocation FORGE_BUCKET = new ResourceLocation("forge", "item/bucket");
    
    private final ResourceLocation generatedParent = mcLoc("item/generated");
    
    public ItemModelGen(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        StreamEx.<ItemProvider>of(ModBlock.values())
            .append(Ore.values())
            .append(GTBlockEntity.values())
            .map(GtUtil::itemName)
            .mapToEntry(name -> location("block", name))
            .forKeyValue(this::withExistingParent);
        
        registerItems(Ingot.values(), "ingot");
        registerItems(Nugget.values(), "nugget");
        registerItems(Rod.values(), "rod");
        registerItems(Dust.values(), "dust");
        registerItems(Smalldust.values(), "smalldust");
        registerItems(Plate.values(), "plate");
        registerItems(TurbineRotor.values(), "turbine_rotor");
        registerItems(Component.values(), "component");
        registerItems(ModCoverItem.values(), "cover");
        registerItems(Tool.values(), "tool");
        registerItems(Upgrade.values(), "upgrade");
        registerItems(Miscellaneous.values(), null);
        registerItems(ColorSpray.values(), "color_spray");
        registerItems(Wrench.values(), "wrench");
        registerItems(JackHammer.values(), "jack_hammer");
        registerItems(Hammer.values(), "hammer");
        registerItems(Saw.values(), "saw");
        registerItems(File.values(), "file");
        registerItems(NuclearCoolantPack.values(), "coolant");
        registerItems(NuclearFuelRod.values(), "fuel_rod");
        registerItems(Armor.values(), "armor");
        
        String fullName = Component.LITHIUM_RE_BATTERY.getName() + "_full";
        providerModel(Component.LITHIUM_RE_BATTERY, "component")
            .override()
                .model(singleItemTexture(fullName, this.generatedParent, location("item", "component", fullName)))
                .predicate(LithiumBatteryItem.CHARGE_PROPERTY, 1)
                .end();
        
        StreamEx.of(ModFluid.values())
            .forEach(this::registerBucket);
    }

    private void registerItems(ItemProvider[] providers, @Nullable String folder) {
        StreamEx.of(providers)
            .forEach(provider -> providerModel(provider, folder));
    }
    
    public ItemModelBuilder providerModel(ItemProvider provider, @Nullable String folder) {
        ResourceLocation texture = locationNullable("item", folder, provider.getName());
        return singleItemTexture(GtUtil.itemName(provider), this.generatedParent, texture);
    }
    
    public ItemModelBuilder singleItemTexture(String name, ResourceLocation parent, ResourceLocation texture) {
        return singleTexture(name, parent, "layer0", texture);
    }
    
    public <T extends FluidProvider & ItemProvider> void registerBucket(T provider) {
        withExistingParent(provider.getRegistryName(), FORGE_BUCKET)
            .customLoader(DynamicFluidContainerModelBuilder::begin)
            .flipGas(true)
            .fluid(provider.getSourceFluid());
    }
}
