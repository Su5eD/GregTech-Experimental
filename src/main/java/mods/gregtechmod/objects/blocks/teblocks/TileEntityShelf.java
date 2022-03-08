package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.block.invslot.InvSlot;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import ic2.core.block.state.UnlistedEnumProperty;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;

public abstract class TileEntityShelf extends TileEntityCoverBehavior {
    public static final IUnlistedProperty<Type> SHELF_TYPE_PROPERTY = new UnlistedEnumProperty<>("shelfType", Type.class);
    
    @NBTPersistent
    private Type type;
    private final InvSlot content = new InvSlot(this, "content", InvSlot.Access.NONE, 1);

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.world.isRemote) {
            ItemStack stack = player.inventory.getCurrentItem();
            if (stack.isEmpty()) {
                ItemStack content = this.content.get();
                if (!content.isEmpty()) {
                    processType(null, player, content, stack);
                }
            }
            else if (this.content.isEmpty()) {
                Type type = Type.fromStack(stack);
                if (accepts(type, stack)) {
                    processType(type, player, ItemStack.EMPTY, stack);
                }
            }
        }
        return true;
    }

    @Override
    protected void onClicked(EntityPlayer player) {
        super.onClicked(player);

        ItemStack stack = this.content.get();
        if (!stack.isEmpty()) {
            ItemStack output = player.isSneaking() ? stack.copy() : ItemHandlerHelper.copyStackWithSize(stack, 1);
            stack.shrink(output.getCount());

            GtUtil.spawnItemInWorld(this.world, this.pos, getFacing(), output);
            if (stack.isEmpty()) {
                setType(null);
            }
        }
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        return super.getExtendedState(state)
            .withProperty(SHELF_TYPE_PROPERTY, this.type);
    }

    @Override
    protected String getDescriptionKey() {
        return GtLocale.buildKey("teblock", "decorative_storage", "description");
    }

    private void processType(Type type, EntityPlayer player, ItemStack give, ItemStack store) {
        player.inventory.mainInventory.set(player.inventory.currentItem, give);
        this.content.put(store);
        setType(type);
    }

    private void setType(Type type) {
        this.type = type;
        updateClientField("type");
    }

    @Override
    public boolean placeCoverAtSide(ICover cover, EntityPlayer player, EnumFacing side, boolean simulate) {
        return side != getFacing() && super.placeCoverAtSide(cover, player, side, simulate);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("type");
    }

    @Override
    public void onNetworkUpdate(String field) {
        super.onNetworkUpdate(field);
        if (field.equals("type")) rerender();
    }
    
    public boolean accepts(@Nullable Type type, ItemStack stack) {
        return type != null && type.predicate.test(stack);
    }

    public enum Type {
        PAPER(stack -> OreDictUnificator.isItemInstanceOf(stack, "paper", true)),
        BOOK(stack -> OreDictUnificator.isItemInstanceOf(stack, "book", true)),
        TIN_CAN(stack -> GtUtil.stackItemEquals(ModHandler.tinCan, stack) || GtUtil.stackItemEquals(ModHandler.filledTinCan, stack));

        public static final Type[] VALUES = values();

        private final Predicate<ItemStack> predicate;

        Type(Predicate<ItemStack> predicate) {
            this.predicate = predicate;
        }

        @Nullable
        public static Type fromStack(ItemStack stack) {
            return StreamEx.of(VALUES)
                .findFirst(type -> type.predicate.test(stack))
                .orElse(null);
        }
    }
}
