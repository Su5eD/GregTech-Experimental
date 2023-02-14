package dev.su5ed.gtexperimental.item;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.api.util.DataOrbSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class DataOrbItem extends ResourceItem {

    public DataOrbItem() {
        super(new ExtendedItemProperties<>());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (player.isShiftKeyDown() && !level.isClientSide) {
            ItemStack stack = player.getItemInHand(usedHand);
            stack.setTag(null);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, usedHand);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (player != null && stack.getCount() == 1 && !level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            String dataTitle = tag.getString("dataTitle");
            DataOrbSerializable serializable = Optional.ofNullable(level.getBlockEntity(context.getClickedPos()))
                .flatMap(be -> be.getCapability(Capabilities.DATA_ORB).resolve())
                .orElse(null);

            if (serializable != null) {
                String dataName = serializable.getDataName();
                if (player.isShiftKeyDown()) {
                    if (dataTitle.equals(dataName)) {
                        CompoundTag data = tag.getCompound("data");
                        serializable.loadDataFromOrb(data);
                    }
                }
                else if (dataName != null) {
                    CompoundTag data = serializable.saveDataToOrb();
                    if (data != null) {
                        tag.putString("dataTitle", dataName);
                        tag.putString("dataName", String.valueOf(data.hashCode()));
                        tag.put("data", data);
                    }
                }

                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.FAIL;
    }
}
