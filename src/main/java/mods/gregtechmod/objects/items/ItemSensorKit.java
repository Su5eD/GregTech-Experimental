package mods.gregtechmod.objects.items;

import com.zuxelus.energycontrol.api.IItemKit;
import com.zuxelus.energycontrol.api.ItemStackHelper;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = "energycontrol", iface = "com.zuxelus.energycontrol.api.IItemKit")
public class ItemSensorKit extends ItemBase implements IItemKit {
    public static final int KIT_ID = 800;

    public ItemSensorKit() {
        super("sensor_kit");
        setMaxStackSize(1);
    }

    @Override
    public int getDamage() {
        return KIT_ID;
    }

    @Override
    public String getName() {
        return Reference.MODID + ":sensor_kit";
    }

    @Override
    public String getUnlocalizedName() {
        return GtLocale.buildKeyItem("gregtechmod.sensor_kit");
    }

    @Override
    public ItemStack getSensorCard(ItemStack itemStack, Item card, EntityPlayer entityPlayer, World world, BlockPos pos, EnumFacing enumFacing) {
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof IPanelInfoProvider)) return ItemStack.EMPTY;

        ItemStack sensorLocationCard = new ItemStack(card, 1, ItemSensorCard.CARD_ID);
        ItemStackHelper.setCoordinates(sensorLocationCard, pos);
        return sensorLocationCard;
    }

    @Override
    public Object[] getRecipe() {
        return null;
    }
}
