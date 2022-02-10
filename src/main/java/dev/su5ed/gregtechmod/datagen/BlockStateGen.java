package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.ClientSetup;
import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.object.ModBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Arrays;

import static dev.su5ed.gregtechmod.api.util.Reference.location;

class BlockStateGen extends BlockStateProvider {

    public BlockStateGen(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Reference.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        Arrays.stream(ModBlock.values())
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
    }
}
