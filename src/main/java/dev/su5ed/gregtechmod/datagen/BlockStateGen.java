package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.util.Reference;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.setup.ClientSetup;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelProvider;
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

                if (block instanceof ConnectedBlock) bakedModel(block, path);
                else simpleModel(block, path, modBlock.getName());
            });

        StreamEx.<BlockItemProvider>of(Ore.values())
            .append(GTBlockEntity.values())
            .map(BlockItemProvider::getBlock)
            .mapToEntry(block -> models().getExistingFile(block.getRegistryName()))
            .forKeyValue(this::simpleBlock);
    }

    private void simpleModel(Block block, String path, String name) {
        ResourceLocation location = location(ModelProvider.BLOCK_FOLDER, name);
        simpleBlock(block, models().cubeAll(path, location));
    }

    private void bakedModel(Block block, String path) {
        simpleBlock(block, models().getBuilder(path)
            .parent(models().getExistingFile(mcLoc("cube")))
            .customLoader((blockModelBuilder, helper) -> {
                ResourceLocation location = ClientSetup.getLoaderLocation(path);
                return new CustomLoaderBuilder<BlockModelBuilder>(location, blockModelBuilder, helper) {};
            })
            .end());
    }

    @Nonnull
    @Override
    public String getName() {
        return Reference.NAME + " Blockstates";
    }
}
