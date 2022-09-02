package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.api.item.SolderingMetal;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SolderingMetalItem extends ResourceItem {

    public SolderingMetalItem(int durability) {
        this(new ExtendedItemProperties<>()
            .durability(durability)
            .description(GtLocale.itemDescriptionKey("soldering_metal")));
    }

    public SolderingMetalItem(ExtendedItemProperties<?> properties) {
        super(properties);
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new SolderingMetalHandler(stack);
    }
    
    public static class SolderingMetalHandler implements SolderingMetal, ICapabilityProvider {
        private final LazyOptional<SolderingMetal> optional = LazyOptional.of(() -> this);
        
        private final ItemStack stack;

        public SolderingMetalHandler(ItemStack stack) {
            this.stack = stack;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void onUsed(Player player) {
            GtUtil.hurtStack(this.stack, 1, player);
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return Capabilities.SOLDERING_METAL.orEmpty(cap, optional);
        }
    }
}
