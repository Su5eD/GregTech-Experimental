package mods.gregtechmod.objects;

import ic2.core.block.comp.Components;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityEnergy;
import mods.gregtechmod.objects.blocks.teblocks.component.*;
import mods.gregtechmod.objects.blocks.teblocks.computercube.TileEntityComputerCube;
import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntityIDSU;
import mods.gregtechmod.objects.blocks.teblocks.energy.TileEntitySuperconductorWire;
import mods.gregtechmod.util.nbt.NBTSaveHandler;

import java.util.Locale;

public enum GregTechComponent {
    COVER_HANDLER(CoverHandler.class),
    SIDED_EMITTER(SidedRedstoneEmitter.class),
    COIL_HANDLER(CoilHandler.class),
    BASIC_TANK(BasicTank.class),
    MAINTENANCE(Maintenance.class),
    UPGRADE_MANAGER(UpgradeManager.class),
    DYNAMIC_ADJUSTABLE_ENERGY(TileEntityEnergy.DynamicAdjustableEnergy.class),
    EXPLODING_ENERGY_SOURCE(TileEntityEnergy.ExplodingEnergySource.class),
    CONDUCTOR_ENERGY(TileEntitySuperconductorWire.ConductorEnergy.class),
    IDSU_ENERGY(TileEntityIDSU.IDSUEnergy.class),
    COMPUTER_CUBE_MODULE(TileEntityComputerCube.ComputerCubeModuleComponent.class),
    GT_REDSTONE_EMITTER(GtRedstoneEmitter.class);

    private final Class<? extends GtComponentBase> clazz;

    GregTechComponent(Class<? extends GtComponentBase> clazz) {
        this.clazz = clazz;
    }

    public void register() {
        NBTSaveHandler.initClass(this.clazz);
        Components.register(this.clazz, Reference.MODID + ":" + name().toLowerCase(Locale.ROOT));
    }
}
