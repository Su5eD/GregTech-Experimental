package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.object.Ingot;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.Nugget;
import dev.su5ed.gregtechmod.util.ItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;

class ItemModelGen extends ItemModelProvider {
    private final ResourceLocation generatedParent = mcLoc("item/generated");
    
    public ItemModelGen(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        StreamEx.of(ModBlock.values())
            .map(block -> block.getItem().getRegistryName().getPath())
            .mapToEntry(name -> modLoc("block/" + name))
            .forKeyValue(this::withExistingParent);
        
        registerItems(Ingot.values(), "ingot");
        registerItems(Nugget.values(), "nugget");
    }

    private void registerItems(ItemProvider[] providers, String folder) {
        StreamEx.of(providers)
            .forEach(provider -> {
                String path = provider.getItem().getRegistryName().getPath();
                ResourceLocation texture = modLoc("item/" + folder + "/" + provider.getName());
                singleTexture(path, this.generatedParent, "layer0", texture);
            });
    }
}
