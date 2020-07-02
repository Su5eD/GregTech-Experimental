package mods.gregtechmod.common.objects.items.base;

import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.init.ItemInit;
import mods.gregtechmod.common.util.IHasModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBase extends Item implements IHasModel {
    protected String toolTip;
    protected String prefix;
    protected String name;

    public ItemBase(String name, @Nullable String description, String prefix) {
        this(prefix+"_"+name, description);
        this.prefix = prefix;
        this.name = name;
    }

    public ItemBase(String name, @Nullable String description) {
        this(name);
        this.toolTip = description;
    }

    public ItemBase(String name) {
        setTranslationKey(name);
        setRegistryName(name);
        setCreativeTab(GregtechMod.gregtechtab);
        ItemInit.ITEMS.put(name, this);
        this.name = name;
    }

    @Override
    public void registerModels() {
        GregtechMod.proxy.registerModel(this, 0, prefix, name);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this.toolTip != null) tooltip.add(this.toolTip);
    }
}
