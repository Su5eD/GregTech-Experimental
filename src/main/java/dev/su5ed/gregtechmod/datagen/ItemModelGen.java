package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.item.LithiumBatteryItem;
import dev.su5ed.gregtechmod.object.Armor;
import dev.su5ed.gregtechmod.object.Cell;
import dev.su5ed.gregtechmod.object.ColorSpray;
import dev.su5ed.gregtechmod.object.Component;
import dev.su5ed.gregtechmod.object.Dust;
import dev.su5ed.gregtechmod.object.File;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.Hammer;
import dev.su5ed.gregtechmod.object.Ingot;
import dev.su5ed.gregtechmod.object.JackHammer;
import dev.su5ed.gregtechmod.object.Miscellaneous;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.ModCoverItem;
import dev.su5ed.gregtechmod.object.ModFluid;
import dev.su5ed.gregtechmod.object.NuclearCoolantPack;
import dev.su5ed.gregtechmod.object.NuclearFuelRod;
import dev.su5ed.gregtechmod.object.Nugget;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.object.Plate;
import dev.su5ed.gregtechmod.object.Rod;
import dev.su5ed.gregtechmod.object.Saw;
import dev.su5ed.gregtechmod.object.Smalldust;
import dev.su5ed.gregtechmod.object.Tool;
import dev.su5ed.gregtechmod.object.TurbineRotor;
import dev.su5ed.gregtechmod.object.Upgrade;
import dev.su5ed.gregtechmod.object.Wrench;
import dev.su5ed.gregtechmod.util.FluidProvider;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import static dev.su5ed.gregtechmod.api.Reference.location;
import static dev.su5ed.gregtechmod.api.Reference.locationNullable;

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
            .map(block -> ForgeRegistries.ITEMS.getKey(block.getItem()).getPath())
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
        registerItems(Cell.values(), "cell");
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
        String path = ForgeRegistries.ITEMS.getKey(provider.getItem()).getPath();
        ResourceLocation texture = locationNullable("item", folder, provider.getName());
        return singleItemTexture(path, this.generatedParent, texture);
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
