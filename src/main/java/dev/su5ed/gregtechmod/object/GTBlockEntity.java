package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.block.BaseEntityBlock;
import dev.su5ed.gregtechmod.blockentity.AutomaticMaceratorBlockEntity;
import dev.su5ed.gregtechmod.blockentity.SonictronBlockEntity;
import dev.su5ed.gregtechmod.blockentity.base.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

public enum GTBlockEntity implements BlockItemProvider, BlockEntityProvider {
    AUTO_MACERATOR(AutomaticMaceratorBlockEntity::new, AllowedFacings.HORIZONTAL, true),
    SONICTRON(SonictronBlockEntity::new, AllowedFacings.NORTH, true);

    private final BlockEntityType<? extends BaseBlockEntity> type;
    private final AllowedFacings allowedFacings;
    private final boolean hasActive;
    private final Lazy<Block> block;
    private final Lazy<Item> item;
    private final Lazy<? extends BaseBlockEntity> dummyBE;

    <T extends BaseBlockEntity> GTBlockEntity(BlockEntityType.BlockEntitySupplier<T> factory, AllowedFacings allowedFacings, boolean hasActive) {
        this.allowedFacings = allowedFacings;
        this.hasActive = hasActive;
        this.block = Lazy.of(() -> new BaseEntityBlock(this));
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
