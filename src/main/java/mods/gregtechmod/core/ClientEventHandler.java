package mods.gregtechmod.core;

import ic2.api.item.IC2Items;
import ic2.core.item.ItemFluidCell;
import mods.gregtechmod.api.util.GtUtil;
import mods.gregtechmod.init.FluidLoader;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.TileEntityIndustrialCentrifuge;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onRenderPlayer(RenderPlayerEvent.Pre event) {
        if (GtUtil.getFullInvisibility(event.getEntityPlayer())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<String> tooltip = event.getToolTip();

        if (stack.isItemEqual(IC2Items.getItem("dust", "diamond"))) tooltip.add("C128");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "lead"))) tooltip.add("Pb");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "obsidian"))) tooltip.add("MgFeSi2O8");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "coal"))) tooltip.add("C2");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "iron"))) tooltip.add("Fe");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "gold"))) tooltip.add("Au");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "copper"))) tooltip.add("Cu");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "tin"))) tooltip.add("Sn");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "bronze"))) tooltip.add("SnCu3");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "silver"))) tooltip.add("Ag");
        else if (stack.isItemEqual(IC2Items.getItem("dust", "clay"))) tooltip.add("Na2LiAl2Si2");
        else if (stack.isItemEqual(IC2Items.getItem("misc_resource", "ashes"))) tooltip.add("C");

        FluidStack fluidContained = FluidUtil.getFluidContained(stack);
        Item item = stack.getItem();
        if (TileEntityIndustrialCentrifuge.isCell(item) && fluidContained != null) {
            FluidLoader.FLUIDS.forEach(provider -> {
                if (provider.getFluid() == fluidContained.getFluid()) {
                    String description = provider.getDescription();
                    if (description != null) tooltip.add(item instanceof ItemFluidCell ? 2 : 1, description);
                }
            });
        }
    }
}
