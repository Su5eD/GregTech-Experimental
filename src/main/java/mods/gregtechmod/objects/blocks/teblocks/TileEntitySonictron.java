package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.IHasGui;
import ic2.core.block.invslot.InvSlot;
import ic2.core.util.StackUtil;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.IDataOrbSerializable;
import mods.gregtechmod.api.util.SonictronSound;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiSonictron;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityAutoNBT;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerSonictron;
import mods.gregtechmod.util.nbt.NBTPersistent;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

public class TileEntitySonictron extends TileEntityAutoNBT implements IHasGui, IDataOrbSerializable {
    private static final Map<Integer, String> RECORD_NAMES = new HashMap<>();
    
    @NBTPersistent
    public int currentIndex = -1;
    public final InvSlot content;

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

    public TileEntitySonictron() {
        this.content = new InvSlot(this, "content", InvSlot.Access.NONE, 64);
        this.content.setStackSizeLimit(24);
    }

    @Override
    protected void updateEntityServer() {
        if (this.world.getRedstonePowerFromNeighbors(pos) > 0) {
            if (this.currentIndex < 0) this.currentIndex = 0;
        }

        if (this.world.getTotalWorldTime() % 2 == 0 && this.currentIndex > -1) {
            this.setActive(true);
            
            doSonictronSound(this.content.get(currentIndex), this.world, this.pos);
            
            if (++this.currentIndex > 63) this.currentIndex = -1;
        } else this.setActive(false);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("currentIndex");
    }

    @Override
    public String getDataName() {
        return "Sonictron-Data";
    }

    @Nullable
    @Override
    public NBTTagCompound saveDataToOrb() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.content.writeToNbt(nbt);
        return nbt;
    }

    @Override
    public void loadDataFromOrb(NBTTagCompound nbt) {
        this.content.readFromNbt(nbt);
    }

    @Override
    public ContainerSonictron getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSonictron(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiSonictron(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}

    public static void loadSonictronSounds() {
        GregTechMod.LOGGER.info("Loading Sonictron sounds");
        Collection<SonictronSound> sounds = Arrays.asList(
                new SonictronSound("block.note.harp", Blocks.IRON_BLOCK, 25),
                new SonictronSound("block.note.pling", Blocks.GOLD_BLOCK, 25),
                new SonictronSound("block.note.basedrum", Blocks.STONE, 25),
                new SonictronSound("block.note.bass", Blocks.LOG, 25),
                new SonictronSound("block.note.hat", Blocks.GLASS, 25),
                new SonictronSound("block.note.snare", Blocks.SAND, 25),
                new SonictronSound("record.", Items.RECORD_CAT, 12),
                new SonictronSound("entity.generic.explode", Blocks.TNT, 3),
                new SonictronSound("block.fire.ambient", Items.FIRE_CHARGE),
                new SonictronSound("item.flintandsteel.use", Items.FLINT_AND_STEEL),
                new SonictronSound("block.lava.pop", Items.LAVA_BUCKET),
                new SonictronSound("block.water.ambient", Items.WATER_BUCKET),
                new SonictronSound("entity.generic.splash", Items.BUCKET),
                new SonictronSound("block.portal.trigger", Blocks.END_PORTAL_FRAME),
                new SonictronSound("block.glass.break", Blocks.GLASS_PANE),
                new SonictronSound("entity.experience_orb.pickup", Items.ENDER_PEARL),
                new SonictronSound("entity.player.levelup", Items.ENDER_EYE),
                new SonictronSound("ui.button.click", Blocks.STONE_BUTTON),
                new SonictronSound("entity.player.big_fall", Blocks.COBBLESTONE),
                new SonictronSound("entity.player.small_fall", Blocks.DIRT),
                new SonictronSound("entity.arrow.shoot", Items.ARROW),
                new SonictronSound("entity.arrow.hit", Items.FISHING_ROD),
                new SonictronSound("entity.item.break", Items.IRON_SHOVEL),
                new SonictronSound("entity.generic.drink", Items.POTIONITEM),
                new SonictronSound("entity.player.burp", Items.GLASS_BOTTLE),
                new SonictronSound("block.chest.open", Blocks.ENDER_CHEST),
                new SonictronSound("block.chest.close", Blocks.CHEST),
                new SonictronSound("block.wooden_door.open", Items.IRON_DOOR),
                new SonictronSound("block.wooden_door.close", Items.OAK_DOOR),
                new SonictronSound("entity.generic.eat", Items.PORKCHOP),
                new SonictronSound("block.cloth.step", Blocks.WOOL),
                new SonictronSound("block.grass.step", Blocks.GRASS),
                new SonictronSound("block.gravel.step", Blocks.GRAVEL),
                new SonictronSound("block.snow.step", Blocks.SNOW),
                new SonictronSound("block.piston.extend", Blocks.PISTON),
                new SonictronSound("block.piston.contract", Blocks.STICKY_PISTON),
                new SonictronSound("ambient.cave", Blocks.MOSSY_COBBLESTONE),
                new SonictronSound("weather.rain", Blocks.LAPIS_BLOCK),
                new SonictronSound("entity.lightning.thunder", Blocks.DIAMOND_BLOCK)
                // new SonictronSound("note.bass", Blocks.PLANKS, 25)
                // new SonictronSound("block.fire.extinguish", Items.LAVA_BUCKET)
                // new SonictronSound("block.portal.ambient", Blocks.PORTAL)
                // new SonictronSound("block.portal.travel", Blocks.END_PORTAL)
                // new SonictronSound("damage.durtflesh", Items.IRON_SWORD)
                // new SonictronSound("damage.hurt", Items.DIAMOND_SWORD)
                // new SonictronSound("random.bow", Items.BOW)
                // new SonictronSound("entity.player.breath", Items.BUCKET)
        );
        GregTechAPI.instance().registerSonictronSounds(sounds);
    }

    public static void doSonictronSound(ItemStack stack, World world, BlockPos pos) {
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
            world.playRecord(pos.add(0.5, 0.5, 0.5), sound);
        } else {
            world.playSound(null, pos, sound, SoundCategory.NEUTRAL, 0.3F, pitch);
        }
    }
}
