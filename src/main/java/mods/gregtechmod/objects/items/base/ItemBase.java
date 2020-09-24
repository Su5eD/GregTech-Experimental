package mods.gregtechmod.objects.items.base;

import mods.gregtechmod.core.GregtechMod;
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

    public ItemBase(String name) {
        this(name, null);
    }

    public ItemBase(String name, @Nullable String description) {
        this(name, description, false);
    }

    public ItemBase(String name, @Nullable String description, boolean hasEffect) {
        this.name = name;
        this.toolTip = description;
        this.hasEffect = hasEffect;
    }

    public Item setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return this.hasEffect;
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregtechMod.getModelResourceLocation(this.name, this.folder));
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (this.toolTip != null) tooltip.add(this.toolTip);
    }
}
