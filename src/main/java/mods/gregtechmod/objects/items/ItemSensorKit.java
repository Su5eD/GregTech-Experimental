package mods.gregtechmod.objects.items;

import com.zuxelus.energycontrol.api.IItemKit;
import com.zuxelus.energycontrol.api.ItemStackHelper;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.items.base.ItemBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(modid = "energycontrol", iface = "com.zuxelus.energycontrol.api.IItemKit")
public class ItemSensorKit extends ItemBase implements IItemKit {

    public ItemSensorKit() {
        super("sensor_kit");
        setRegistryName("sensor_kit");
        setTranslationKey("sensor_kit");
        setMaxStackSize(1);
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public ItemStack getSensorCard(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IPanelInfoProvider) {
            ItemStack card = new ItemStack(BlockItems.Miscellaneous.SENSOR_CARD.getInstance());
            ItemStackHelper.setCoordinates(card, pos);  
            return card;
        }
        return ItemStack.EMPTY;
    }
}
