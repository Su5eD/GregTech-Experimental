package dev.su5ed.gregtechmod.blockentity.base;

import dev.su5ed.gregtechmod.api.machine.PowerProvider;
import dev.su5ed.gregtechmod.api.machine.UpgradableBlockEntity;
import dev.su5ed.gregtechmod.api.upgrade.Upgrade;
import dev.su5ed.gregtechmod.api.upgrade.UpgradeCategory;
import dev.su5ed.gregtechmod.api.util.GtFluidTank;
import dev.su5ed.gregtechmod.blockentity.component.FluidHandler;
import dev.su5ed.gregtechmod.blockentity.component.UpgradeManager;
import dev.su5ed.gregtechmod.util.BlockEntityProvider;
import dev.su5ed.gregtechmod.util.GtLocale;
import dev.su5ed.gregtechmod.util.power.SteamPowerProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

public abstract class UpgradableBlockEntityImpl extends ElectricBlockEntityImpl implements UpgradableBlockEntity {
    public final FluidHandler fluidHandler;
    private final UpgradeManager<UpgradableBlockEntityImpl> upgradeManager;

    public UpgradableBlockEntityImpl(BlockEntityProvider provider, BlockPos pos, BlockState state) {
        super(provider, pos, state);

        this.fluidHandler = addComponent(new FluidHandler(this));
        this.upgradeManager = addComponent(new UpgradeManager<>(this, this::onUpdateUpgrade));
    }

    protected void onUpdateUpgrade(UpgradeCategory category) {}

    protected abstract int getBaseEUCapacity();

    protected int getSteamCapacity() {
        return 10000;
    }

    protected boolean isMultiplePacketsForTransformer() {
        return true;
    }

    @Override
    public void configurePowerProvider(PowerProvider provider) {
        if (provider instanceof SteamPowerProvider steamProvider) {
            steamProvider.getSteamTank().setCapacity(getSteamCapacity());
        }
    }

    @Override
    public <T extends GtFluidTank> T addTank(T tank) {
        return this.fluidHandler.add(tank);
    }

    @Override
    public void provideAdditionalDrops(List<? super ItemStack> drops) {
        super.provideAdditionalDrops(drops);

        StreamEx.of(this.upgradeManager.getUpgrades())
            .map(UpgradeManager.UpgradeInstance::stack)
            .toListAndThen(drops::addAll);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        return this.upgradeManager.addUpgrade(stack, player) ? InteractionResult.SUCCESS : super.use(state, level, pos, player, hand, hit);
    }

    @Override
    public final int getSourcePackets() {
        int basePackets = getBaseSourcePackets();
        return basePackets == 1 && isMultiplePacketsForTransformer() && getUpgradeCount(UpgradeCategory.TRANSFORMER) > 0 ? 4 : basePackets;
    }

    @Override
    public final int getSinkTier() {
        return getBaseSinkTier() + getUpgradeCount(UpgradeCategory.TRANSFORMER);
    }

    @Override
    public final int getSourceTier() {
        int transformers = getUpgradeCount(UpgradeCategory.TRANSFORMER);
        int tier = getBaseSourceTier() + transformers;

        return transformers > 0 && isMultiplePacketsForTransformer() ? tier - 1 : tier;
    }

    @Override
    public final int getEnergyCapacity() {
        return getBaseEUCapacity() + getExtraEUCapacity();
    }

    @Override
    public int getExtraEUCapacity() {
        return StreamEx.of(this.upgradeManager.getUpgrades())
            .map(UpgradeManager.UpgradeInstance::upgrade)
            .mapToInt(Upgrade::getExtraEUCapacity)
            .sum();
    }

    @Override
    public int getUpgradeCount(UpgradeCategory category) {
        return this.upgradeManager.getUpgradeCount(category);
    }

    @Override
    public Set<UpgradeCategory> getCompatibleUpgrades() {
        return UpgradeCategory.DEFAULT;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);

        String upgrades = StreamEx.of(getCompatibleUpgrades())
            .filter(UpgradeCategory::show)
            .map(category -> category.name().substring(0, 1))
            .distinct()
            .sorted()
            .joining(" ");
        if (!upgrades.isEmpty()) {
            tooltip.add(GtLocale.key("info", "possible_upgrades").toComponent(upgrades).withStyle(ChatFormatting.GRAY));
        }
    }
}
