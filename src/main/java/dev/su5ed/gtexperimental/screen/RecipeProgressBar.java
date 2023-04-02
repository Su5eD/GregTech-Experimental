package dev.su5ed.gtexperimental.screen;

import net.minecraft.resources.ResourceLocation;

public enum RecipeProgressBar {
    MACERATING(SimpleMachineScreen.AutomaticMaceratorScreen.BACKGROUND),
    EXTRACTING(SimpleMachineScreen.AutomaticExtractorScreen.BACKGROUND),
    COMPRESSING(SimpleMachineScreen.AutomaticCompressorScreen.BACKGROUND),
    RECYCLING(SimpleMachineScreen.AutomaticRecyclerScreen.BACKGROUND),
    SMELTING(SimpleMachineScreen.AutomaticElectricFurnaceScreen.BACKGROUND),
    EXTRUDING(SimpleMachineScreen.WiremillScreen.BACKGROUND),
    BENDING(SimpleMachineScreen.BenderScreen.BACKGROUND),
    ASSEMBLING(SimpleMachineScreen.AssemblerScreen.BACKGROUND),
    CANNING(SimpleMachineScreen.AutoCannerScreen.BACKGROUND);

    public final ResourceLocation texture;
    public final int x;
    public final int y;
    public final int width;
    public final int height;
    public final Direction direction;

    RecipeProgressBar(ResourceLocation texture) {
        this(texture, 18);
    }

    RecipeProgressBar(ResourceLocation texture, int height) {
        this(texture, 176, 20, height, Direction.RIGHT);
    }

    RecipeProgressBar(ResourceLocation texture, int x, int width, int height, Direction direction) {
        this(texture, x, 0, width, height, direction);
    }

    RecipeProgressBar(ResourceLocation texture, int x, int y, int width, int height, Direction direction) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.direction = direction;
    }

    public enum Direction {
        DOWN, UP, LEFT, RIGHT
    }
}
