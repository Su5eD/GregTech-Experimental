package mods.gregtechmod.common.objects.items;

import com.zuxelus.energycontrol.api.IItemKit;
import com.zuxelus.energycontrol.api.ItemStackHelper;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.objects.items.base.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSensorKit extends ItemBase implements IItemKit {
    public static final int KIT_ID = 800;

    public ItemSensorKit() {
        super("sensor_kit", "Attach to GregTech Machines");
        setMaxStackSize(1);
    }

    @Override
    public int getDamage() {
        return -1;
    }

    @Override
    public String getName() {
        return GregtechMod.MODID + ":sensor_kit";
    }

    @Override
    public String getUnlocalizedName() {
        return "item.gregtechmod.sensor_kit";
    }

    @Override
    public ItemStack getSensorCard(ItemStack stack, Item card, EntityPlayer player, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof IPanelInfoProvider))
            return ItemStack.EMPTY;

        ItemStack sensorLocationCard = new ItemStack(card, 1, ItemSensorCard.CARD_ID);
        ItemStackHelper.setCoordinates(sensorLocationCard, pos);
        return sensorLocationCard;
    }

    @Override
    public Object[] getRecipe() {
        return null;
    }
}
