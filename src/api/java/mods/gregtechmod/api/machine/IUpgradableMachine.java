package mods.gregtechmod.api.machine;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Provides upgrade item compatibility
 * @author Su5eD
 */
public interface IUpgradableMachine extends IElectricalMachine {
    
    boolean hasSteamTank();

    @Nullable
    FluidTank getSteamTank();

    void addSteamTank();

    @Nullable
    GameProfile getOwner();

    void setEUcapacity(double capacity);

    void setSinkTier(int tier);

    double getExtraEnergyStorage();

    int getUpgradeCount(GtUpgradeType type);

    int getUpgradeCount(IC2UpgradeType type);

    int getOverclockersCount();

    void setOverclockerCount(int count);

    boolean isPrivate();

    void setPrivate(boolean value, GameProfile owner);

    Set<GtUpgradeType> getCompatibleGtUpgrades();

    Set<IC2UpgradeType> getCompatibleIC2Upgrades();

    boolean hasMjUpgrade();

    void addMjUpgrade();
}
