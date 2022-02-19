package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.api.util.IDataOrbSerializable;
import dev.su5ed.gregtechmod.object.ModObjects;
import ic2.core.util.StackUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class DataOrbItem extends ResourceItem {

    public DataOrbItem() {
        super(ModObjects.DEFAULT_ITEM_PROPERTIES);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getItemInHand();
        Level level = context.getLevel();
        Player player = context.getPlayer();
        
        if (player != null && stack.getCount() == 1 && !level.isClientSide) {
            BlockEntity te = level.getBlockEntity(context.getClickedPos());
            CompoundTag tag = StackUtil.getOrCreateNbtData(stack);
            String dataTitle = tag.getString("dataTitle");

            if (te instanceof IDataOrbSerializable serializable) {
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
