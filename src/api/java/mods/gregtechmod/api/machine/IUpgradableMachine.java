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
public interface IUpgradableMachine extends IElectricMachine {
    
    boolean hasSteamTank();

    @Nullable
    FluidTank getSteamTank();

    void addSteamTank();

    @Nullable
    GameProfile getOwner();

    int getExtraEUCapacity();
    
    void addExtraEUCapacity(int extraCapacity);
    
    void addExtraSinkTier();

    int getUpgradeCount(GtUpgradeType type);

    int getUpgradeCount(IC2UpgradeType type);

    int getOverclockersCount();

    boolean isPrivate();

    void setPrivate(boolean value, GameProfile owner);

    Set<GtUpgradeType> getCompatibleGtUpgrades();

    Set<IC2UpgradeType> getCompatibleIC2Upgrades();

    boolean hasMjUpgrade();

    void addMjUpgrade();

    /**
     * @return the most among stored EU, MJ, or steam
     */
    double getUniversalEnergy();

    /**
     * @return The maximum amount of energy this machine can store, either EU, MJ, or Steam converted to EU
     */
    double getUniversalEnergyCapacity();

    double getStoredSteam();

    double getSteamCapacity();

    long getStoredMj();

    long getMjCapacity();

    void setMjCapacity(long capacity);
}
