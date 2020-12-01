package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ItemToolBase extends ItemTool implements IModelInfoProvider {
    public final String name;
    protected String toolTip;
    protected final int damageOnHit;
    protected boolean showDurability = true;
    protected List<String> effectiveAganist = new ArrayList<>();

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material) {
        this(name, description, durability, attackDamage, material, Collections.emptySet(), 3);
    }

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, int damageOnHit) {
        this(name, description, durability, attackDamage, material, Collections.emptySet(), damageOnHit);
    }

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, Set<Block> effectiveBlocks, int damageOnHit) {
        super(attackDamage, 0, material, effectiveBlocks);
        this.name = name;
        this.toolTip = description;
        this.damageOnHit = damageOnHit;
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
        if (this.damageOnHit > 0) stack.damageItem(this.damageOnHit, attacker);
        ResourceLocation entityName = EntityList.getKey(target);
        if (entityName != null && this.effectiveAganist.contains(entityName.toString())) target.attackEntityFrom(DamageSource.causeMobDamage(attacker), this.attackDamage);
        return true;
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregTechMod.getModelResourceLocation(this.name, "tool"));
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
