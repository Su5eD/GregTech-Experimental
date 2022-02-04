package mods.gregtechmod.objects.blocks.teblocks.computercube;

import ic2.api.item.IC2Items;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.compat.ModHandler;
import mods.gregtechmod.gui.GuiComputerCubeGuide;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeGuide;
import mods.gregtechmod.util.GtLocale;
import mods.gregtechmod.util.IItemProvider;
import mods.gregtechmod.util.ProfileDelegate;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import one.util.streamex.StreamEx;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;

public class ComputerCubeGuide implements IComputerCubeModule {
    private static final ResourceLocation NAME = new ResourceLocation(Reference.MODID, "guide");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "blocks/machines/adv_machine_screen_random");
    private static final List<GuidePage> PAGES = new ArrayList<>();

    @NBTPersistent
    private int currentPage;
    public final InvSlot displayStacks;

    public static void addPage(String translationKey, int length, List<ItemStack> stacks) {
        PAGES.add(new GuidePage(translationKey, length, stacks));
    }

    public ComputerCubeGuide(TileEntityComputerCube base) {
        this.displayStacks = new InvSlot(base, "display", InvSlot.Access.NONE, 5);
    }

    public GuidePage getCurrentPage() {
        return PAGES.get(this.currentPage);
    }

    public void nextPage() {
        switchPage(1);
    }

    public void previousPage() {
        switchPage(-1);
    }

    public void switchPage(int step) {
        this.currentPage = (PAGES.size() + this.currentPage + step) % PAGES.size();
    }

    @Override
    public ResourceLocation getName() {
        return NAME;
    }

    @Override
    public boolean updateServer() {
        return false;
    }

    @Override
    public ContainerComputerCubeGuide getGuiContainer(EntityPlayer player, TileEntityComputerCube base) {
        return new ContainerComputerCubeGuide(player, base);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin, TileEntityComputerCube base) {
        return new GuiComputerCubeGuide(getGuiContainer(player, base));
    }

    @Nullable
    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }

    public enum Page { // TODO
        LIGHTNING_ROD(17, () -> {
            ItemStack ironFence = IC2Items.getItem("fence", "iron");
            return StreamEx.of(GregTechObjectAPI.getTileEntity("lightning_rod"), ironFence, ironFence, ironFence, ironFence);
        }),
        QUANTUM_CHEST(17, "quantum_chest"),
        COMPUTER_CUBE(17, "computer_cube"),
        UUM_ASSEMBLER(16, () -> StreamEx.of(/*GregTechObjectAPI.getTileEntity("uum_assembler"),*/ ModHandler.uuMatter)),
        SONICTRON(17, () -> StreamEx.of(GregTechObjectAPI.getTileEntity("sonictron"), BlockItems.Tool.SONICTRON_PORTABLE.getItemStack())),
        LESU(17, () -> StreamEx.of(GregTechObjectAPI.getTileEntity("lesu"), BlockItems.Block.LESUBLOCK.getItemStack())),
        IDSU(15, "idsu"),
        AESU(10, "aesu"),
        CHARGE_O_MAT(16, "charge_o_mat"),
        INDUSTRIAL_CENTRIFUGE(17, "industrial_centrifuge"),
        INDUSTRIAL_ELECTROLYZER(16, "industrial_electrolyzer"),
        INDUSTRIAL_GRINDER(17, () -> StreamEx.of(
            GregTechObjectAPI.getTileEntity("industrial_grinder"),
            BlockItems.Block.STANDARD_MACHINE_CASING.getItemStack(),
            BlockItems.Block.REINFORCED_MACHINE_CASING.getItemStack(),
            new ItemStack(Blocks.WATER)
        )),
        INDUSTRIAL_BLAST_FURNACE(17, "industrial_blast_furnace"),
        INDUSTRIAL_SAWMILL(17, () -> StreamEx.of(
            GregTechObjectAPI.getTileEntity("industrial_sawmill"),
            BlockItems.Dust.WOOD.getItemStack(),
            BlockItems.Plate.WOOD.getItemStack()
        )),
        IMPLOSION_COMPRESSOR(17, () -> StreamEx.of(GregTechObjectAPI.getTileEntity("implosion_compressor"), IC2Items.getItem("te", "itnt"))),
        SUPERCONDUCTOR(17, () -> StreamEx.of(
            GregTechObjectAPI.getTileEntity("supercondensator"),
            GregTechObjectAPI.getTileEntity("superconductor_wire"),
            BlockItems.Component.SUPERCONDUCTOR.getItemStack()
        )),
        PLAYER_DETECTOR(17, StreamEx::empty /*() -> Stream.of(GregTechObjectAPI.getTileEntity("player_detector")*/),
        MATTER_FABRICATOR(14, () -> StreamEx.of(GregTechObjectAPI.getTileEntity("matter_fabricator"), ModHandler.uuMatter)),
        ELECTRIC_AUTOCRAFTING(17, "electric_crafting_table"),
        AUTOMATION(17, () -> StreamEx.of("electric_translocator", "electric_buffer_small", "electric_buffer_large").map(GregTechObjectAPI::getTileEntity)),
        SILVER_ORE(5, BlockItems.Ore.GALENA, BlockItems.Ingot.SILVER, BlockItems.Dust.SILVER),
        SAPPHIRES_AND_RUBIES(15, BlockItems.Ore.SAPPHIRE, BlockItems.Miscellaneous.SAPPHIRE, BlockItems.Ore.RUBY, BlockItems.Miscellaneous.RUBY),
        BAUXITE_ORE(17, BlockItems.Ore.BAUXITE, BlockItems.Dust.BAUXITE, BlockItems.Dust.ALUMINIUM, BlockItems.Ingot.ALUMINIUM, BlockItems.Block.ALUMINIUM),
        TITANIUM(10, BlockItems.Ore.BAUXITE, BlockItems.Dust.BAUXITE, BlockItems.Dust.TITANIUM, BlockItems.Ingot.TITANIUM, BlockItems.Block.TITANIUM),
        IRIDIUM_ORE(14, () -> StreamEx.of(
            BlockItems.Ore.IRIDIUM.getItemStack(),
            IC2Items.getItem("misc_resource", "iridium_ore"),
            IC2Items.getItem("crafting", "iridium")
        )),
        HELIUM_COOLANT_CELL(7, () -> StreamEx.of(
            BlockItems.NuclearCoolantPack.COOLANT_HELIUM_360K.getItemStack(),
            BlockItems.NuclearCoolantPack.COOLANT_HELIUM_180K.getItemStack(),
            BlockItems.NuclearCoolantPack.COOLANT_HELIUM_60K.getItemStack(),
            ProfileDelegate.getCell("helium"),
            ProfileDelegate.getCell("helium3")
        )),
        DESTRUCTORPACK(5, BlockItems.Tool.DESTRUCTORPACK),
        DATA_ORBS(10, BlockItems.Component.DATA_ORB),
        ENERGY_ORBS(8, BlockItems.Tool.LAPOTRONIC_ENERGY_ORB, BlockItems.Armor.LAPOTRONPACK),
        IRIDIUM_NEUTRON_REFLECTOR(17, () -> StreamEx.of(IC2Items.getItem("iridium_reflector"))),
        ROCK_CUTTER(16, BlockItems.Tool.ROCK_CUTTER),
        TESLA_STAFF(13, BlockItems.Tool.TESLA_STAFF);

        private final String translationKey;
        private final int length;
        private final Supplier<StreamEx<ItemStack>> stacks;

        Page(int length, String... teBlocks) {
            this(length, () -> StreamEx.of(teBlocks).map(GregTechObjectAPI::getTileEntity));
        }

        Page(int length, IItemProvider... providers) {
            this(length, () -> StreamEx.of(providers).map(IItemProvider::getItemStack));
        }

        Page(int length, Supplier<StreamEx<ItemStack>> stacks) {
            this.translationKey = GtLocale.buildKey("computercube", "desc", name().toLowerCase(Locale.ROOT));
            this.length = length;
            this.stacks = stacks;
        }

        public static void register() {
            StreamEx.of(values())
                .mapToEntry(page -> page.stacks.get()
                    .limit(5)
                    .toImmutableList())
                .forKeyValue((page, stacks) -> addPage(page.translationKey, page.length, stacks));
        }
    }

    public static class GuidePage {
        public final String translationKey;
        public final int length;
        public final List<ItemStack> stacks;

        public GuidePage(String translationKey, int length, List<ItemStack> stacks) {
            if (length < 1) throw new IllegalArgumentException("Page is empty");

            this.translationKey = translationKey;
            this.length = length;
            this.stacks = stacks;
        }
    }
}
