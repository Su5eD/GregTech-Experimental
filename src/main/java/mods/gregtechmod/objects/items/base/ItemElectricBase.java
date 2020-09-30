package mods.gregtechmod.objects.items.base;

import ic2.core.item.BaseElectricItem;
import mods.gregtechmod.core.GregtechMod;
import mods.gregtechmod.util.IModelInfoProvider;
import mods.gregtechmod.util.ModelInformation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class ItemElectricBase extends BaseElectricItem implements IModelInfoProvider {
    public final String name;
    private final String description;
    private String folder;

    public ItemElectricBase(String name, String description, double maxCharge, double transferLimit, int tier) {
        super(null, maxCharge, transferLimit, tier);
        this.name = name;
        this.description = description;
    }

    public Item setFolder(String folder) {
        this.folder = folder;
        return this;
    }

    @Override
    public String getTranslationKey() {
        return "item."+this.name; //TODO: Can this be exchanged for a setTranslationKey?
    }

    @Override
    public ModelInformation getModelInformation() {
        return new ModelInformation(GregtechMod.getModelResourceLocation(this.name, this.folder));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add("Tier: "+this.tier);
        if (this.description != null) tooltip.add(this.description);
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }
}
