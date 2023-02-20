package dev.su5ed.gtexperimental;

import dev.su5ed.gtexperimental.api.util.SonictronSound;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GregTechIMC {
    public static final String IMC_ADD_SONICTRON_SOUND = "addSonictronSound";
    public static final GregTechIMC INSTANCE = new GregTechIMC();

    private final List<SonictronSound> sonictronSounds = new ArrayList<>();

    private GregTechIMC() {}

    void processIMC(final InterModProcessEvent event) {
        event.getIMCStream(s -> s.equals(IMC_ADD_SONICTRON_SOUND))
            .map(message -> (SonictronSound) message.messageSupplier().get())
            .forEach(this::addSonictronSound);
    }

    public void addSonictronSound(SonictronSound sound) {
        this.sonictronSounds.add(sound);
    }

    public List<SonictronSound> getSonictronSounds() {
        return Collections.unmodifiableList(this.sonictronSounds);
    }

    public String getSoundFor(Item item) {
        return StreamEx.of(this.sonictronSounds)
            .filter(sound -> item == sound.item().asItem())
            .map(SonictronSound::name)
            .findFirst()
            .orElse("block.note.harp");
    }
}
