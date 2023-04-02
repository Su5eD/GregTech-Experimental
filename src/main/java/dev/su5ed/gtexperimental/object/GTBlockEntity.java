package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.block.BaseEntityBlock;
import dev.su5ed.gtexperimental.block.SimpleMachineBlock;
import dev.su5ed.gtexperimental.blockentity.AutomaticRecyclerBlockEntity;
import dev.su5ed.gtexperimental.blockentity.SimpleMachineBlockEntity;
import dev.su5ed.gtexperimental.blockentity.SonictronBlockEntity;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.util.BlockEntityProvider;
import dev.su5ed.gtexperimental.util.BlockItemProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Function;

import static dev.su5ed.gtexperimental.object.GTBlockEntity.ActiveType.ACTIVE;
import static dev.su5ed.gtexperimental.object.GTBlockEntity.ActiveType.ACTIVE_GUI;

public enum GTBlockEntity implements BlockItemProvider, BlockEntityProvider {
    AUTO_MACERATOR(SimpleMachineBlock::new, SimpleMachineBlockEntity::autoMacerator, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    AUTO_EXTRACTOR(SimpleMachineBlock::new, SimpleMachineBlockEntity::autoExtractor, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    AUTO_COMPRESSOR(SimpleMachineBlock::new, SimpleMachineBlockEntity::autoCompressor, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    AUTO_RECYCLER(SimpleMachineBlock::new, AutomaticRecyclerBlockEntity::new, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    AUTO_ELECTRIC_FURNACE(SimpleMachineBlock::new, SimpleMachineBlockEntity::autoElectricFurnace, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    WIREMILL(SimpleMachineBlock::new, SimpleMachineBlockEntity::wiremill, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    BENDER(SimpleMachineBlock::new, SimpleMachineBlockEntity::bender, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    ALLOY_SMELTER(SimpleMachineBlock::new, SimpleMachineBlockEntity::alloySmelter, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    ASSEMBLER(SimpleMachineBlock::new, SimpleMachineBlockEntity::assembler, AllowedFacings.HORIZONTAL, ACTIVE_GUI),
    SONICTRON(SonictronBlockEntity::new, AllowedFacings.NORTH, ACTIVE);

    private final BlockEntityType<? extends BaseBlockEntity> type;
    private final AllowedFacings allowedFacings;
    private final ActiveType activeType;
    private final Lazy<Block> block;
    private final Lazy<Item> item;
    private final Lazy<? extends BaseBlockEntity> dummyBE;

    <T extends BaseBlockEntity> GTBlockEntity(BlockEntityType.BlockEntitySupplier<T> factory, AllowedFacings allowedFacings, ActiveType activeType) {
        this(BaseEntityBlock::new, factory, allowedFacings, activeType);
    }

    <T extends BaseBlockEntity> GTBlockEntity(Function<BlockEntityProvider, ? extends BaseEntityBlock> blockFactory, BlockEntityType.BlockEntitySupplier<T> factory, AllowedFacings allowedFacings, ActiveType activeType) {
        this.allowedFacings = allowedFacings;
        this.activeType = activeType;
        this.block = Lazy.of(() -> blockFactory.apply(this));
        this.item = Lazy.of(() -> new BlockItem(getBlock(), ModObjects.itemProperties()));
        this.type = BlockEntityType.Builder.of(factory, getBlock()).build(null);
        this.dummyBE = Lazy.of(() -> this.type.create(new BlockPos(0, 0, 0), this.block.get().defaultBlockState()));
    }

    public boolean hasActiveItemModel() {
        return this.activeType.gui;
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
        return this.activeType.active;
    }

    public enum ActiveType {
        ACTIVE(true, false),
        ACTIVE_GUI(true, true),
        NO_ACTIVE(false, false);

        private final boolean active;
        private final boolean gui;

        ActiveType(boolean active, boolean gui) {
            this.active = active;
            this.gui = gui;
        }
    }
}
