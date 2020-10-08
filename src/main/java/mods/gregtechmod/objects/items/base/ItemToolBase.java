package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemToolBase extends ItemTool implements IModelInfoProvider {
    protected String name;
    protected String toolTip;
    protected boolean showDurability = true;

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material) {
        this(name, description, durability, attackDamage, material, Collections.emptySet());
    }

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, Set<Block> effectiveBlocks) {
        super(attackDamage, 0, material, effectiveBlocks);
        this.name = name;
        this.toolTip = description;
        setMaxDamage(durability - 1);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (showDurability && stack.getMaxDamage() > 0) tooltip.add((stack.getMaxDamage() - stack.getItemDamage() + 1) + " / " + (stack.getMaxDamage() + 1));
        if (this.toolTip != null) tooltip.add(this.toolTip);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(3, attacker);
        return true;
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation(this.name, "tool"));
    }

    @Override
    public String getTranslationKey() {
        return GregTechMod.MODID+"."+super.getTranslationKey();
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return this.getTranslationKey();
    }
}
