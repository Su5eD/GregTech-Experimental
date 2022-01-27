package mods.gregtechmod.objects.items;

import com.zuxelus.energycontrol.api.*;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.items.base.ItemBase;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Optional.Interface(modid = "energycontrol", iface = "com.zuxelus.energycontrol.api.IItemCard")
public class ItemSensorCard extends ItemBase implements IItemCard {
    public static final int DISPLAY_MAIN = 1;
    public static final int DISPLAY_SECOND = 2;
    public static final int DISPLAY_TERTIARY = 4;
    public static final int CARD_ID = 800; //Because why would Energy Control use ResourceLocations?

    public ItemSensorCard() {
        super("sensor_card");
        setMaxStackSize(1);
    }

    @Override
    public int getDamage() {
        return CARD_ID;
    }

    @Override
    public String getName() {
        return Reference.MODID + ":sensor_card";
    }

    @Override
    public String getUnlocalizedName() {
        return GtLocale.buildKeyItem("gregtechmod.sensor_card");
    }

    @Override
    public CardState update(World world, ICardReader card, int i, BlockPos blockPos) {
        TileEntity tileEntity = world.getTileEntity(card.getTarget());
        if (tileEntity instanceof IPanelInfoProvider && ((IPanelInfoProvider)tileEntity).isGivingInformation()) {
            card.setString("mainInfo", ((IPanelInfoProvider)tileEntity).getMainInfo());
            card.setString("secondaryInfo", ((IPanelInfoProvider)tileEntity).getSecondaryInfo());
            card.setString("tertiaryInfo", ((IPanelInfoProvider)tileEntity).getTertiaryInfo());
            return CardState.OK;
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<PanelString> getStringData(int displaySettings, ICardReader card, boolean b, boolean b1) {
        List<PanelString> result = new LinkedList<>();

        if((displaySettings & DISPLAY_MAIN) != 0)  {
            PanelString line = new PanelString();
            line.textLeft = card.getString("mainInfo");
            result.add(line);
        }
        if((displaySettings & DISPLAY_SECOND) != 0) {
            PanelString line = new PanelString();
            line.textLeft = card.getString("secondaryInfo");
            result.add(line);
        }
        if((displaySettings & DISPLAY_TERTIARY) != 0) {
            PanelString line = new PanelString();
            line.textLeft = card.getString("tertiaryInfo");
            result.add(line);
        }
        return result;
    }

    @Override
    public List<PanelSetting> getSettingsList() {
        List<PanelSetting> result = new ArrayList<>(3);
        result.add(new PanelSetting("Primary", DISPLAY_MAIN, 800));
        result.add(new PanelSetting("Secondary", DISPLAY_SECOND, 800));
        result.add(new PanelSetting("Tertiary", DISPLAY_TERTIARY, 800));
        return result;
    }

    @Override
    public boolean isRemoteCard() {
        return false;
    }

    @Override
    public int getKitFromCard() {
        return ItemSensorKit.KIT_ID;
    }

    @Override
    public Object[] getRecipe() {
        return null;
    }
}
