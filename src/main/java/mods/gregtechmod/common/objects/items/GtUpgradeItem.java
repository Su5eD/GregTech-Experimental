package mods.gregtechmod.common.objects.items;

import ic2.core.block.state.EnumProperty;
import ic2.core.block.state.IIdProvider;
import ic2.core.ref.IMultiItem;
import mods.gregtechmod.common.core.GregtechMod;
import mods.gregtechmod.common.init.ItemInit;
import mods.gregtechmod.common.util.IHasModel;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class GtUpgradeItem<T extends Enum<T> & IIdProvider> extends Item implements IHasModel, IMultiItem<T> {
    @SuppressWarnings("all")
    private EnumProperty<T> typeProperty = new EnumProperty("type", GtUpgradeType.class);

    public GtUpgradeItem() {
        setRegistryName("upgrade");
        setCreativeTab(GregtechMod.gregtechtab);
        ItemInit.ITEMS.put("upgrade", this);
        setHasSubtypes(true);
        setTranslationKey("upgrade");
    }

    @Override
    public void registerModels() {
        for (GtUpgradeType type : GtUpgradeType.values()) {
            registerModel(this,type.getId(), "upgrade", type.getName());
        }

        for (GtUpgradeType type : GtUpgradeType.values()) {
            ModelBakery.registerItemVariants( this, getModelLocation("upgrade", type.getName()));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void registerModel(Item item, int meta, String name, String extraName) {
        ModelLoader.setCustomModelResourceLocation(item, meta, getModelLocation(name, extraName));
    }

    @SideOnly(Side.CLIENT)
    public static ModelResourceLocation getModelLocation(String name, String extraName) {
        String loc = String.format("%s:%s/%s", GregtechMod.MODID, name, extraName);
        return new ModelResourceLocation(loc, extraName);
    }

    @Override
    public String getTranslationKey(ItemStack stack) {
        return "item.upgrade."+getVariant(stack);
    }

    @Override
    public ItemStack getItemStack(T t) {
        return new ItemStack(this, 1, t.getId());
    }

    @Override
    public ItemStack getItemStack(String s) {
        return new ItemStack(this, 1, GtUpgradeType.valueOf(s).getId());
    }

    @Override
    public String getVariant(ItemStack stack) {
        return this.typeProperty.getValue(stack.getMetadata()).name();
    }

    @Override
    public Set<T> getAllTypes() {
        return EnumSet.allOf(this.typeProperty.getValueClass());
    }

    public enum GtUpgradeType implements IIdProvider{
        hv_transformer(2, 3, "Higher tier of the transformer upgrade"),
         //item_detector(maxCount), //cover
         //liquid_detector(maxCount), //cover
        lithium_battery(4, 1, "Adds 100000 EU to the energy capacity"),
        energy_crystal(4, 2, "Adds 100000 EU to the energy capacity"),
        lapotron_crystal(4, 3, "Adds 1 Million EU to the energy capacity"),
        energy_orb(4, 4, "Adds 10 Million EU to the energy capacity"),
        quantum_chest(1, 1, "Upgrades a Digital Chest to a Quantum chest"),
        machine_lock(1, 1, "Makes a machine private for the one, who applies this upgrade"),
         //energy_meter(maxCount), //cover
         //active_detector(maxCount), //cover
         //machine_controller(maxCount), //cover
        steam_upgrade(1, 1,"Lets Machines consume Steam at 2mb per EU (lossless)"),
        steam_tank(4, 1, "Increases Steam Capacity by 64 Buckets");

        private final int maxCount;
        private final int requiredTier;
        private final String description;

        private boolean canBeInserted = true;

        GtUpgradeType(int maxCount, int requiredTier, String description) {
            this.maxCount = maxCount;
            this.requiredTier = requiredTier;
            this.description = description;
        }

        private static void updateconditions(int currentTier, boolean hasSteamUpgrade) {
            hv_transformer.canBeInserted = currentTier >= 3 && currentTier < 5;
            steam_tank.canBeInserted = hasSteamUpgrade;
        }

        public boolean canBeInserted(int count, int tier, boolean hasSteamUpgrade) {
            updateconditions(tier, hasSteamUpgrade);

            if (this.maxCount > count && this.requiredTier <= tier) return this.canBeInserted;
            return false;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public int getId() {
            return ordinal();
        }

        public String getDescription() { return this.description; }
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (GtUpgradeType type : GtUpgradeType.values()) {
            items.add(new ItemStack(this, 1, ((IIdProvider)type).getId()));
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        GtUpgradeType type = GtUpgradeType.values()[stack.getMetadata()];
        tooltip.add(type.getDescription());
    }
}
