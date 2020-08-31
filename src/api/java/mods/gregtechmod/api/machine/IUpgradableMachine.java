package mods.gregtechmod.api.machine;

import com.mojang.authlib.GameProfile;
import ic2.api.upgrade.UpgradableProperty;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Provides upgrade item compatibility
 * @author Su5eD
 */
public interface IUpgradableMachine extends IGregtechMachine {

    int getTier();

    boolean hasSteamTank();

    @Nullable
    FluidTank getSteamTank();

    void addSteamTank();

    @Nullable
    GameProfile getOwner();

    int getSinkTier();

    int getSourceTier();

    void setEUcapacity(double capacity);

    void setSinkTier(int tier);

    void setSourceTier(int tier);

    double getExtraEnergyStorage();

    int getTransformerUpgradeCount();

    int getOverclockersCount();

    void setOverclockerCount(int count);

    boolean isPrivate();

    void setPrivate(boolean value, GameProfile owner);

    Set<GtUpgradeType> getCompatibleGtUpgrades();

    Set<UpgradableProperty> getCompatibleIC2Upgrades();
}
