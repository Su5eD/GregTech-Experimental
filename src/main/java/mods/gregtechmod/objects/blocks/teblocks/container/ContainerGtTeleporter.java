package mods.gregtechmod.objects.blocks.teblocks.container;

import mods.gregtechmod.inventory.SlotInteractive;
import mods.gregtechmod.objects.blocks.teblocks.TileEntityGtTeleporter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.world.DimensionType;

import java.util.List;
import java.util.function.BiConsumer;

public class ContainerGtTeleporter extends ContainerGtBase<TileEntityGtTeleporter> {

    public ContainerGtTeleporter(EntityPlayer player, TileEntityGtTeleporter base) {
        super(player, base);

        for (Button button : Button.VALUES) {
            int yOffset = 5 + button.ordinal() * 18;

            addSlotToContainer(new SlotInteractive(this.base, -1, 8, yOffset, clickType -> pushButton(-64, -512, clickType, button.onClick)));
            addSlotToContainer(new SlotInteractive(this.base, -1, 26, yOffset, clickType -> pushButton(-1, -16, clickType, button.onClick)));

            addSlotToContainer(new SlotInteractive(this.base, -1, 134, yOffset, clickType -> pushButton(1, 16, clickType, button.onClick)));
            addSlotToContainer(new SlotInteractive(this.base, -1, 152, yOffset, clickType -> pushButton(64, 512, clickType, button.onClick)));
        }
    }
    
    private enum Button {
        X((te, i) -> te.targetPos = te.targetPos.add(i, 0, 0)),
        Y((te, i) -> te.targetPos = te.targetPos.add(0, i, 0)),
        Z((te, i) -> te.targetPos = te.targetPos.add(0, 0, i)),
        DIM((te, i) -> {
            DimensionType[] types = DimensionType.values();
            int idx = (te.targetDimension.ordinal() + i) % types.length;
            te.targetDimension = types[idx < 0 ? types.length + idx : idx];
        });
        
        private static final Button[] VALUES = values();
        
        private final BiConsumer<TileEntityGtTeleporter, Integer> onClick;

        Button(BiConsumer<TileEntityGtTeleporter, Integer> onClick) {
            this.onClick = onClick;
        }
    }
    
    private void pushButton(int min, int max, ClickType clickType, BiConsumer<TileEntityGtTeleporter, Integer> consumer) {
        consumer.accept(this.base, clickType == ClickType.QUICK_MOVE ? max : min);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("hasEgg");
    }
}
