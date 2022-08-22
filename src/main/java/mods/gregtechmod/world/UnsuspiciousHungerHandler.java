package mods.gregtechmod.world;

import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.core.GregTechConfig;
import mods.gregtechmod.util.IExhaustingItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = Reference.MODID)
public final class UnsuspiciousHungerHandler {
    
    private static long tickCount;
    
    @SubscribeEvent
    public static void onServerTick(TickEvent.PlayerTickEvent event) {
        if (GregTechConfig.GENERAL.unsuspiciousHungerConfig && tickCount++ % 2400L == 1200L) {
            int count = 64;

            List<NonNullList<ItemStack>> inventories = Arrays.asList(event.player.inventory.mainInventory, event.player.inventory.offHandInventory, event.player.inventory.armorInventory);
            for (NonNullList<ItemStack> list : inventories) {
                for (ItemStack stack : list) {
                    Item item = stack.getItem();
                    boolean isArmor = list == event.player.inventory.armorInventory;
                    if (item instanceof IExhaustingItem && ((IExhaustingItem) item).shouldExhaust(isArmor)) {
                        if (isArmor) count += 256;
                        else count += stack.getMaxStackSize() > 1 ? stack.getCount() : 64;
                    }
                }
            }
            
            event.player.addExhaustion(Math.max(1.0F, count / 666.6F));
        }
    } 
    
    private UnsuspiciousHungerHandler() {}
}
