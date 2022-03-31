package mods.gregtechmod.api.machine;

import com.mojang.authlib.GameProfile;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * Provides upgrade item compatibility
 *
 * @author Su5eD
 */
public interface IUpgradableMachine extends IElectricMachine {

    /**
     * I'm not responsible for any damage caused by calling this method
     */
    void forceAddUpgrade(ItemStack stack);

    boolean addUpgrade(ItemStack stack, EntityPlayer player);

    boolean hasSteamTank();

    @Nullable
    FluidTank getSteamTank();

    void addSteamTank();

    GameProfile getOwner();

    void setOwner(GameProfile owner);

    int getExtraEUCapacity();

    void addExtraEUCapacity(int extraCapacity);

    void addExtraTier();

    int getUpgradeCount(GtUpgradeType type);

    int getUpgradeCount(IC2UpgradeType type);

    boolean isPrivate();

    void setPrivate(boolean value);

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

    int getSteamCapacity();

    long getStoredMj();

    long getMjCapacity();

    void setMjCapacity(long capacity);

    default int getBaseSinkTier() {
        return 0;
    }

    default int getBaseSourceTier() {
        return 0;
    }

    default int getBaseSourcePackets() {
        return 1;
    }
}
