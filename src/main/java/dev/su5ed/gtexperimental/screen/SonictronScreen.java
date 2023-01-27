package dev.su5ed.gtexperimental.screen;

import dev.su5ed.gtexperimental.menu.SonictronMenu;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SonictronScreen extends BlockEntityScreen<SonictronMenu> {
    public static final ResourceLocation BACKGROUND = GtUtil.guiTexture("sonictron");
    
    public SonictronScreen(SonictronMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title, BACKGROUND);
    }
}