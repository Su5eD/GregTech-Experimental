package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.setup.ClientSetup;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.Ore;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import one.util.streamex.StreamEx;

import javax.annotation.Nonnull;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

class BlockStateGen extends BlockStateProvider {

    public BlockStateGen(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Reference.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        StreamEx.of(ModBlock.values())
            .forEach(modBlock -> {
                Block block = modBlock.getBlock();
                String path = block.getRegistryName().getPath();
                String texturePath = modBlock.getName();

                ModelFile model;
                if (block instanceof ConnectedBlock) {
                    model = models().getBuilder(path)
                        .parent(models().getExistingFile(mcLoc("cube")))
                        .customLoader((blockModelBuilder, helper) -> {
                            ResourceLocation location = ClientSetup.getLoaderLocation(texturePath);
                            return new CustomLoaderBuilder<BlockModelBuilder>(location, blockModelBuilder, helper) {};
                        })
                        .end();
                }
                else {
                    ResourceLocation location = location(ModelProvider.BLOCK_FOLDER + "/" + texturePath);
                    model = models().cubeAll(path, location);
                }

                simpleBlock(block, model);
            });
        
        StreamEx.of(Ore.values())
            .forEach(ore -> {
                Block block = ore.getBlock();
                String path = block.getRegistryName().getPath();
                String texturePath = ore.getName();
                ResourceLocation location = location(ModelProvider.BLOCK_FOLDER + "/ore/" + texturePath);
                simpleBlock(block, models().cubeAll(path, location));
            });
    }

    @Nonnull
    @Override
    public String getName() {
        return Reference.NAME + " Blockstates";
    }
}
