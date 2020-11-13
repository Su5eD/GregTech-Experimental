package mods.gregtechmod.objects.blocks.tileentities;

import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.SonictronSound;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiSonictron;
import mods.gregtechmod.objects.blocks.tileentities.machines.container.ContainerSonictron;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class TileEntitySonictron extends TileEntityInventory implements IHasGui {
    public int currentIndex = -1;
    public final InvSlot content;
    public boolean isPowered;

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

        if (this.world.getWorldTime()%2 == 0 && this.currentIndex > -1) {
            if (!this.getActive())
                this.setActive(true);
            GregTechMod.proxy.doSonictronSound(this.content.get(currentIndex), this.world, this.pos.add(0.5, 0.5, 0.5));
            if (++this.currentIndex > 63) this.currentIndex=-1;
        } else if (this.getActive())
            this.setActive(false);
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("currentIndex");
        return ret;
    }

    public static void loadSonictronSounds() {
        GregTechAPI.logger.info("Loading Sonictron sounds");
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.IRON_BLOCK, "block.note.harp", 25));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.GOLD_BLOCK, "block.note.pling", 25));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.STONE, "block.note.basedrum", 25));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.LOG, "block.note.bass", 25));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.PLANKS, "note.bass", 25));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.GLASS, "block.note.hat", 25));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.SAND, "block.note.snare", 25));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.RECORD_CAT, "record.", 12));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.TNT, "entity.generic.explode", 3));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.FIRE_CHARGE, "block.fire.ambient", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.FLINT_AND_STEEL, "item.flintandsteel.use", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.LAVA_BUCKET, "block.lava.pop", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.WATER_BUCKET, "block.water.ambient", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.BUCKET, "entity.generic.splash", 1));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.LAVA_BUCKET, "block.fire.extinguish", 1));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.PORTAL, "block.portal.ambient", 1));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.END_PORTAL, "block.portal.travel", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.END_PORTAL_FRAME, "block.portal.trigger", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.GLASS_PANE, "block.glass.break", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.ENDER_PEARL, "entity.experience_orb.pickup", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.ENDER_EYE, "entity.player.levelup", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.STONE_BUTTON, "ui.button.click", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.COBBLESTONE, "entity.player.big_fall", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.DIRT, "entity.player.small_fall", 1));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.IRON_SWORD, "damage.durtflesh", 1));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.DIAMOND_SWORD, "damage.hurt", 1));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.BOW, "random.bow", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.ARROW, "entity.arrow.shoot", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.FISHING_ROD, "entity.arrow.hit", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.IRON_SHOVEL, "entity.item.break", 1));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.BUCKET, "entity.player.breath", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.POTIONITEM, "entity.generic.drink", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.GLASS_BOTTLE, "entity.player.burp", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.ENDER_CHEST, "block.chest.open", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.CHEST, "block.chest.close", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.IRON_DOOR, "block.wooden_door.open", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.OAK_DOOR, "block.wooden_door.close", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Items.PORKCHOP, "entity.generic.eat", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.WOOL, "block.cloth.step", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.GRASS, "block.grass.step", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.GRAVEL, "block.gravel.step", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.SNOW, "block.snow.step", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.PISTON, "block.piston.extend", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.STICKY_PISTON, "block.piston.contract", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.MOSSY_COBBLESTONE, "ambient.cave", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.LAPIS_BLOCK, "weather.rain", 1));
        GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.DIAMOND_BLOCK, "entity.lightning.thunder", 1));
    }

    @Override
    public ContainerBase<?> getGuiContainer(EntityPlayer entityPlayer) {
        return new ContainerSonictron(this);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui(EntityPlayer entityPlayer, boolean b) {
        return new GuiSonictron(new ContainerSonictron(this));
    }

    @Override
    public void onGuiClosed(EntityPlayer entityPlayer) {}
}
