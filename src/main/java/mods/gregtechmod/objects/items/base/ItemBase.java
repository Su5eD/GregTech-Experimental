package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.ICustomItemModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ItemBase extends Item implements ICustomItemModel {
    protected String name;
    protected Supplier<String> description;
    protected String folder;
    protected boolean hasEffect;
    protected boolean isEnchantable;
    protected boolean showDurability = true;
    protected EnumRarity rarity;

    public ItemBase(String name) {
        this(name, name, 0);
    }

    public ItemBase(String name, @Nullable String description) {
        this(name, () -> description, false);
    }

    public ItemBase(String name, Supplier<String> description) {
        this(name, description, false);
    }

    public ItemBase(String name, Supplier<String> description, boolean hasEffect) {
        this(name, description, 0, hasEffect);
    }

    public ItemBase(String name, int durability) {
        this(name, name, durability);
    }

    public ItemBase(String name, String description, int durability) {
        this(name, () -> GtLocale.translateItemDescription(description), durability, false);
    }

    public ItemBase(String name, Supplier<String> description, int durability) {
        this(name, description, durability, false);
    }

    public ItemBase(String name, Supplier<String> description, int durability, boolean hasEffect) {
        this.name = name;
        this.description = description;
        this.hasEffect = hasEffect;
        if (durability > 0) setMaxDamage(durability - 1);
    }

    public ItemBase setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public ItemBase setEnchantable(boolean value) {
        this.isEnchantable = value;
        return this;
    }

    public ItemBase setRarity(EnumRarity rarity) {
        this.rarity = rarity;
        return this;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return this.isEnchantable;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.hasEffect;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return this.isEnchantable;
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return this.rarity != null ? this.rarity : super.getForgeRarity(stack);
    }

    @Override
    public ResourceLocation getItemModel() {
        return GtUtil.getModelResourceLocation(this.name, this.folder);
    }

    protected final String getDurabilityInfo(ItemStack stack) {
        return stack.getMaxDamage() > 0 ? (stack.getMaxDamage() - stack.getItemDamage() + 1) + " / " + (stack.getMaxDamage() + 1) : "";
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String durability = getDurabilityInfo(stack);
        if (this.showDurability && !durability.isEmpty()) tooltip.add(durability);

        String description = this.description.get();
        if (description != null) tooltip.add(description);
    }

    @Override
    public String getTranslationKey() {
        return GtLocale.buildKey(super.getTranslationKey());
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }
}
