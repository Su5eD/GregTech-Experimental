package dev.su5ed.gregtechmod.item;

import dev.su5ed.gregtechmod.api.cover.ICoverProvider;
import net.minecraft.network.chat.MutableComponent;

public class CoverItem extends ResourceItem {
    private final ICoverProvider coverProvider;

    public CoverItem(Properties pProperties, MutableComponent description, ICoverProvider coverProvider) {
        super(pProperties, description);
        
        this.coverProvider = coverProvider;
    }
}
