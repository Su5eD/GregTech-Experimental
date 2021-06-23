package mods.gregtechmod.compat;

import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.init.OreDictHandler;
import mods.gregtechmod.util.OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.events.TinkerRegisterEvent;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICastingRecipe;

import java.util.Locale;

@Mod.EventBusSubscriber
public class TConstructModule {

    @SubscribeEvent
    @Optional.Method(modid = "tconstruct")
    public static void onTinkerRegisterBasinCastingRegister(TinkerRegisterEvent.BasinCastingRegisterEvent event) {
        event.setCanceled(unifyCastingRecipe(CastingRecipeType.BASIN, event.getRecipe()));
    }

    @SubscribeEvent
    @Optional.Method(modid = "tconstruct")
    public static void onTinkerRegisterTableCastingRegister(TinkerRegisterEvent.TableCastingRegisterEvent event) {
        event.setCanceled(unifyCastingRecipe(CastingRecipeType.TABLE, event.getRecipe()));
    }

    @Optional.Method(modid = "tconstruct")
    private static boolean unifyCastingRecipe(CastingRecipeType type, ICastingRecipe recipe) {
        if (recipe instanceof CastingRecipe) {
            ItemStack output = ((CastingRecipe) recipe).getResult();
            if (!output.isEmpty()) {
                String name = OreDictUnificator.getAssociation(output);
                String unifiedName = OreDictHandler.GT_ORE_NAMES.get(name);
                if (unifiedName != null) name = unifiedName;

                ItemStack unified = OreDictUnificator.getUnifiedOre(name);
                if (!unified.isEmpty() && !output.isItemEqual(unified) && GregTechAPI.getDynamicConfig(type.getName(), name, true)) {
                    ICastingRecipe unifiedRecipe = new CastingRecipe(unified, ((CastingRecipe) recipe).cast, ((CastingRecipe) recipe).getFluid(), false, false);
                    if (type == CastingRecipeType.BASIN) TinkerRegistry.registerBasinCasting(unifiedRecipe);
                    else TinkerRegistry.registerTableCasting(unifiedRecipe);
                    return true;
                }
            }
        }
        return false;
    }
    
    private enum CastingRecipeType {
        BASIN,
        TABLE;
        
        public String getName() {
            return name().toLowerCase(Locale.ROOT) + "_casting";
        }
    }
}
