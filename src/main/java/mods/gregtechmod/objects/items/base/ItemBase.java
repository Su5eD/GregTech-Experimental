package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBase extends Item implements IModelInfoProvider {
    protected String name;
    protected String toolTip;
    protected String folder;
    protected boolean hasEffect;
    protected boolean isEnchantable;
    protected boolean showDurability = true;

    public ItemBase(String name, @Nullable String description) {
        this(name, description, false);
    }

    public ItemBase(String name, @Nullable String description, boolean hasEffect) {
        this(name, description, 0, hasEffect);
    }

    public ItemBase(String name, @Nullable String description, int durability) {
        this(name, description, durability, false);
    }

    public ItemBase(String name, @Nullable String description, int durability, boolean hasEffect) {
        this.name = name;
        this.toolTip = description;
        this.hasEffect = hasEffect;
        setMaxDamage(durability - 1);
    }

    public ItemBase setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    public ItemBase setEnchantable(boolean value) {
        this.isEnchantable = value;
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
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation(this.name, this.folder));
    }

    protected final String getDurabilityInfo(ItemStack stack) {
        if (stack.getMaxDamage() > 0) return (stack.getMaxDamage() - stack.getItemDamage() + 1) + " / " + (stack.getMaxDamage() + 1);
        else return "";
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String durability = getDurabilityInfo(stack);
        if (this.showDurability && !durability.isEmpty()) tooltip.add(durability);
        if (this.toolTip != null) tooltip.add(this.toolTip);
    }

    @Override
    public String getTranslationKey() {
        return Reference.MODID+"."+super.getTranslationKey();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }
}
