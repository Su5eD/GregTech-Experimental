package mods.gregtechmod.core;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.init.ClientEventHandler;
import mods.gregtechmod.util.IProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ClientProxy implements IProxy {

    @Override
    public void init() {
        ClientEventHandler.gatherModItems();
    }

    @Override
    public void playSound(SoundEvent sound, float pitch) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(sound, pitch));
    }

    @Override
    public void doSonictronSound(ItemStack stack, World world, BlockPos pos) {
        float pitch = 1.0F;
        String name = "block.note.harp";

        if (stack.isEmpty()) return;

        for (int i = 0; i < GregTechAPI.SONICTRON_SOUNDS.size(); i++) {
            if (StackUtil.checkItemEquality(stack, GregTechAPI.SONICTRON_SOUNDS.get(i).item)) {
                name = GregTechAPI.SONICTRON_SOUNDS.get(i).name;
                break;
            }
        }

        if (name.startsWith("random.explode")) {
            if (stack.getCount()==3) {
                name = "entity.tnt.primed";
            } else if (stack.getCount()==2) {
                name = "entity.generic.explode";
            }
        }

        if (name.startsWith("record.")) {
            switch (stack.getCount()) {
                case  1:
                    name += "13";
                    break;
                case  2:
                    name += "cat";
                    break;
                case  3:
                    name += "blocks";
                    break;
                case  4:
                    name += "chirp";
                    break;
                case  5:
                    name += "far";
                    break;
                case  6:
                    name += "mall";
                    break;
                case  7:
                    name += "mellohi";
                    break;
                case  8:
                    name += "stal";
                    break;
                case  9:
                    name += "strad";
                    break;
                case 10:
                    name += "ward";
                    break;
                case 11:
                    name += "11";
                    break;
                case 12:
                    name += "wait";
                    break;
                default:
                    name += "wherearewenow";
                    break;
            }
        }

        if (name.startsWith("block.note.")) {
            pitch = (float)Math.pow(2.0D, (double)(stack.getCount() - 13) / 12.0D);
        }

        SoundEvent sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(name));
        if (sound == null) throw new IllegalArgumentException("Attempted to play invalid sound "+name);

        if (name.startsWith("record.")) {
            world.playRecord(pos, sound);
        } else {
            world.playSound(null, pos, sound, SoundCategory.NEUTRAL, 0.3F, pitch);
        }
    }
}
