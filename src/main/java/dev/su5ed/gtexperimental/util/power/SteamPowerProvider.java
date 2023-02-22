package dev.su5ed.gtexperimental.util.power;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.api.machine.PowerProvider;
import dev.su5ed.gtexperimental.api.machine.UpgradableBlockEntity;
import dev.su5ed.gtexperimental.api.util.GtFluidTank;
import dev.su5ed.gtexperimental.util.GtFluidTankImpl;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.SteamHelper;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Set;

public class SteamPowerProvider implements PowerProvider {
    private final GtFluidTank steamTank;

    @SuppressWarnings("deprecation")
    public SteamPowerProvider(UpgradableBlockEntity machine) {
        this.steamTank = machine.addTank(new GtFluidTankImpl("steam", machine.be(), 0, fluidStack -> fluidStack.getFluid().is(GregTechTags.STEAM), GtUtil.ALL_FACINGS, Set.of()));
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
    public int getCapacity() {
        return (int) SteamHelper.getEUForSteam(this.steamTank.getFluid(), this.steamTank.getCapacity());
    }
}
