package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.item.LithiumBatteryItem;
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
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static dev.su5ed.gregtechmod.api.util.Reference.location;
import static dev.su5ed.gregtechmod.api.util.Reference.locationNullable;

class ItemModelGen extends ItemModelProvider {
    private final ResourceLocation generatedParent = mcLoc("item/generated");
    
    public ItemModelGen(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        StreamEx.<ItemProvider>of(ModBlock.values())
            .append(Ore.values())
            .append(GTBlockEntity.values())
            .map(block -> block.getItem().getRegistryName().getPath())
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
        
        String fullName = Component.LITHIUM_RE_BATTERY.getName() + "_full";
        providerModel(Component.LITHIUM_RE_BATTERY, "component")
            .override()
                .model(singleItemTexture(fullName, this.generatedParent, location("item", "component", fullName)))
                .predicate(LithiumBatteryItem.CHARGE_PROPERTY, 1)
                .end();
    }

    @NotNull
    @Override
    public String getName() {
        return Reference.NAME + " Item Models";
    }

    private void registerItems(ItemProvider[] providers, @Nullable String folder) {
        StreamEx.of(providers)
            .forEach(provider -> providerModel(provider, folder));
    }
    
    public ItemModelBuilder providerModel(ItemProvider provider, @Nullable String folder) {
        String path = provider.getItem().getRegistryName().getPath();
        ResourceLocation texture = locationNullable("item", folder, provider.getName());
        return singleItemTexture(path, this.generatedParent, texture);
    }
    
    public ItemModelBuilder singleItemTexture(String name, ResourceLocation parent, ResourceLocation texture) {
        return singleTexture(name, parent, "layer0", texture);
    }
}
