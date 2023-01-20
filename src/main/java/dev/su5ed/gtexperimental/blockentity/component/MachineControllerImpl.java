package dev.su5ed.gtexperimental.blockentity.component;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.machine.MachineController;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.blockentity.base.BaseBlockEntity;
import dev.su5ed.gtexperimental.util.BooleanCountdown;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import static dev.su5ed.gtexperimental.api.Reference.location;

public class MachineControllerImpl extends GtComponentBase<BaseBlockEntity> implements MachineController {
    public static final ResourceLocation NAME = location("work_controller");

    private final Map<Direction, Integer> levels = new HashMap<>();
    private final BooleanCountdown workStartedNow = new BooleanCountdown(2);
    private final LazyOptional<MachineController> optional = LazyOptional.of(() -> this);
    
    private boolean enableWorking = true;
    private boolean enableInput = true;
    private boolean enableOutput = true;

    public MachineControllerImpl(BaseBlockEntity parent) {
        super(parent);
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public void onFieldUpdate(String name) {

    }

    @Override
    public <U> LazyOptional<U> getCapability(@NotNull Capability<U> cap, @Nullable Direction side) {
        if (cap == Capabilities.MACHINE_CONTROLLER) {
            return this.optional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.optional.invalidate();
    }

    @Override
    public int getSignal(Direction side) {
        return this.levels.getOrDefault(side, 0);
    }

    @Override
    public void setRedstoneOutput(Direction side, int strength) {
        if (getSignal(side) != strength) {
            this.levels.put(side, strength);
            this.parent.updateNeighbors();
        }
    }

    @Override
    public boolean isAllowedToWork() {
        return this.enableWorking;
    }

    @Override
    public void setAllowedToWork(boolean value) {
        this.enableWorking = value;
        if (this.enableWorking) {
            this.workStartedNow.reset();
        }
    }

    @Override
    public boolean workJustHasBeenEnabled() {
        return this.workStartedNow.get();
    }

    @Override
    public boolean isInputEnabled() {
        return this.enableInput;
    }

    @Override
    public void setInputEnabled(boolean value) {
        this.enableInput = value;
    }

    @Override
    public boolean isOutputEnabled() {
        return this.enableOutput;
    }

    @Override
    public void setOutputEnabled(boolean value) {
        this.enableOutput = value;
    }


    @Override
    public void tickServer() {
        super.tickServer();
        
        this.workStartedNow.countDown();
    }

    @Override
    public void save(FriendlyCompoundTag tag) {
        super.save(tag);
        
        tag.putBoolean("enableWorking", this.enableWorking);
        tag.putBoolean("enableInput", this.enableInput);
        tag.putBoolean("enableOutput", this.enableOutput);
    }

    @Override
    public void load(FriendlyCompoundTag tag) {
        super.load(tag);
        
        this.enableWorking = tag.getBoolean("enableWorking");
        this.enableInput = tag.getBoolean("enableInput");
        this.enableOutput = tag.getBoolean("enableOutput");
    }
}
