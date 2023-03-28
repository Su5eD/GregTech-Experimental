package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.block.BaseEntityBlock;
import dev.su5ed.gtexperimental.block.SimpleMachineBlock;
import dev.su5ed.gtexperimental.blockentity.SonictronBlockEntity;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.blockentity.SimpleMachineBlockEntity;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.BlockItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Function;

public enum GTBlockEntity implements BlockItemProvider, BlockEntityProvider {
    AUTO_MACERATOR(SimpleMachineBlock::new, SimpleMachineBlockEntity::autoMacerator, AllowedFacings.HORIZONTAL, true),
    AUTO_EXTRACTOR(SimpleMachineBlock::new, SimpleMachineBlockEntity::autoExtractor, AllowedFacings.HORIZONTAL, true),
    AUTO_COMPRESSOR(SimpleMachineBlock::new, SimpleMachineBlockEntity::autoCompressor, AllowedFacings.HORIZONTAL, true),
    SONICTRON(SonictronBlockEntity::new, AllowedFacings.NORTH, true);

    private final BlockEntityType<? extends BaseBlockEntity> type;
    private final AllowedFacings allowedFacings;
    private final boolean hasActive;
    private final Lazy<Block> block;
    private final Lazy<Item> item;
    private final Lazy<? extends BaseBlockEntity> dummyBE;

    <T extends BaseBlockEntity> GTBlockEntity(BlockEntityType.BlockEntitySupplier<T> factory, AllowedFacings allowedFacings, boolean hasActive) {
        this(BaseEntityBlock::new, factory, allowedFacings, hasActive);
    }

    <T extends BaseBlockEntity> GTBlockEntity(Function<BlockEntityProvider, ? extends BaseEntityBlock> blockFactory, BlockEntityType.BlockEntitySupplier<T> factory, AllowedFacings allowedFacings, boolean hasActive) {
        this.allowedFacings = allowedFacings;
        this.hasActive = hasActive;
        this.block = Lazy.of(() -> blockFactory.apply(this));
        this.item = Lazy.of(() -> new BlockItem(getBlock(), ModObjects.itemProperties()));
        this.type = BlockEntityType.Builder.of(factory, getBlock()).build(null);
        this.dummyBE = Lazy.of(() -> this.type.create(new BlockPos(0, 0, 0), this.block.get().defaultBlockState()));
    }

    @Override
    public String getRegistryName() {
        return getName();
    }

    @Override
    public Block getBlock() {
        return this.block.get();
    }

    @Override
    public Item getItem() {
        return this.item.get();
    }

    @Override
    public BlockEntityType<?> getType() {
        return this.type;
    }

    @Override
    public AllowedFacings getAllowedFacings() {
        return this.allowedFacings;
    }

    @Override
    public BaseBlockEntity getDummyInstance() {
        return this.dummyBE.get(); 
    }

    @Override
    public boolean hasActiveModel() {
        return this.hasActive;
    }
}
