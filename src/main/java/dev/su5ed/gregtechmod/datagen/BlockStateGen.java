package dev.su5ed.gregtechmod.datagen;

import dev.su5ed.gregtechmod.api.Reference;
import dev.su5ed.gregtechmod.block.BaseEntityBlock;
import dev.su5ed.gregtechmod.block.ConnectedBlock;
import dev.su5ed.gregtechmod.model.ConnectedModelBuilder;
import dev.su5ed.gregtechmod.object.GTBlockEntity;
import dev.su5ed.gregtechmod.object.ModBlock;
import dev.su5ed.gregtechmod.object.ModObjects;
import dev.su5ed.gregtechmod.object.Ore;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.StreamEx;

import static dev.su5ed.gregtechmod.api.Reference.location;

class BlockStateGen extends BlockStateProvider {

    public BlockStateGen(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Reference.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        StreamEx.of(ModBlock.values())
            .forEach(modBlock -> {
                Block block = modBlock.getBlock();
                String path = ForgeRegistries.BLOCKS.getKey(block).getPath();

                if (block instanceof ConnectedBlock) {
                    bakedModel(block, modBlock.getName(), path);
                }
                else {
                    simpleModel(block, path, modBlock.getName());
                }
            });

        simpleBlock(ModObjects.LIGHT_SOURCE_BLOCK.get(), models().getExistingFile(new ResourceLocation("block/air")));

        StreamEx.<BlockItemProvider>of(Ore.values())
            .map(BlockItemProvider::getBlock)
            .mapToEntry(block -> models().getExistingFile(ForgeRegistries.BLOCKS.getKey(block)))
            .forKeyValue(this::simpleBlock);

        StreamEx.of(GTBlockEntity.values())
            .forEach(this::blockEntity);
    }

    private void blockEntity(GTBlockEntity provider) {
        Block block = provider.getBlock();
        ResourceLocation name = ForgeRegistries.BLOCKS.getKey(block);
        ModelFile model = models().getExistingFile(name);
        VariantBlockStateBuilder builder = getVariantBuilder(block);
        if (provider.hasActiveModel()) {
            ModelFile active = models().getExistingFile(new ResourceLocation(name.toString() + "_active"));
            builder
                .partialState()
                    .with(BaseEntityBlock.ACTIVE, false)
                    .setModels(new ConfiguredModel(model))
                .partialState()
                    .with(BaseEntityBlock.ACTIVE, true)
                    .setModels(new ConfiguredModel(active));
        }
        else {
            builder.partialState().setModels(new ConfiguredModel(model));
        }
    }

    private void simpleModel(Block block, String path, String name) {
        ResourceLocation location = location(ModelProvider.BLOCK_FOLDER, name);
        simpleBlock(block, models().cubeAll(path, location));
    }

    private void bakedModel(Block block, String name, String path) {
        simpleBlock(block, models().getBuilder(path)
            .parent(models().getExistingFile(mcLoc("cube")))
            .customLoader(ConnectedModelBuilder::new)
            .setTextureRoot(name)
            .end());
    }
}
