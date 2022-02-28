package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.block.invslot.InvSlot;
import ic2.core.block.state.Ic2BlockState.Ic2BlockStateInstance;
import mods.gregtechmod.api.cover.ICover;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityCoverBehavior;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.JavaUtil;
import mods.gregtechmod.util.OreDictUnificator;
import mods.gregtechmod.util.PropertyHelper;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

public class TileEntityShelf extends TileEntityCoverBehavior {
    @NBTPersistent
    private Type type = Type.EMPTY;
    private final InvSlot content = new InvSlot(this, "content", InvSlot.Access.NONE, 1);

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!player.world.isRemote) {
            ItemStack stack = player.inventory.getCurrentItem();
            if (stack.isEmpty()) {
                ItemStack content = this.content.get();
                if (!content.isEmpty()) {
                    processType(Type.EMPTY, player, content, stack);
                }
            }
            else if (this.content.isEmpty()) {
                Type type = Type.fromStack(stack);
                if (type != null) {
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
                setType(Type.EMPTY);
            }
        }
    }

    @Override
    protected Ic2BlockStateInstance getExtendedState(Ic2BlockStateInstance state) {
        Ic2BlockStateInstance ret = super.getExtendedState(state);
        if (this.type != Type.EMPTY) {
            PropertyHelper.TextureOverride override = new PropertyHelper.TextureOverride(EnumFacing.NORTH, this.type.texture);
            return ret.withProperty(PropertyHelper.TEXTURE_OVERRIDE_PROPERTY, override);
        }
        return ret;
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

    public enum Type {
        EMPTY(JavaUtil.alwaysFalse(), null),
        PAPER(stack -> OreDictUnificator.isItemInstanceOf(stack, "paper", true)),
        BOOK(stack -> OreDictUnificator.isItemInstanceOf(stack, "book", true), new ResourceLocation("blocks/bookshelf")),
        TIN_CAN(stack -> GtUtil.stackItemEquals(ModHandler.tinCan, stack) || GtUtil.stackItemEquals(ModHandler.filledTinCan, stack));

        public static final Type[] VALUES = values();

        private final Predicate<ItemStack> predicate;
        @Nullable
        public final ResourceLocation texture;

        Type(Predicate<ItemStack> predicate) {
            this.predicate = predicate;
            this.texture = new ResourceLocation(Reference.MODID, "blocks/machines/wood_shelf/wood_shelf_" + name().toLowerCase(Locale.ROOT));
        }

        Type(Predicate<ItemStack> predicate, ResourceLocation texture) {
            this.predicate = predicate;
            this.texture = texture;
        }

        @Nullable
        public static Type fromStack(ItemStack stack) {
            return StreamEx.of(VALUES)
                .findFirst(type -> type.predicate.test(stack))
                .orElse(null);
        }
    }
}
