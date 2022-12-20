package dev.su5ed.gregtechmod.util.power;

import dev.su5ed.gregtechmod.GregTechTags;
import dev.su5ed.gregtechmod.api.cover.CoverHandler;
import dev.su5ed.gregtechmod.api.machine.PowerProvider;
import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.util.GtFluidTank;
import dev.su5ed.gregtechmod.util.GtFluidTankImpl;
import dev.su5ed.gregtechmod.util.GtUtil;
import dev.su5ed.gregtechmod.util.SteamHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Set;

public class SteamPowerProvider implements PowerProvider {
    private final GtFluidTank steamTank;

    @SuppressWarnings("deprecation")
    public SteamPowerProvider(UpgradableBlockEntity be, CoverHandler coverHandler) {
        this.steamTank = be.addTank(new GtFluidTankImpl("steam", coverHandler, 0, fluidStack -> fluidStack.getFluid().is(GregTechTags.STEAM), GtUtil.ALL_FACINGS, Set.of()));
    }

    public GtFluidTank getSteamTank() {
        return this.steamTank;
    }

    @Override
    public int getPriority() {
        return 2;
    }

    @Override
    public double useEnergy(double amount, boolean simulate) {
        int steam = SteamHelper.getSteamForEU(amount, this.steamTank.getFluid());
        if (steam > 0) {
            FluidStack drained = this.steamTank.drain(steam, IFluidHandler.FluidAction.SIMULATE);
            if (drained.getAmount() >= amount) {
                if (!simulate) {
                    this.steamTank.drain(drained, IFluidHandler.FluidAction.EXECUTE);
                }
                return amount;
            }
        }
        return 0;
    }

    @Override
    public double getStoredEnergy() {
        return SteamHelper.getEUForSteam(this.steamTank.getFluid());
    }

    @Override
    public double getCapacity() {
        return SteamHelper.getEUForSteam(this.steamTank.getFluid(), this.steamTank.getCapacity());
    }
}
