package dev.su5ed.gregtechmod.blockentity.base;

import dev.su5ed.gregtechmod.api.util.FriendlyCompoundTag;
import dev.su5ed.gregtechmod.blockentity.component.BlockEntityComponent;
import dev.su5ed.gregtechmod.network.GregTechNetwork;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import one.util.streamex.StreamEx;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class BaseBlockEntity extends BlockEntity {
    protected final BlockEntityProvider.AllowedFacings allowedFacings; 
    private final Map<ResourceLocation, BlockEntityComponent> components = new HashMap<>();

    protected BaseBlockEntity(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider.getType(), pos, state);
        
        this.allowedFacings = provider.getAllowedFacings();
    }

    public void tickClient() {
    }

    public void tickServer() {
    }
    
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    public Optional<ItemStack> getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return Optional.empty();
    }
    
    public Direction getFacing() {
        return getBlockState().getValue(BlockStateProperties.FACING);
    }
    
    public boolean setFacing(Direction side) {
        if (this.allowedFacings.allows(side)) {
            BlockState state = getBlockState();
            this.level.setBlock(this.worldPosition, state.setValue(BlockStateProperties.FACING, side), Block.UPDATE_ALL);
            return true;
        }
        return false;
    }
    
    public boolean wrenchCanRemove() {
        return true;
    }

    public void updateClientField(String name) {
        GregTechNetwork.updateClientField(this, name);
    }

    protected <T extends BlockEntityComponent> T addComponent(T component) {
        ResourceLocation name = component.getName();
        if (this.components.containsKey(name)) throw new RuntimeException("Duplicate component: " + name);
        else this.components.put(name, component);
        return component;
    }

    public Optional<BlockEntityComponent> getComponent(ResourceLocation name) {
        return Optional.ofNullable(this.components.get(name));
    }

    public void updateRenderClient() {
        GtUtil.assertClientSide(this.level);

        requestModelDataUpdate();
        BlockState state = getBlockState();
        this.level.sendBlockUpdated(this.worldPosition, state, state, Block.UPDATE_IMMEDIATE);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level.isClientSide) {
            GregTechNetwork.requestInitialData(this);
        }
    }

    @Override
    protected final void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        
        FriendlyCompoundTag friendlyTag = new FriendlyCompoundTag();
        saveAdditional(friendlyTag);
        tag.put("fields", friendlyTag);
        tag.put("components", saveComponents());
    }

    @Override
    public final void load(CompoundTag tag) {
        super.load(tag);
        
        FriendlyCompoundTag fields = new FriendlyCompoundTag(tag.getCompound("fields"));
        FriendlyCompoundTag components = new FriendlyCompoundTag(tag.getCompound("components"));
        load(fields);
        loadComponents(components);
    }

    protected void saveAdditional(FriendlyCompoundTag tag) {}
    
    protected void load(FriendlyCompoundTag tag) {}

    private CompoundTag saveComponents() {
        CompoundTag tag = new CompoundTag();
        this.components.forEach((name, component) -> {
            FriendlyCompoundTag componentTag = component.save();
            tag.put(name.toString(), componentTag);
        });
        return tag;
    }

    private void loadComponents(FriendlyCompoundTag tag) {
        StreamEx.of(tag.getAllKeys())
            .mapToEntry(name -> this.components.get(new ResourceLocation(name)), tag::getCompound)
            .nonNullKeys()
            .mapValues(FriendlyCompoundTag::new)
            .forKeyValue(BlockEntityComponent::load);
    }
}
