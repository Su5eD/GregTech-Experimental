package mods.gregtechmod.objects.items.tools;

import ic2.api.item.ElectricItem;
import mods.gregtechmod.api.item.ISolderingMetal;
import mods.gregtechmod.api.item.ISolderingTool;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.objects.items.base.ItemElectricBase;
import mods.gregtechmod.util.GtLocale;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.IntStream;

public class ItemSolderingTool extends ItemElectricBase implements ISolderingTool {

    public ItemSolderingTool() {
        super("soldering_tool", 10000, 1000, 1, 1000, false);
        setFolder("tool");
        setRegistryName("soldering_tool");
        setTranslationKey("soldering_tool");
        setCreativeTab(GregTechMod.GREGTECH_TAB);
        this.hasEmptyVariant = true;
    }

    public ItemStack findSolderingMetal(EntityPlayer player) {
        return IntStream.range(0, player.inventory.getSizeInventory())
            .mapToObj(player.inventory::getStackInSlot)
            .filter(stack -> stack.getItem() instanceof ISolderingMetal)
            .findFirst()
            .orElse(null);
    }

    @Override
    public boolean solder(ItemStack stack, EntityPlayer player, boolean simulate) {
        if (ElectricItem.manager.getCharge(stack) < 1000) return false;
        ItemStack metalStack = findSolderingMetal(player);
        if (metalStack == null) return false;
        ISolderingMetal metal = (ISolderingMetal) metalStack.getItem();
        if (!metal.canUse(stack)) return false;

        if (!simulate) {
            ElectricItem.manager.use(stack, 1000, player);
            metal.onUsed(player, metalStack);
        }
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
            tooltip.add(GtLocale.translateItem("soldering_tool.metal_requirement"));
        }
    }
}
