package mods.gregtechmod.objects.items;

import com.zuxelus.energycontrol.api.*;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.objects.items.base.ItemBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ItemSensorCard extends ItemBase implements IItemCard {
    public static final int DISPLAY_MAIN = 1;
    public static final int DISPLAY_SECOND = 2;
    public static final int DISPLAY_TERTIARY = 4;
    public static final int CARD_ID = 800; //Because why would Energy Control use ReourceLocations?

    public ItemSensorCard() {
        super("sensor_card", "Insert into a display panel");
        setMaxStackSize(1);
    }

    @Override
    public int getDamage() {
        return CARD_ID;
    }

    @Override
    public String getName() {
        return GregtechMod.MODID + ":sensor_card";
    }

    @Override
    public String getUnlocalizedName() {
        return "item.gregtechmod.sensor_card";
    }

    @Override
    public CardState update(World world, ICardReader card, int i, BlockPos blockPos) {
        TileEntity tTileEntity = world.getTileEntity(card.getTarget());
        if (tTileEntity instanceof IPanelInfoProvider && ((IPanelInfoProvider)tTileEntity).isGivingInformation()) {
            card.setString("mainInfo", ((IPanelInfoProvider)tTileEntity).getMainInfo());
            card.setString("secondaryInfo", ((IPanelInfoProvider)tTileEntity).getSecondaryInfo());
            card.setString("tertiaryInfo", ((IPanelInfoProvider)tTileEntity).getTertiaryInfo());
            return CardState.OK;
        }
        return CardState.NO_TARGET;
    }

    @Override
    public List<PanelString> getStringData(int displaySettings, ICardReader card, boolean showLabels) {
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
    public ICardGui getSettingsScreen(ICardReader iCardReader) {
        return null;
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
