package mods.gregtechmod.common.objects.blocks;

import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.init.BlockInit;
import mods.gregtechmod.common.init.ItemInit;
import mods.gregtechmod.common.util.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {

    public BlockBase(String name, Material material, Float hardness, Float resistance) {
        super(material);
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(GregtechMod.gregtechtab);
        setHardness(hardness);
        setResistance(resistance);

        BlockInit.BLOCKS.add(this);
        ItemInit.ITEMS.put(name, new ItemBlock(this).setRegistryName(name));
    }

    @Override
    public void registerModels() {
        GregtechMod.proxy.registerModel(Item.getItemFromBlock(this), 0);
    }
}
