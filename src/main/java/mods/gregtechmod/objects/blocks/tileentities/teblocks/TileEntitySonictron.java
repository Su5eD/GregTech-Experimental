package mods.gregtechmod.objects.blocks.tileentities.teblocks;

import ic2.core.ContainerBase;
import ic2.core.IHasGui;
import ic2.core.block.TileEntityInventory;
import ic2.core.block.invslot.InvSlot;
import mods.gregtechmod.api.GregTechAPI;
import mods.gregtechmod.api.util.SonictronSound;
import mods.gregtechmod.core.GregTechMod;
import mods.gregtechmod.gui.GuiSonictron;
import mods.gregtechmod.objects.blocks.tileentities.teblocks.container.ContainerSonictron;
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
            if (!this.getActive()) this.setActive(true);
            GregTechMod.proxy.doSonictronSound(this.content.get(currentIndex), this.world, this.pos.add(0.5, 0.5, 0.5));
            if (++this.currentIndex > 63) this.currentIndex=-1;
        } else if (this.getActive()) this.setActive(false);
    }

    @Override
    public List<String> getNetworkedFields() {
        List<String> ret = super.getNetworkedFields();
        ret.add("currentIndex");
        return ret;
    }

    public static void loadSonictronSounds() {
        GregTechAPI.logger.info("Loading Sonictron sounds");
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.note.harp", Blocks.IRON_BLOCK, 25));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.note.pling", Blocks.GOLD_BLOCK, 25));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.note.basedrum", Blocks.STONE, 25));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.note.bass", Blocks.LOG, 25));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.PLANKS, "note.bass", 25));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.note.hat", Blocks.GLASS, 25));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.note.snare", Blocks.SAND, 25));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("record.", Items.RECORD_CAT, 12));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.generic.explode", Blocks.TNT, 3));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.fire.ambient", Items.FIRE_CHARGE));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("item.flintandsteel.use", Items.FLINT_AND_STEEL));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.lava.pop", Items.LAVA_BUCKET));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.water.ambient", Items.WATER_BUCKET));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.generic.splash", Items.BUCKET));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.LAVA_BUCKET, "block.fire.extinguish"));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.PORTAL, "block.portal.ambient"));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Blocks.END_PORTAL, "block.portal.travel"));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.portal.trigger", Blocks.END_PORTAL_FRAME));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.glass.break", Blocks.GLASS_PANE));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.experience_orb.pickup", Items.ENDER_PEARL));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.player.levelup", Items.ENDER_EYE));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("ui.button.click", Blocks.STONE_BUTTON));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.player.big_fall", Blocks.COBBLESTONE));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.player.small_fall", Blocks.DIRT));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.IRON_SWORD, "damage.durtflesh"));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.DIAMOND_SWORD, "damage.hurt"));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.BOW, "random.bow"));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.arrow.shoot", Items.ARROW));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.arrow.hit", Items.FISHING_ROD));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.item.break", Items.IRON_SHOVEL));
        //GregTechAPI.sonictronSounds.add(new SonictronSound(Items.BUCKET, "entity.player.breath"));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.generic.drink", Items.POTIONITEM));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.player.burp", Items.GLASS_BOTTLE));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.chest.open", Blocks.ENDER_CHEST));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.chest.close", Blocks.CHEST));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.wooden_door.open", Items.IRON_DOOR));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.wooden_door.close", Items.OAK_DOOR));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.generic.eat", Items.PORKCHOP));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.cloth.step", Blocks.WOOL));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.grass.step", Blocks.GRASS));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.gravel.step", Blocks.GRAVEL));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.snow.step", Blocks.SNOW));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.piston.extend", Blocks.PISTON));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("block.piston.contract", Blocks.STICKY_PISTON));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("ambient.cave", Blocks.MOSSY_COBBLESTONE));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("weather.rain", Blocks.LAPIS_BLOCK));
        GregTechAPI.SONICTRON_SOUNDS.add(new SonictronSound("entity.lightning.thunder", Blocks.DIAMOND_BLOCK));
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
