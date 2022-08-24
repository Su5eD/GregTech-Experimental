package mods.gregtechmod.objects.items.base;

import net.minecraft.block.Block;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IRarity;

public class ItemBlockBase extends ItemBlock {
    private final EnumRarity rarity;
    
    public ItemBlockBase(Block block, EnumRarity rarity) {
        super(block);
        this.rarity = rarity;
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return this.rarity;
    }
}
