package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.Capabilities;
import dev.su5ed.gregtechmod.api.util.DataOrbSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DataOrbItem extends ResourceItem {

    public DataOrbItem() {
        super(new ExtendedItemProperties<>());
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (player != null && stack.getCount() == 1 && !level.isClientSide) {
            BlockEntity be = level.getBlockEntity(context.getClickedPos());
            CompoundTag tag = stack.getOrCreateTag();
            String dataTitle = tag.getString("dataTitle");
            DataOrbSerializable serializable = be.getCapability(Capabilities.DATA_ORB).orElse(null);

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

        return super.useOn(context);
    }
}
