package dev.su5ed.gtexperimental.screen;

import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.util.GtUtil;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.function.Function;

public class SimpleMachineScreen extends BlockEntityScreen<SimpleMachineMenu> {
    public static final Function<String, ResourceLocation> BACKGROUNDS = Util.memoize(GtUtil::guiTexture);

    private final RecipeProgressBar progressBar;

    public static ResourceLocation background(String name) {
        return BACKGROUNDS.apply(name);
    }

    public SimpleMachineScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title, ResourceLocation background, RecipeProgressBar progressBar) {
        super(menu, playerInventory, title, background);

        this.progressBar = progressBar;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new ProgressBarWidget(this.leftPos + 78, this.topPos + 24, this.progressBar, this::getProgressRatio));
        addRenderableWidget(IconCycle.createVertical(this.leftPos + 7, this.topPos + 62, 58, () -> this.menu.blockEntity.provideEnergy));
        addRenderableWidget(IconCycle.createVertical(this.leftPos + 25, this.topPos + 62, 76, () -> this.menu.blockEntity.autoOutput));
        addRenderableWidget(IconCycle.createVertical(this.leftPos + 43, this.topPos + 62, 94, () -> this.menu.blockEntity.getMachineController().isStrictInputSides()));
    }

    private double getProgressRatio() {
        return this.menu.blockEntity.recipeHandler.getProgress() / (double) Math.max(this.menu.blockEntity.recipeHandler.getMaxProgress(), 1);
    }

    public static class AutomaticMaceratorScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("auto_macerator");

        public AutomaticMaceratorScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.MACERATING);
        }
    }

    public static class AutomaticExtractorScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("auto_extractor");

        public AutomaticExtractorScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.EXTRACTING);
        }
    }

    public static class AutomaticCompressorScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("auto_compressor");

        public AutomaticCompressorScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.COMPRESSING);
        }
    }

    public static class AutomaticRecyclerScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("auto_recycler");

        public AutomaticRecyclerScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.RECYCLING);
        }
    }

    public static class AutomaticElectricFurnaceScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("auto_electric_furnace");

        public AutomaticElectricFurnaceScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.SMELTING);
        }
    }

    public static class WiremillScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("wiremill");

        public WiremillScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.EXTRUDING);
        }
    }

    public static class BenderScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("bender");

        public BenderScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.BENDING);
        }
    }

    public static class AlloySmelterScreen extends SimpleMachineScreen {
        public AlloySmelterScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, AutomaticElectricFurnaceScreen.BACKGROUND, RecipeProgressBar.SMELTING);
        }
    }

    public static class AssemblerScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("assembler");

        public AssemblerScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.ASSEMBLING);
        }
    }

    public static class AutoCannerScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("auto_canner");

        public AutoCannerScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.CANNING);
        }
    }

    public static class LatheScreen extends SimpleMachineScreen {
        public static final ResourceLocation BACKGROUND = background("lathe");

        public LatheScreen(SimpleMachineMenu menu, Inventory playerInventory, Component title) {
            super(menu, playerInventory, title, BACKGROUND, RecipeProgressBar.TURNING);
        }
    }
}
