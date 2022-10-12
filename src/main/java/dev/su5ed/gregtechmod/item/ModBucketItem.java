package dev.su5ed.gregtechmod.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class ModBucketItem extends BucketItem {
    private final MutableComponent description;
    
    public ModBucketItem(Supplier<? extends Fluid> supplier, MutableComponent description, Properties builder) {
        super(supplier, builder);
        
        this.description = description != null ? description.withStyle(ChatFormatting.GRAY) : null;
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new FluidBucketWrapper(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (this.description != null) {
            tooltipComponents.add(this.description);
        }
    }
}
