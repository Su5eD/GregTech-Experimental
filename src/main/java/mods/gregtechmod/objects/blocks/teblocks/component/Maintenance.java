package mods.gregtechmod.objects.blocks.teblocks.component;

import ic2.core.block.TileEntityBlock;
import ic2.core.network.GrowingBuffer;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.DataInput;
import java.io.IOException;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Maintenance extends GtComponentBase {
    @NBTPersistent
    private boolean wrench;
    @NBTPersistent
    private boolean screwdriver;
    @NBTPersistent
    private boolean softHammer;
    @NBTPersistent
    private boolean hardHammer;
    @NBTPersistent
    private boolean solderingTool;
    @NBTPersistent
    private boolean crowbar;

    public Maintenance(TileEntityBlock parent) {
        super(parent);
    }

    public void doRandomMaintenanceDamage(Random rand) {
        if (rand.nextInt(6000) == 0) {
            switch (rand.nextInt(6)) {
                case 0:
                    this.wrench = false;
                    break;
                case 1:
                    this.screwdriver = false;
                    break;
                case 2:
                    this.softHammer = false;
                    break;
                case 3:
                    this.hardHammer = false;
                    break;
                case 4:
                    this.solderingTool = false;
                    break;
                case 5:
                    this.crowbar = false;
                    break;
            }
        }
    }
    
    @Override
    public void onContainerUpdate(EntityPlayerMP player) {
        GrowingBuffer buf = new GrowingBuffer(8);
        buf.writeBoolean(this.wrench);
        buf.writeBoolean(this.screwdriver);
        buf.writeBoolean(this.softHammer);
        buf.writeBoolean(this.hardHammer);
        buf.writeBoolean(this.solderingTool);
        buf.writeBoolean(this.crowbar);
        buf.flip();
                                    
        setNetworkUpdate(player, buf);
    }
            
    @Override
    public void onNetworkUpdate(DataInput in) throws IOException {
        this.wrench = in.readBoolean();
        this.screwdriver = in.readBoolean();
        this.softHammer = in.readBoolean();
        this.hardHammer = in.readBoolean();
        this.solderingTool = in.readBoolean();
        this.crowbar = in.readBoolean();
    }
    
    public void collectMaintenanceStatus(Maintenance source) {
        getAndResetMaintenanceStatus(source::getWrench, this::setWrench, source::setWrench);
        getAndResetMaintenanceStatus(source::getScrewdriver, this::setScrewdriver, source::setScrewdriver);
        getAndResetMaintenanceStatus(source::getSoftHammer, this::setSoftHammer, source::setSoftHammer);
        getAndResetMaintenanceStatus(source::getHardHammer, this::setHardHammer, source::setHardHammer);
        getAndResetMaintenanceStatus(source::getSolderingTool, this::setSolderingTool, source::setSolderingTool);
        getAndResetMaintenanceStatus(source::getCrowbar, this::setCrowbar, source::setCrowbar);
    }
    
    private void getAndResetMaintenanceStatus(Supplier<Boolean> getter, Consumer<Boolean> setter, Consumer<Boolean> resetter) {
        if (getter.get()) setter.accept(true);
        resetter.accept(false);
    }
    
    public void setAll(boolean value) {
        this.wrench = this.screwdriver = this.softHammer = this.hardHammer = this.solderingTool = this.crowbar = value;
    }

    public void setWrench(boolean wrench) {
        this.wrench = wrench;
    }

    public void setScrewdriver(boolean screwdriver) {
        this.screwdriver = screwdriver;
    }

    public void setSoftHammer(boolean softHammer) {
        this.softHammer = softHammer;
    }

    public void setHardHammer(boolean hardHammer) {
        this.hardHammer = hardHammer;
    }

    public void setSolderingTool(boolean solderingTool) {
        this.solderingTool = solderingTool;
    }

    public void setCrowbar(boolean crowbar) {
        this.crowbar = crowbar;
    }

    public boolean getWrench() {
        return this.wrench;
    }

    public boolean getScrewdriver() {
        return this.screwdriver;
    }

    public boolean getSoftHammer() {
        return this.softHammer;
    }

    public boolean getHardHammer() {
        return this.hardHammer;
    }

    public boolean getSolderingTool() {
        return this.solderingTool;
    }

    public boolean getCrowbar() {
        return this.crowbar;
    }
}
