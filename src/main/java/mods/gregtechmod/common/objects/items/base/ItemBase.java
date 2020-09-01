package mods.gregtechmod.common.objects.items.base;

import mods.gregtechmod.common.core.GregtechMod;
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
    protected String name;
    protected String toolTip;
    protected String prefix;
    protected String folder;
    protected boolean hasEffect;

    public ItemBase(String name) {
        this(name, null, null, null);
    }

    public ItemBase(String name, @Nullable String description) {
        this(name, description, null, null);
    }

    public ItemBase(String name, @Nullable String description, String prefix) {
        this(name, description, prefix, null);
    }

    public ItemBase(String name, @Nullable String description, String prefix, boolean hasEffect) {
        this(name, description, prefix, null, hasEffect);
    }

    public ItemBase(String name, @Nullable String description, String prefix, String folder) {
        this(name, description, prefix, folder, false);
    }

    public ItemBase(String name, @Nullable String description, String prefix, String folder, boolean hasEffect) {
        String bName = prefix != null ? prefix+"_"+name : name;
        setTranslationKey(bName);
        setRegistryName(bName);
        setCreativeTab(GregtechMod.GREGTECH_TAB);
        this.name = name;
        this.toolTip = description;
        this.prefix = prefix;
        this.folder = folder;
        this.hasEffect = hasEffect;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.hasEffect;
    }

    @Override
    public void registerModels() {
        GregtechMod.proxy.registerModel(this, 0, name, prefix, folder);
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this.toolTip != null) tooltip.add(this.toolTip);
    }
}
