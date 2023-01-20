package dev.su5ed.gtexperimental.datagen;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.Reference;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.ModFluid;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class FluidTagsGen extends FluidTagsProvider {
    
    public FluidTagsGen(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, Reference.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(GregTechTags.FORGE_STEAM)
            .add(ModFluid.STEAM.getSourceFluid(), ModFluid.STEAM.getFlowingFluid());
        
        tag(GregTechTags.STEAM)
            .addTag(GregTechTags.FORGE_STEAM)
            .addOptional(new ResourceLocation(ModHandler.IC2_MODID, "steam"))
            .addOptional(new ResourceLocation(ModHandler.IC2_MODID, "superheated_steam"));
    }
}
