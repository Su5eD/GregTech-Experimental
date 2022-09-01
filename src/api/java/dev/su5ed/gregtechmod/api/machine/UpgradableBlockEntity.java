package dev.su5ed.gregtechmod.api.machine;

import com.mojang.authlib.GameProfile;
import dev.su5ed.gregtechmod.api.upgrade.GtUpgradeType;
import dev.su5ed.gregtechmod.api.upgrade.IC2UpgradeType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * Provides upgrade stack compatibility
 * @author Su5eD
 */
public interface UpgradableBlockEntity extends IElectricMachine {

    /**
     * I'm not responsible for any damage caused by calling this method
     */
    void forceAddUpgrade(ItemStack stack);
    
    boolean addUpgrade(ItemStack stack, Player player);
    
    boolean hasSteamTank();

    @Nullable
    FluidTank getSteamTank();

    void addSteamTank();

    GameProfile getOwner();
    
    void setOwner(GameProfile owner);
    
    boolean isOwnedBy(GameProfile profile);

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
