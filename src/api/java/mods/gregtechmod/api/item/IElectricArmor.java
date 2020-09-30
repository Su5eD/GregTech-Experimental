package mods.gregtechmod.api.item;

import mods.gregtechmod.api.util.ArmorPerk;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collection;
import java.util.Map;

public interface IElectricArmor {

    Collection<ArmorPerk> getPerks();

    Map<EntityPlayer, Float> getJumpChargeMap();

    boolean canProvideEnergy();

    double getAbsorbtionPercentage();

    int getDamageEnergyCost();
}
