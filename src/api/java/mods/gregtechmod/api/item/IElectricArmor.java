package mods.gregtechmod.api.item;

import mods.gregtechmod.api.util.ArmorPerk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.Collection;
import java.util.Map;

public interface IElectricArmor {

    EntityEquipmentSlot getSlot();

    Collection<ArmorPerk> getPerks();

    Map<EntityPlayer, Float> getJumpChargeMap();

    boolean canProvideEnergy();

    double getAbsorbtionPercentage();

    int getDamageEnergyCost();
}
