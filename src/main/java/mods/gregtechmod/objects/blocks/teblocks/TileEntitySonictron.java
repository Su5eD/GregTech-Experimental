package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.SonictronSound;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiSonictron;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerSonictron;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class TileEntitySonictron extends TileEntityInventory implements IHasGui {
    public int currentIndex = -1;
    public final InvSlot content;

    public TileEntitySonictron() {
        this.content = new InvSlot(this, "content", InvSlot.Access.NONE, 64);
        this.content.setStackSizeLimit(24);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setInteger("currentIndex", this.currentIndex);
        return super.writeToNBT(nbt);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        this.currentIndex = nbtTagCompound.getInteger("currentIndex");
    }

    @Override
    protected void updateEntityServer() {
        if (this.world.getRedstonePowerFromNeighbors(pos) > 0) {
            if (this.currentIndex < 0) {
                this.currentIndex = 0;
            }
        }

        if (this.world.getWorldTime() % 2 == 0 && this.currentIndex > -1) {
            if (!this.getActive()) this.setActive(true);
            GregTechMod.runProxy(clientProxy -> clientProxy.doSonictronSound(this.content.get(currentIndex), this.world, this.pos.add(0.5, 0.5, 0.5)));
            if (++this.currentIndex > 63) this.currentIndex = -1;
        } else if (this.getActive()) this.setActive(false);
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("currentIndex");
        return ret;
    }

    public static void loadSonictronSounds() {
        GregTechMod.logger.info("Loading Sonictron sounds");
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
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
