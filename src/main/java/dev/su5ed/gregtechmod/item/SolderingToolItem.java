package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.api.item.SolderingMetal;
import dev.su5ed.gregtechmod.api.item.SolderingTool;
import dev.su5ed.gregtechmod.compat.ModHandler;
import dev.su5ed.gregtechmod.util.GtLocale;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import one.util.streamex.IntStreamEx;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SolderingToolItem extends ElectricItem {

    public SolderingToolItem() {
        super(new ElectricItemProperties()
            .maxCharge(10000)
            .energyTier(1)
            .operationEnergyCost(1000)
            .transferLimit(1000)
            .hasEmptyVariant(true)
            .autoDescription());
    }

    @Nullable
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new SolderingToolHandler(stack, this.operationEnergyCost);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);
        if (ModHandler.canUseEnergy(stack, this.operationEnergyCost)) {
            components.add(GtLocale.itemKey("soldering_tool", "metal_requirement").toComponent().withStyle(ChatFormatting.GRAY));
        }
    }

    public static class SolderingToolHandler implements SolderingTool, ICapabilityProvider {
        private final LazyOptional<SolderingTool> optional = LazyOptional.of(() -> this);
        
        private final ItemStack stack;
        private final int operationEnergyCost;

        public SolderingToolHandler(ItemStack stack, int operationEnergyCost) {
            this.stack = stack;
            this.operationEnergyCost = operationEnergyCost;
        }

        @Override
        public boolean solder(Player player, boolean simulate) {
            return ModHandler.canUseEnergy(this.stack, this.operationEnergyCost) && findSolderingMetal(player)
                .filter(SolderingMetal::canUse)
                .map(metal -> {
                    if (!simulate) {
                        ModHandler.useEnergy(this.stack, this.operationEnergyCost, player);
                        metal.onUsed(player);
                    }
                    return true;
                })
                .orElse(false);
        }

        @NotNull
        @Override
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return Capabilities.SOLDERING_TOOL.orEmpty(cap, this.optional);
        }
        
        private Optional<SolderingMetal> findSolderingMetal(Player player) {
            Inventory inventory = player.getInventory();
            return IntStreamEx.range(0, inventory.getContainerSize())
                .mapToObj(inventory::getItem)
                .mapPartial(stack -> stack.getCapability(Capabilities.SOLDERING_METAL).resolve())
                .findFirst();
        }
    }
}
