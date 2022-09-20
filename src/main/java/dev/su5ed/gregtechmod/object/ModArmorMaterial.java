package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.api.util.Reference;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Locale;

public enum ModArmorMaterial implements ArmorMaterial {
    IRON(ArmorMaterials.IRON),
    DIAMOND(ArmorMaterials.DIAMOND);

    private final ArmorMaterial parent;

    ModArmorMaterial(ArmorMaterial parent) {
        this.parent = parent;
    }

    @Override
    public int getDurabilityForSlot(EquipmentSlot pSlot) {
        return 0;
    }

    @Override
    public int getDefenseForSlot(EquipmentSlot slot) {
        return this.parent.getDefenseForSlot(slot);
    }

    @Override
    public int getEnchantmentValue() {
        return this.parent.getEnchantmentValue();
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.parent.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.parent.getRepairIngredient();
    }

    @Override
    public String getName() {
        return Reference.MODID + ":" + name().toLowerCase(Locale.ROOT);
    }

    @Override
    public float getToughness() {
        return this.parent.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return this.parent.getKnockbackResistance();
    }
}
