package mods.gregtechmod.objects.items;

import com.zuxelus.energycontrol.api.*;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Optional.Interface(modid = "energycontrol", iface = "com.zuxelus.energycontrol.api.IItemCard")
public class ItemSensorCard extends ItemBase implements IItemCard {
    public static final int DISPLAY_MAIN = 1;
    public static final int DISPLAY_SECOND = 2;
    public static final int DISPLAY_TERTIARY = 4;

    public ItemSensorCard() {
        super("sensor_card");
        setRegistryName("sensor_card");
        setTranslationKey("sensor_card");
        setMaxStackSize(1);
        setCreativeTab(GregTechMod.GREGTECH_TAB);
    }

    @Override
    public CardState update(World world, ICardReader card, int i, BlockPos blockPos) {
        TileEntity te = world.getTileEntity(card.getTarget());
        if (te instanceof IPanelInfoProvider && ((IPanelInfoProvider) te).isGivingInformation()) {
            card.setString("mainInfo", ((IPanelInfoProvider) te).getMainInfo());
            card.setString("secondaryInfo", ((IPanelInfoProvider) te).getSecondaryInfo());
            card.setString("tertiaryInfo", ((IPanelInfoProvider) te).getTertiaryInfo());
            return CardState.OK;
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<PanelString> getStringData(int displaySettings, ICardReader card, boolean b, boolean b1) {
        List<PanelString> result = new LinkedList<>();

        if ((displaySettings & DISPLAY_MAIN) != 0) {
            PanelString line = new PanelString();
            line.textLeft = card.getString("mainInfo");
            result.add(line);
        }
        if ((displaySettings & DISPLAY_SECOND) != 0) {
            PanelString line = new PanelString();
            line.textLeft = card.getString("secondaryInfo");
            result.add(line);
        }
        if ((displaySettings & DISPLAY_TERTIARY) != 0) {
            PanelString line = new PanelString();
            line.textLeft = card.getString("tertiaryInfo");
            result.add(line);
        }
        return result;
    }

    @Override
    public List<PanelSetting> getSettingsList(ItemStack stack) {
        return Arrays.asList(
            new PanelSetting("Primary", DISPLAY_MAIN),
            new PanelSetting("Secondary", DISPLAY_SECOND),
            new PanelSetting("Tertiary", DISPLAY_TERTIARY)
        );
    }

    @Override
    public boolean isRemoteCard(ItemStack stack) {
        return false;
    }
}
