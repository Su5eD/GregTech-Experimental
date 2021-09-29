package mods.gregtechmod.objects.blocks.teblocks.computercube;

import ic2.api.item.IC2Items;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.GregTechObjectAPI;
import mods.gregtechmod.api.util.Reference;
import mods.gregtechmod.gui.GuiComputerCubeGuide;
import mods.gregtechmod.objects.BlockItems;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerComputerCubeGuide;
import mods.gregtechmod.util.IItemProvider;
import mods.gregtechmod.util.ProfileDelegate;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComputerCubeGuide implements IComputerCubeModule {
    private static final ResourceLocation NAME = new ResourceLocation(Reference.MODID, "guide");
    private static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID, "blocks/machines/adv_machine_screen_random");
    private static final List<GuidePage> PAGES = new ArrayList<>();
    
    @NBTPersistent
    private int currentPage;
    @NBTPersistent
    public final InvSlot displayStacks = new InvSlot(5);
    
    public static void addPage(String translationKey, int length, List<ItemStack> stacks) {
        PAGES.add(new GuidePage(translationKey, length, stacks));
    }
    
    @SuppressWarnings("unused")
    public ComputerCubeGuide(TileEntityComputerCube base) {}
    
    public GuidePage getCurrentPage() {
        return PAGES.get(this.currentPage);
    }
    
    public void nextPage() {
        this.currentPage = (this.currentPage + 1) % PAGES.size();
    }
    
    public void previousPage() {
        if (--this.currentPage < 0) this.currentPage = PAGES.size() - 1;
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
        return new ContainerComputerCubeGuide(base);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin, TileEntityComputerCube base) {
        return new GuiComputerCubeGuide(getGuiContainer(player, base));
    }

    @Nullable
    @Override
    public ResourceLocation getTexture() {
        return TEXTURE;
    }
    
    public enum Page {
        LIGHTNING_ROD(17, () -> {
            ItemStack ironFence = IC2Items.getItem("fence", "iron");
            return Stream.of(GregTechObjectAPI.getTileEntity("lightning_rod"), ironFence, ironFence, ironFence, ironFence);
        }),
        QUANTUM_CHEST(17, () -> Stream.of(GregTechObjectAPI.getTileEntity("quantum_chest"))),
        COMPUTER_CUBE(17, () -> Stream.of(GregTechObjectAPI.getTileEntity("computer_cube"))),
        UUM_ASSEMBLER(16, () -> Stream.of(/*GregTechObjectAPI.getTileEntity("uum_assembler"),*/ IC2Items.getItem("misc_resource", "matter"))),
        SONICTRON(17, () -> Stream.of(GregTechObjectAPI.getTileEntity("sonictron"), new ItemStack(BlockItems.Tool.SONICTRON_PORTABLE.getInstance()))),
        LESU(17, () -> Stream.of(GregTechObjectAPI.getTileEntity("lesu"), new ItemStack(BlockItems.Block.LESUBLOCK.getBlockInstance()))),
        IDSU(15, () -> Stream.of(GregTechObjectAPI.getTileEntity("idsu"))),
        AESU(10, () -> Stream.of(GregTechObjectAPI.getTileEntity("aesu"))),
        CHARGE_O_MAT(16, Stream::empty /*() -> Stream.of(GregTechObjectAPI.getTileEntity("charge_o_mat"))*/),
        INDUSTRIAL_CENTRIFUGE(17, () -> Stream.of(GregTechObjectAPI.getTileEntity("industrial_centrifuge"))),
        INDUSTRIAL_ELECTROLYZER(16, () -> Stream.of(GregTechObjectAPI.getTileEntity("industrial_electrolyzer"))),
        INDUSTRIAL_GRINDER(17, () -> Stream.of(
                GregTechObjectAPI.getTileEntity("industrial_grinder"), 
                new ItemStack(BlockItems.Block.STANDARD_MACHINE_CASING.getBlockInstance()), 
                new ItemStack(BlockItems.Block.REINFORCED_MACHINE_CASING.getBlockInstance()), new ItemStack(Blocks.WATER))
        ),
        INDUSTRIAL_BLAST_FURNACE(17, () -> Stream.of(GregTechObjectAPI.getTileEntity("industrial_blast_furnace"))),
        INDUSTRIAL_SAWMILL(17, () -> Stream.of(
                GregTechObjectAPI.getTileEntity("industrial_sawmill"), 
                new ItemStack(BlockItems.Dust.WOOD.getInstance()), 
                new ItemStack(BlockItems.Plate.WOOD.getInstance()))
        ),
        IMPLOSION_COMPRESSOR(17, () -> Stream.of(GregTechObjectAPI.getTileEntity("implosion_compressor"), IC2Items.getItem("te", "itnt"))),
        SUPERCONDUCTOR(17, () -> Stream.of(
                GregTechObjectAPI.getTileEntity("supercondensator"), 
                GregTechObjectAPI.getTileEntity("superconductor_wire"),
                new ItemStack(BlockItems.Component.SUPERCONDUCTOR.getInstance())
        )),
        PLAYER_DETECTOR(17, Stream::empty /*() -> Stream.of(GregTechObjectAPI.getTileEntity("player_detector")*/),
        MATTER_FABRICATOR(14, () -> Stream.of(/*GregTechObjectAPI.getTileEntity("gt_matter_fabricator"),*/ IC2Items.getItem("misc_resource", "matter"))),
        ELECTRIC_AUTOCRAFTING(17, Stream::empty /*() -> Stream.of(GregTechObjectAPI.getTileEntity("electric_autocrafting"))*/),
        AUTOMATION(17, Stream::empty /*() -> Stream.of(GregTechObjectAPI.getTileEntity("electric_translocator"), GregTechObjectAPI.getTileEntity("electric_small_buffer"), GregTechObjectAPI.getTileEntity("electric_large_buffer"))*/),
        SILVER_ORE(5, () -> itemStackStream(BlockItems.Ore.GALENA, BlockItems.Ingot.SILVER, BlockItems.Dust.SILVER)),
        SAPPHIRES_AND_RUBIES(15, () -> itemStackStream(BlockItems.Ore.SAPPHIRE, BlockItems.Miscellaneous.SAPPHIRE, BlockItems.Ore.RUBY, BlockItems.Miscellaneous.RUBY)),
        BAUXITE_ORE(17, () -> itemStackStream(BlockItems.Ore.BAUXITE, BlockItems.Dust.BAUXITE, BlockItems.Dust.ALUMINIUM, BlockItems.Ingot.ALUMINIUM, BlockItems.Block.ALUMINIUM)),
        TITANIUM(10, () -> itemStackStream(BlockItems.Ore.BAUXITE, BlockItems.Dust.BAUXITE, BlockItems.Dust.TITANIUM, BlockItems.Ingot.TITANIUM, BlockItems.Block.TITANIUM)),
        IRIDIUM_ORE(14, () -> Stream.of(
                BlockItems.Ore.IRIDIUM.getItemStack(),
                IC2Items.getItem("misc_resource", "iridium_ore"),
                IC2Items.getItem("crafting", "iridium")
        )),
        HELIUM_COOLANT_CELL(7, () -> Stream.of(
                BlockItems.NuclearCoolantPack.COOLANT_HELIUM_360K.getItemStack(),
                BlockItems.NuclearCoolantPack.COOLANT_HELIUM_180K.getItemStack(),
                BlockItems.NuclearCoolantPack.COOLANT_HELIUM_60K.getItemStack(),
                ProfileDelegate.getCell("helium"),
                ProfileDelegate.getCell("helium3")
        )),
        DESTRUCTORPACK(5, () -> itemStackStream(BlockItems.Tool.DESTRUCTORPACK)),
        DATA_ORBS(10, () -> itemStackStream(BlockItems.Component.DATA_ORB)),
        ENERGY_ORBS(8, () -> itemStackStream(BlockItems.Tool.LAPOTRONIC_ENERGY_ORB, BlockItems.Armor.LAPOTRONPACK)),
        IRIDIUM_NEUTRON_REFLECTOR(17, () -> Stream.of(IC2Items.getItem("iridium_reflector"))),
        ROCK_CUTTER(16, () -> itemStackStream(BlockItems.Tool.ROCK_CUTTER)),
        TESLA_STAFF(13, () -> itemStackStream(BlockItems.Tool.TESLA_STAFF));
        
        private final String translationKey;
        private final int length;
        private final Supplier<Stream<ItemStack>> stacks;

        Page(int length, Supplier<Stream<ItemStack>> stacks) {
            this.translationKey = Reference.MODID + "." + "computercube.desc." + name().toLowerCase(Locale.ROOT);
            this.length = length;
            this.stacks = stacks;
        }
        
        private static Stream<ItemStack> itemStackStream(IItemProvider... providers) {
            return Stream.of(providers)
                    .map(IItemProvider::getItemStack);
        }
        
        public static void register() {
            for (Page page : values()) {
                List<ItemStack> stacks = page.stacks.get()
                                .limit(5)
                        .collect(Collectors.toList());
                addPage(page.translationKey, page.length, stacks);
            }
        }
    }
    
    public static class GuidePage {
        public final String translationKey;
        public final int length;
        public final List<ItemStack> stacks;

        public GuidePage(String translationKey, int length, List<ItemStack> stacks) {
            if (length < 1) throw new IllegalArgumentException("Page must not be empty");
            
            this.translationKey = translationKey;
            this.length = length;
            this.stacks = Collections.unmodifiableList(stacks);
        }
    }
}
