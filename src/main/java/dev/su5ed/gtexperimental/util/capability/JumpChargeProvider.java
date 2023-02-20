package dev.su5ed.gtexperimental.util.capability;

import dev.su5ed.gtexperimental.Capabilities;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class JumpChargeProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {
    public static final ResourceLocation NAME = location("jump_charge");

    private final JumpCharge instance = new JumpChargeStore(1.0);
    private final LazyOptional<JumpCharge> optionalData = LazyOptional.of(() -> instance);

    @Override
    public CompoundTag serializeNBT() {
        return this.instance.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.instance.deserializeNBT(nbt);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        return Capabilities.JUMP_CHARGE.orEmpty(cap, this.optionalData);
    }
}
