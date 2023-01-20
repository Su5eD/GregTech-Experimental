package dev.su5ed.gtexperimental.object;

import dev.su5ed.gtexperimental.GregTechTags;
import dev.su5ed.gtexperimental.item.ReactorCoolantCellItem;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.TaggedItemProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.Lazy;
import org.jetbrains.annotations.Nullable;

public enum NuclearCoolantPack implements TaggedItemProvider {
    NAK_60K(60000, GregTechTags.CRAFTING_60K_COOLANT_STORE),
    NAK_180K(180000, GregTechTags.CRAFTING_180K_COOLANT_STORE),
    NAK_360K(360000, GregTechTags.CRAFTING_360K_COOLANT_STORE),
    HELIUM_60K(60000, GregTechTags.CRAFTING_60K_COOLANT_STORE),
    HELIUM_180K(180000, GregTechTags.CRAFTING_180K_COOLANT_STORE),
    HELIUM_360K(360000, GregTechTags.CRAFTING_360K_COOLANT_STORE);

    private final Lazy<Item> instance;
    private final TagKey<Item> tag;

    NuclearCoolantPack(int heatStorage, TagKey<Item> tag) {
        this.instance = Lazy.of(() -> new ReactorCoolantCellItem(heatStorage));
        this.tag = tag;
    }

    @Override
    public String getRegistryName() {
        return GtUtil.registryName(getName(), "coolant");
    }

    @Override
    public Item getItem() {
        return this.instance.get();
    }

    @Nullable
    @Override
    public TagKey<Item> getTag() {
        return this.tag;
    }
}
