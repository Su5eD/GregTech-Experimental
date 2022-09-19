package dev.su5ed.gregtechmod.object;

import dev.su5ed.gregtechmod.GregTechMod;
import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.item.ElectricArmorItem;
import dev.su5ed.gregtechmod.item.ElectricArmorItem.ElectricArmorItemProperties;
import dev.su5ed.gregtechmod.util.ArmorPerk;
import dev.su5ed.gregtechmod.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nullable;

public enum Armor implements TaggedItemProvider {
    CLOAKING_DEVICE(ModArmorMaterial.IRON, EquipmentSlot.CHEST, GregTechMod.isClassic ? 10000000 : 100000000, 8192, GregTechMod.isClassic ? 4 : 5, false, null, Rarity.EPIC, ArmorPerk.INVISIBILITY_FIELD),
    LAPOTRONPACK(ModArmorMaterial.IRON, EquipmentSlot.CHEST, GregTechMod.isClassic ? 10000000 : 100000000, 8192, GregTechMod.isClassic ? 4 : 5, true, GregTechMod.isClassic ? GregTechTags.CRAFTING_10KK_EU_STORE : GregTechTags.CRAFTING_100KK_EU_STORE, Rarity.EPIC),
    LITHIUM_BATPACK(ModArmorMaterial.IRON, EquipmentSlot.CHEST, 600000, 128, 1, true, GregTechTags.CRAFTING_600K_EU_STORE, Rarity.COMMON),
//    ULTIMATE_CHEAT_ARMOR(EquipmentSlot.CHEST, 1000000000, Integer.MAX_VALUE, 1, 10, 100, true, ArmorPerk.values()),
    LIGHT_HELMET(ModArmorMaterial.IRON, EquipmentSlot.HEAD, 10000, 32, 1, false, ArmorPerk.LAMP, ArmorPerk.SOLARPANEL);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

    Armor(ArmorMaterial material, EquipmentSlot slot, int maxCharge, int transferLimit, int tier, boolean chargeProvider, ArmorPerk... perks) {
        this(material, slot, maxCharge, transferLimit, tier, chargeProvider, null, Rarity.COMMON, perks);
    }

    Armor(ArmorMaterial material, EquipmentSlot slot, int maxCharge, int transferLimit, int tier, boolean chargeProvider, TagKey<Item> tag, Rarity rarity, ArmorPerk... perks) {
        this.tag = tag;

        this.instance = Lazy.of(() -> new ElectricArmorItem(material, slot, new ElectricArmorItemProperties()
            .maxCharge(maxCharge)
            .transferLimit(transferLimit)
            .providesEnergy(chargeProvider)
            .energyTier(tier)
            .perks(perks)
            .rarity(rarity)));
    }

    @Override
    public String getRegistryName() {
        return getName();
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Nullable
    @Override
    public TagKey<Item> getTag() {
        return this.tag;
    }
}
