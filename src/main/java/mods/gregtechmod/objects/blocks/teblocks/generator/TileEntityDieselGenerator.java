package mods.gregtechmod.objects.blocks.teblocks.generator;

import mods.gregtechmod.api.recipe.fuel.GtFuels;
import mods.gregtechmod.api.recipe.fuel.IFuel;
import mods.gregtechmod.api.recipe.fuel.IFuelProvider;
import mods.gregtechmod.api.recipe.ingredient.IRecipeIngredient;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiDieselGenerator;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityFluidGenerator;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerFluidGenerator;
import mods.gregtechmod.recipe.fuel.FuelSimple;
import mods.gregtechmod.recipe.ingredient.RecipeIngredientItemStack;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityDieselGenerator extends TileEntityFluidGenerator {

    public TileEntityDieselGenerator() {
        super("diesel_generator", GtFuels.diesel);
    }

    @Override
    public int getSourceTier() {
        return 1;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 1000000;
    }

    @Override
    protected double getMaxOutputEUp() {
        return 12;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiDieselGenerator(new ContainerFluidGenerator(player, this), this.tank.content);
    }
    
    public static class FuelCanRecipeProvider implements IFuelProvider<IFuel<IRecipeIngredient>, ItemStack> {
        @Override
        public IFuel<IRecipeIngredient> getFuel(ItemStack target) {
            int fuelCanValue = ModHandler.getFuelCanValue(target);
            if (fuelCanValue > 0) {
                return FuelSimple.create(RecipeIngredientItemStack.create(target, 1), ModHandler.emptyFuelCan.copy(), fuelCanValue * 5 / 4F / 1000);
            }
            return null;
        }

        @Override
        public boolean hasFuel(ItemStack target) {
            return target.isItemEqual(ModHandler.filledFuelCan);
        }
    }
}
