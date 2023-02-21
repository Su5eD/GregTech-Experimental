package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.Capabilities;
import dev.su5ed.gtexperimental.GregTechIMC;
import dev.su5ed.gtexperimental.GregTechMod;
import dev.su5ed.gtexperimental.api.util.DataOrbSerializable;
import dev.su5ed.gtexperimental.api.util.FriendlyCompoundTag;
import dev.su5ed.gtexperimental.api.util.SonictronSound;
import dev.su5ed.gtexperimental.blockentity.base.InventoryBlockEntity;
import dev.su5ed.gtexperimental.menu.SonictronMenu;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.util.inventory.InventorySlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.ForgeRegistries;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SonictronBlockEntity extends InventoryBlockEntity implements MenuProvider, DataOrbSerializable {
    private static final Map<Integer, String> RECORD_NAMES = EntryStream.of(
            1, "13",
            2, "cat",
            3, "blocks",
            4, "chirp",
            5, "far",
            6, "mall",
            7, "mellohi",
            8, "stal",
            9, "strad",
            10, "ward"
        )
        .append(11, "11")
        .append(12, "wait")
        .toMap();

    private final LazyOptional<DataOrbSerializable> dataOrbOptional = LazyOptional.of(() -> this);
    public final InventorySlot content;
    public int currentIndex = -1;

    public SonictronBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.SONICTRON, pos, state);

        this.content = addSlot("content", InventorySlot.Mode.NONE, 64);
    }

    @Override
    public void tickServer() {
        super.tickServer();

        if (isRedstonePowered() && this.currentIndex < 0) {
            this.currentIndex = 0;
        }

        setActive(this.currentIndex > -1);

        if (getTicks() % 2 == 0 && this.currentIndex > -1) {
            doSonictronSound(this.content.get(this.currentIndex), this.level, this.worldPosition);

            if (++this.currentIndex > 63) {
                this.currentIndex = -1;
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (cap == Capabilities.DATA_ORB) {
            return this.dataOrbOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public String getDataName() {
        return "Sonictron-Data";
    }

    @Override
    public CompoundTag saveDataToOrb() {
        return this.inventoryHandler.save();
    }

    @Override
    public void loadDataFromOrb(CompoundTag tag) {
        this.inventoryHandler.load(tag); // TODO TEST
    }

    public static void loadSonictronSounds() {
        GregTechMod.LOGGER.info("Loading Sonictron sounds");
        StreamEx.of(
            new SonictronSound(SoundEvents.NOTE_BLOCK_HARP, Blocks.IRON_BLOCK, 25),
            new SonictronSound(SoundEvents.NOTE_BLOCK_PLING, Blocks.GOLD_BLOCK, 25),
            new SonictronSound(SoundEvents.NOTE_BLOCK_BASEDRUM, Blocks.STONE, 25),
            new SonictronSound(SoundEvents.NOTE_BLOCK_BASS, Blocks.OAK_LOG, 25),
            new SonictronSound(SoundEvents.NOTE_BLOCK_HAT, Blocks.GLASS, 25),
            new SonictronSound(SoundEvents.NOTE_BLOCK_SNARE, Blocks.SAND, 25),
//            new SonictronSound("music_disc.", Items.MUSIC_DISC_CAT, 12), TODO Music discs
            new SonictronSound(SoundEvents.GENERIC_EXPLODE, Blocks.TNT, 3),
            new SonictronSound(SoundEvents.FIRE_AMBIENT, Items.FIRE_CHARGE),
            new SonictronSound(SoundEvents.FLINTANDSTEEL_USE, Items.FLINT_AND_STEEL),
            new SonictronSound(SoundEvents.LAVA_POP, Items.LAVA_BUCKET),
            new SonictronSound(SoundEvents.WATER_AMBIENT, Items.WATER_BUCKET),
            new SonictronSound(SoundEvents.GENERIC_SPLASH, Items.BUCKET),
            new SonictronSound(SoundEvents.PORTAL_TRIGGER, Blocks.END_PORTAL_FRAME),
            new SonictronSound(SoundEvents.GLASS_BREAK, Blocks.GLASS_PANE),
            new SonictronSound(SoundEvents.EXPERIENCE_ORB_PICKUP, Items.ENDER_PEARL),
            new SonictronSound(SoundEvents.PLAYER_LEVELUP, Items.ENDER_EYE),
            new SonictronSound(SoundEvents.UI_BUTTON_CLICK, Blocks.STONE_BUTTON),
            new SonictronSound(SoundEvents.PLAYER_BIG_FALL, Blocks.COBBLESTONE),
            new SonictronSound(SoundEvents.PLAYER_SMALL_FALL, Blocks.DIRT),
            new SonictronSound(SoundEvents.ARROW_SHOOT, Items.ARROW),
            new SonictronSound(SoundEvents.ARROW_HIT, Items.FISHING_ROD),
            new SonictronSound(SoundEvents.ITEM_BREAK, Items.IRON_SHOVEL),
            new SonictronSound(SoundEvents.GENERIC_DRINK, Items.POTION),
            new SonictronSound(SoundEvents.PLAYER_BURP, Items.GLASS_BOTTLE),
            new SonictronSound(SoundEvents.CHEST_OPEN, Blocks.ENDER_CHEST),
            new SonictronSound(SoundEvents.CHEST_CLOSE, Blocks.CHEST),
            new SonictronSound(SoundEvents.WOODEN_DOOR_OPEN, Items.IRON_DOOR),
            new SonictronSound(SoundEvents.WOODEN_DOOR_CLOSE, Items.OAK_DOOR),
            new SonictronSound(SoundEvents.GENERIC_EAT, Items.PORKCHOP),
            new SonictronSound(SoundEvents.WOOL_STEP, Blocks.WHITE_WOOL),
            new SonictronSound(SoundEvents.GRASS_STEP, Blocks.GRASS),
            new SonictronSound(SoundEvents.GRAVEL_STEP, Blocks.GRAVEL),
            new SonictronSound(SoundEvents.SNOW_STEP, Blocks.SNOW),
            new SonictronSound(SoundEvents.PISTON_EXTEND, Blocks.PISTON),
            new SonictronSound(SoundEvents.PISTON_CONTRACT, Blocks.STICKY_PISTON),
            new SonictronSound(SoundEvents.AMBIENT_CAVE, Blocks.MOSSY_COBBLESTONE),
            new SonictronSound(SoundEvents.WEATHER_RAIN, Blocks.LAPIS_BLOCK),
            new SonictronSound(SoundEvents.LIGHTNING_BOLT_THUNDER, Blocks.DIAMOND_BLOCK)
            // new SonictronSound("note.bass", Blocks.PLANKS, 25)
            // new SonictronSound("block.fire.extinguish", Items.LAVA_BUCKET)
            // new SonictronSound("block.portal.ambient", Blocks.PORTAL)
            // new SonictronSound("block.portal.travel", Blocks.END_PORTAL)
            // new SonictronSound("damage.durtflesh", Items.IRON_SWORD)
            // new SonictronSound("damage.hurt", Items.DIAMOND_SWORD)
            // new SonictronSound("random.bow", Items.BOW)
            // new SonictronSound("entity.player.breath", Items.BUCKET)
        ).forEach(GregTechIMC.INSTANCE::addSonictronSound);
    }

    public static void doSonictronSound(ItemStack stack, Level level, BlockPos pos) {
        doSonictronSound(stack, level, pos, 0.3F);
    }

    public static void doSonictronSound(ItemStack stack, Level level, BlockPos pos, float volume) {
        if (stack.isEmpty()) return;

        String name = GregTechIMC.INSTANCE.getSoundFor(stack.getItem());
        int count = stack.getCount();
        boolean musicDisc = name.startsWith("music_disc.");

        if (musicDisc) {
            String suffix = RECORD_NAMES.getOrDefault(count, "wherearewenow");
            name += suffix;
        }

        float pitch = name.startsWith("block.note.") ? (float) Math.pow(2, (count - 13) / 12D) : 1;

        SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation(name));
        if (sound == null) {
            throw new IllegalArgumentException("Attempted to play invalid sound " + name);
        }

        level.playSound(null, pos, sound, SoundSource.BLOCKS, volume, pitch);
    }

    @Override
    protected void saveAdditional(FriendlyCompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt("currentIndex", this.currentIndex);
    }

    @Override
    protected void load(FriendlyCompoundTag tag) {
        super.load(tag);

        this.currentIndex = tag.getInt("currentIndex");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new SonictronMenu(containerId, this.worldPosition, playerInventory, player);
    }
}
