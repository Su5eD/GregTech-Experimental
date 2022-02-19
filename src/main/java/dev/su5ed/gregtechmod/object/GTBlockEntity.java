package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.block.BaseEntityBlock;
import dev.su5ed.gregtechmod.blockentity.AutomaticMaceratorBlockEntity;
import dev.su5ed.gregtechmod.blockentity.BaseBlockEntity;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.BlockItemProvider;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

public enum GTBlockEntity implements BlockItemProvider, BlockEntityProvider {
    AUTO_MACERATOR(AutomaticMaceratorBlockEntity::new, AllowedFacings.HORIZONTAL);

    private final BlockEntityType<?> type;
    private final AllowedFacings allowedFacings;
    private final Lazy<Block> block;
    private final Lazy<Item> item;

    GTBlockEntity(BlockEntityType.BlockEntitySupplier<? extends BaseBlockEntity> factory, AllowedFacings allowedFacings) {
        String name = getName();
        this.allowedFacings = allowedFacings;
        this.block = Lazy.of(() -> new BaseEntityBlock(this).setRegistryName(name));
        this.item = Lazy.of(() -> new BlockItem(getBlock(), ModObjects.DEFAULT_ITEM_PROPERTIES).setRegistryName(name));
        this.type = BlockEntityType.Builder.of(factory, getBlock()).build(null).setRegistryName(name);
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
}
