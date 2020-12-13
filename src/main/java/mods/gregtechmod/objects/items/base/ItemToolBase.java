package mods.gregtechmod.objects.items.base;

import ic2.core.item.IPseudoDamageItem;
import ic2.core.item.tool.ToolClass;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
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
    protected List<String> effectiveAganist = new ArrayList<>();
    protected final Set<ToolClass> toolClasses;

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage) {
        this(name, description, durability, attackDamage, ToolMaterial.WOOD, Collections.emptySet(), 3, 0, Collections.emptySet());
    }

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material) {
        this(name, description, durability, attackDamage, material, Collections.emptySet(), 3, 0, Collections.emptySet());
    }

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, int damageOnHit, int harvestLevel, Set<ToolClass> toolClasses) {
        this(name, description, durability, attackDamage, ToolMaterial.WOOD, Collections.emptySet(), damageOnHit, harvestLevel, toolClasses);
    }

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, int damageOnHit) {
        this(name, description, durability, attackDamage, material, Collections.emptySet(), damageOnHit, 0, Collections.emptySet());
    }

    public ItemToolBase(String name, @Nullable String description, int durability, float attackDamage, ToolMaterial material, Set<Block> effectiveBlocks, int damageOnHit, int harvestLevel, Set<ToolClass> toolClasses) {
        super(attackDamage - 1, 0, material, effectiveBlocks);
        this.name = name;
        this.toolTip = description;
        this.damageOnHit = damageOnHit;
        this.toolClasses = toolClasses;
        for (ToolClass toolClass : toolClasses) setHarvestLevel(toolClass.name, harvestLevel);
        setMaxDamage(durability - 1);
        setMaxStackSize(1);
        setNoRepair();
    }

    @Override
    public boolean canHarvestBlock(IBlockState state) {
        Material material = state.getMaterial();

        for(ToolClass toolClass : this.toolClasses) {
            if (toolClass.whitelist.contains(material) || toolClass.whitelist.contains(state.getBlock())) return true;
            if (toolClass.blacklist.contains(material) || toolClass.blacklist.contains(state.getBlock())) return false;
        }

        return super.canHarvestBlock(state);
    }

    protected final String getDurabilityInfo(ItemStack stack) {
        if (!(this instanceof IPseudoDamageItem) && stack.getMaxDamage() > 0) return (stack.getMaxDamage() - stack.getItemDamage() + 1) + " / " + (stack.getMaxDamage() + 1);
        else return "";
    }

    @Override
    public float getDestroySpeed(ItemStack stack, IBlockState state) {
        return this.canHarvestBlock(state, stack) ? this.efficiency : super.getDestroySpeed(stack, state);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        String durability = getDurabilityInfo(stack);
        if (!durability.isEmpty()) tooltip.add(durability);
        if (this.toolTip != null) tooltip.add(this.toolTip);
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (this.damageOnHit > 0) stack.damageItem(this.damageOnHit, attacker);
        ResourceLocation entityName = EntityList.getKey(target);
        if (entityName != null && this.effectiveAganist.contains(entityName.toString())) GtUtil.damageEntity(target, attacker, this.attackDamage+1);
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
