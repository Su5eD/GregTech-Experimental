package dev.su5ed.gtexperimental.item;

import com.google.common.collect.ImmutableMap;
import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.compat.ThermalHandler;
import dev.su5ed.gtexperimental.object.Miscellaneous;
import dev.su5ed.gtexperimental.util.GtLocale;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ColorSprayItem extends ResourceItem {
    private static final Map<DyeColor, Block> COLOR_WOOLS = ImmutableMap.<DyeColor, Block>builder()
        .put(DyeColor.WHITE, Blocks.WHITE_WOOL)
        .put(DyeColor.ORANGE, Blocks.ORANGE_WOOL)
        .put(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL)
        .put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL)
        .put(DyeColor.YELLOW, Blocks.YELLOW_WOOL)
        .put(DyeColor.LIME, Blocks.LIME_WOOL)
        .put(DyeColor.PINK, Blocks.PINK_WOOL)
        .put(DyeColor.GRAY, Blocks.GRAY_WOOL)
        .put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL)
        .put(DyeColor.CYAN, Blocks.CYAN_WOOL)
        .put(DyeColor.PURPLE, Blocks.PURPLE_WOOL)
        .put(DyeColor.BLUE, Blocks.BLUE_WOOL)
        .put(DyeColor.BROWN, Blocks.BROWN_WOOL)
        .put(DyeColor.GREEN, Blocks.GREEN_WOOL)
        .put(DyeColor.RED, Blocks.RED_WOOL)
        .put(DyeColor.BLACK, Blocks.BLACK_WOOL)
        .build();

    private final DyeColor color;
    private final Block replacement;

    public ColorSprayItem(DyeColor color) {
        super(new ExtendedItemProperties<>()
            .durability(512)
            .description(GtLocale.itemDescriptionKey("color_spray")));

        this.color = color;
        this.replacement = COLOR_WOOLS.get(this.color);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack stack) {
        ItemStack copy = stack.copy();
        return copy.hurt(8, GtUtil.RANDOM, null) ? Miscellaneous.EMPTY_SPRAY_CAN.getItemStack() : copy;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        super.appendHoverText(stack, level, components, isAdvanced);

        components.add(GtLocale.itemKey("color_spray", "description_dyeing")
            .toComponent(stack.getMaxDamage())
            .withStyle(ChatFormatting.GRAY)
            .append(" ")
            .append(Component.translatable("color.minecraft." + this.color.getName())));

        components.add(GtLocale.itemKey("color_spray", "description_crafting")
            .toComponent(stack.getMaxDamage() / 8)
            .withStyle(ChatFormatting.GRAY));
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        Block value = COLOR_WOOLS.containsValue(block) && block != this.replacement ? this.replacement
            : ModHandler.thermalLoaded && ThermalHandler.canColorRockwool(block, this.color) ? ThermalHandler.getRockWool(this.color)
            : null;
        if (value != null && GtUtil.hurtStack(stack, 1, context.getPlayer())) {
            if (!level.isClientSide) level.setBlockAndUpdate(pos, value.defaultBlockState());
            return InteractionResult.SUCCESS;
        }
        return super.onItemUseFirst(stack, context);
    }
}
