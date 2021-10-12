package mods.gregtechmod.objects.blocks.teblocks;

import ic2.core.IHasGui;
import ic2.core.block.EntityIC2Explosive;
import ic2.core.util.Util;
import mods.gregtechmod.api.machine.IPanelInfoProvider;
import mods.gregtechmod.api.upgrade.GtUpgradeType;
import mods.gregtechmod.api.upgrade.IC2UpgradeType;
import mods.gregtechmod.gui.GuiGtTeleporter;
import mods.gregtechmod.objects.blocks.teblocks.base.TileEntityUpgradable;
import mods.gregtechmod.objects.blocks.teblocks.container.ContainerGtTeleporter;
import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.TeleportUtil;
import mods.gregtechmod.util.nbt.NBTPersistent;
import mods.gregtechmod.util.nbt.NBTPersistent.Include;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

public class TileEntityGtTeleporter extends TileEntityUpgradable implements IHasGui, IPanelInfoProvider {
    private static final Map<Class<? extends Entity>, Function<Object, Float>> WEIGHTS = new HashMap<>();
    
    @NBTPersistent(include = Include.NON_NULL)
    public BlockPos targetPos;
    @NBTPersistent(include = Include.NON_NULL)
    public DimensionType targetDimension;
    
    private boolean hasEgg;
    
    static {
        addEntityWeight(EntityPlayer.class, player -> {
            int handsSize = 128;
            int mainInventoryCount = handsSize + player.inventory.mainInventory.stream()
                    .filter(stack -> !stack.isEmpty())
                    .mapToInt(stack -> stack.getMaxStackSize() > 1 ? stack.getCount() : 64)
                    .sum();
            int armorInventoryCount = (int) (player.inventory.armorInventory.stream()
                    .filter(stack -> !stack.isEmpty())
                    .count() * 256);
            
            return (mainInventoryCount + armorInventoryCount) / 666.6F;
        });
        Stream.of(EntityArrow.class, EntityEnderEye.class, EntityFireball.class, EntityFireworkRocket.class, EntityItem.class, EntityThrowable.class, EntityXPOrb.class)
                .forEach(clazz -> addEntityWeight(clazz, e -> 0.001F));
        addEntityWeight(EntityHanging.class, 0.005F);
        addEntityWeight(EntityFallingBlock.class, 0.01F);
        addEntityWeight(EntityBoat.class, 0.1F);
        addEntityWeight(EntityMinecart.class, 0.1F);
        addEntityWeight(EntityLiving.class, 0.5F);
        addEntityWeight(EntityEnderCrystal.class, 2);
        addEntityWeight(EntityIC2Explosive.class, 5);
        addEntityWeight(EntityTNTPrimed.class, 5);
    }
    
    public static <T extends Entity> void addEntityWeight(Class<T> clazz, float weight) {
        WEIGHTS.put(clazz, e -> weight);
    }
    
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T extends Entity> void addEntityWeight(Class<T> clazz, Function<T, Float> weight) {
        WEIGHTS.put(clazz, (Function) weight);
    }

    public TileEntityGtTeleporter() {
        super("gt_teleporter");
    }

    @Override
    protected void onLoaded() {
        super.onLoaded();
        
        this.hasEgg = checkForEgg();
        if (this.targetPos == null) this.targetPos = this.pos.offset(EnumFacing.UP);
        if (this.targetDimension == null) this.targetDimension = this.world.provider.getDimensionType();
    }

    @Override
    protected boolean onActivatedChecked(EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        return side == getFacing() && !this.world.isRemote || super.onActivatedChecked(player, hand, side, hitX, hitY, hitZ);
    }

    @Override
    protected void updateEntityServer() {
        super.updateEntityServer();
        
        if (this.tickCounter % 100 == 50) this.hasEgg = checkForEgg();
        
        if (isAllowedToWork() && isRedstonePowered()) {
            useEnergy(8192);
            
            int distance = calculateDistance();
            BlockPos offset = this.pos.offset(getFacing(), 2);
            AxisAlignedBB teleportRange = new AxisAlignedBB(offset.add(-1, -1, -1), offset.add(2, 2, 2));
            
            this.world.getEntitiesWithinAABB(Entity.class, teleportRange).stream()
                    .filter(entity -> !entity.isDead)
                    .forEach(entity -> {
                        int cost = (int) (Math.pow(distance, 2) * calculateWeight(entity));
                        if (cost >= 0 && canUseEnergy(cost)) {
                            if (entity.isRiding()) entity.dismountRidingEntity();
                            if (entity.isBeingRidden()) entity.getPassengers().forEach(Entity::dismountRidingEntity);
                            
                            if (teleportEntity(entity)) useEnergy(cost);
                        }
                    });
            setActive(true);
        } else {
            setActive(false);
        }
    }
    
    private boolean teleportEntity(Entity entity) {
        double x = this.targetPos.getX() + 0.5;
        double y = this.targetPos.getY() + 1.5 + entity.getYOffset();
        double z = this.targetPos.getZ() + 0.5;
        DimensionType dimension = isDimensionalTeleportAvailable() ? this.targetDimension : this.world.provider.getDimensionType();
        
        TeleportUtil.performTeleport(entity, dimension, x, y, z);
        
        return true;
    }
    
    public boolean checkForEgg() {
        for (int i = -5; i <= 5; i++) {
            for (int j = -5; j <= 5; j++) {
                for (int k = -5; k <= 5; k++) {
                    BlockPos pos = this.pos.add(i, j, k);
                    if (this.world.getBlockState(pos).getBlock() == Blocks.DRAGON_EGG) return true;
                }
            }
        }
        return false;
    }
    
    private int calculateDistance() {
        int multiplier = this.targetDimension != this.world.provider.getDimensionType() && isDimensionalTeleportAvailable() ? 100 : 1;
        double x = Math.pow(this.pos.getX() - this.targetPos.getX(), 2);
        double y = Math.pow(this.pos.getY() - this.targetPos.getY(), 2);
        double z = Math.pow(this.pos.getZ() - this.targetPos.getZ(), 2);
        
        return Math.abs(multiplier * (int) Math.sqrt(x + y + z));
    }
    
    public boolean canTeleportAcrossDimensions() {
        return this.hasEgg;
    }
    
    private boolean isDimensionalTeleportAvailable() {
        return canTeleportAcrossDimensions() && this.targetDimension != null;
    }
    
    private float calculateWeight(Entity entity) {
        Function<Object, Float> func = getWeightFunc(entity.getClass());
        return func != null ? func.apply(entity) : -1;
    }
    
    private Function<Object, Float> getWeightFunc(Class<?> clazz) {
        Function<Object, Float> weight = WEIGHTS.get(clazz);
        
        if (weight == null) {
            Class<?> superClass = clazz.getSuperclass();
            return superClass != Object.class ? getWeightFunc(superClass) : null;
        }
        
        return weight;
    }

    @Override
    protected Collection<EnumFacing> getSinkSides() {
        return Util.allFacings;
    }

    @Override
    public int getBaseSinkTier() {
        return 9;
    }

    @Override
    public double getMaxInputEUp() {
        return 1000000;
    }

    @Override
    protected int getBaseEUCapacity() {
        return 10000000;
    }

    @Override
    public Set<IC2UpgradeType> getCompatibleIC2Upgrades() {
        return Collections.singleton(IC2UpgradeType.BATTERY);
    }

    @Override
    public Set<GtUpgradeType> getCompatibleGtUpgrades() {
        return EnumSet.of(GtUpgradeType.BATTERY, GtUpgradeType.LOCK, GtUpgradeType.MJ, GtUpgradeType.STEAM);
    }

    @Override
    public void getNetworkedFields(List<? super String> list) {
        super.getNetworkedFields(list);
        list.add("targetPos");
        list.add("targetDimension");
    }

    @Override
    public ContainerGtTeleporter getGuiContainer(EntityPlayer player) {
        return new ContainerGtTeleporter(player, this);
    }

    @Override
    public GuiScreen getGui(EntityPlayer player, boolean isAdmin) {
        return new GuiGtTeleporter(getGuiContainer(player));
    }

    @Override
    public void onGuiClosed(EntityPlayer player) {}

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String getMainInfo() {
        return GtUtil.translateTeBlock("gt_teleporter", "charge", this.getUniversalEnergy());
    }

    @Override
    public String getSecondaryInfo() {
        return "X: " + this.targetPos.getX() + "  Y: " + this.targetPos.getY() + "  Z: " + this.targetPos.getZ();
    }

    @Override
    public String getTertiaryInfo() {
        return GtUtil.translateTeBlock("gt_teleporter", "dimension", this.targetDimension.getName());
    }
}
