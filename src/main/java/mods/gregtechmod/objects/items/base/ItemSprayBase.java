package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.objects.BlockItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ItemSprayBase extends ItemToolCrafting {

    public ItemSprayBase(String name, int durability, float attackDamage, int craftingDamage, int damageOnHit) {
        super(name, durability, attackDamage, craftingDamage, damageOnHit);
    }

    public ItemSprayBase(String name, String genericDescriptionKey, int durability, float attackDamage, ToolMaterial material, int craftingDamage, int damageOnHit) {
        super(name, genericDescriptionKey, durability, attackDamage, material, craftingDamage, damageOnHit);
    }

    @Override
    public ItemStack getEmptyItem() {
        return BlockItems.Miscellaneous.SPRAY_CAN_EMPTY.getItemStack();
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return false;
    }
}
