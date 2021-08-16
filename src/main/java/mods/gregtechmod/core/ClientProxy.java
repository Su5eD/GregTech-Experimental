package mods.gregtechmod.core;

import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ClientProxy {
    private static final Map<Integer, String> RECORD_NAMES = new HashMap<>();
    
    static {
        RECORD_NAMES.put(1, "13");
        RECORD_NAMES.put(2, "cat");
        RECORD_NAMES.put(3, "blocks");
        RECORD_NAMES.put(4, "chirp");
        RECORD_NAMES.put(5, "far");
        RECORD_NAMES.put(6, "mall");
        RECORD_NAMES.put(7, "mellohi");
        RECORD_NAMES.put(8, "stal");
        RECORD_NAMES.put(9, "strad");
        RECORD_NAMES.put(10, "ward");
        RECORD_NAMES.put(11, "11");
        RECORD_NAMES.put(12, "wait");
    }

    public void playSound(SoundEvent sound, float pitch) {
        Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(sound, pitch));
    }

    public void doSonictronSound(ItemStack stack, World world, BlockPos pos) {
        if (stack.isEmpty()) return;
        
        float pitch = 1;
        String name = GregTechAPI.instance().getSonictronSounds().stream()
                .filter(sound -> StackUtil.checkItemEquality(stack, sound.item))
                .map(sound -> sound.name)
                .findFirst()
                .orElse("block.note.harp");
        int count = stack.getCount();

        if (name.startsWith("random.explode")) {
            if (count == 3) {
                name = "entity.tnt.primed";
            } else if (count == 2) {
                name = "entity.generic.explode";
            }
        }

        if (name.startsWith("record.")) {
            String suffix = RECORD_NAMES.getOrDefault(count, "wherearewenow");
            name += suffix;
        }

        if (name.startsWith("block.note.")) {
            pitch = (float) Math.pow(2D, (double) (count - 13) / 12D);
        }

        SoundEvent sound = SoundEvent.REGISTRY.getObject(new ResourceLocation(name));
        if (sound == null) throw new IllegalArgumentException("Attempted to play invalid sound " + name);

        if (name.startsWith("record.")) {
            world.playRecord(pos, sound);
        } else {
            world.playSound(null, pos, sound, SoundCategory.NEUTRAL, 0.3F, pitch);
        }
    }
}
