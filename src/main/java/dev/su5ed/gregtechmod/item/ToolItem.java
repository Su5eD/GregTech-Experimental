package dev.su5ed.gregtechmod.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import dev.su5ed.gregtechmod.util.GtUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.ToolAction;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ToolItem extends ResourceItem {
    private final float attackDamage;
    private final Multimap<Attribute, AttributeModifier> attributeModifiers;
    private final List<TagKey<EntityType<?>>> effectiveAganist;
    private final int selfDamageOnHit;
    protected final float destroySpeed;
    protected final Tier tier;
    private final Set<ToolAction> actions;
    private final Set<TagKey<Block>> blockTags;

    public ToolItem(ToolItemProperties<?> properties) {
        super(properties.setNoEnchant());

        this.attackDamage = properties.attackDamage - 1;
        this.attributeModifiers = ImmutableMultimap.of(
            Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION),
            Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", properties.attackSpeed, AttributeModifier.Operation.ADDITION)
        );
        this.effectiveAganist = Collections.unmodifiableList(properties.effectiveAganist);
        this.selfDamageOnHit = properties.selfDamageOnHit;
        this.destroySpeed = properties.destroySpeed;
        this.tier = properties.tier;
        this.actions = Collections.unmodifiableSet(properties.actions);
        this.blockTags = Collections.unmodifiableSet(properties.blockTags);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        return slot == EquipmentSlot.MAINHAND ? this.attributeModifiers : super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (this.selfDamageOnHit > 0) {
            stack.hurtAndBreak(this.selfDamageOnHit, attacker, entity -> entity.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        if (StreamEx.of(this.effectiveAganist).anyMatch(target.getType()::is)) {
            GtUtil.damageEntity(target, attacker, this.attackDamage + 1);
        }
        return true;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return this.actions.contains(toolAction);
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return canMineBlock(state) && TierSortingRegistry.isCorrectTierForDrops(this.tier, state);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return canMineBlock(state) ? this.destroySpeed : super.getDestroySpeed(stack, state);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miningEntity) {
        if (!this.actions.isEmpty() && !level.isClientSide && isDamageable(stack) && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, miningEntity, player -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        }
        return super.mineBlock(stack, level, state, pos, miningEntity);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag isAdvanced) {
        String durability = getDurabilityInfo(stack);
        if (durability != null) {
            components.add(Component.literal(durability));
        }

        super.appendHoverText(stack, level, components, isAdvanced);
    }

    @Nullable
    protected String getDurabilityInfo(ItemStack stack) {
        return stack.isDamageableItem() ? (stack.getMaxDamage() - stack.getDamageValue()) + " / " + stack.getMaxDamage() : null;
    }

    private boolean canMineBlock(BlockState state) {
        return StreamEx.of(this.blockTags)
            .anyMatch(state::is);
    }

    @SuppressWarnings("unchecked")
    public static class ToolItemProperties<T extends ToolItemProperties<T>> extends ExtendedItemProperties<T> {
        private float attackDamage;
        private float attackSpeed;
        private final List<TagKey<EntityType<?>>> effectiveAganist = new ArrayList<>();
        private int selfDamageOnHit;
        private float destroySpeed;
        private Tier tier;
        private final Set<ToolAction> actions = new HashSet<>();
        private final Set<TagKey<Block>> blockTags = new HashSet<>();

        public ToolItemProperties() {}

        public ToolItemProperties(Properties properties) {
            super(properties);
        }

        public T attackDamage(float attackDamage) {
            this.attackDamage = attackDamage;
            return (T) this;
        }

        public T attackSpeed(float attackSpeed) {
            this.attackSpeed = attackSpeed;
            return (T) this;
        }

        @SafeVarargs
        public final T effectiveAganist(TagKey<EntityType<?>>... tags) {
            Collections.addAll(this.effectiveAganist, tags);
            return (T) this;
        }

        public T selfDamageOnHit(int selfDamageOnHit) {
            this.selfDamageOnHit = selfDamageOnHit;
            return (T) this;
        }

        public T destroySpeed(float destroySpeed) {
            this.destroySpeed = destroySpeed;
            return (T) this;
        }

        public T tier(Tier tier) {
            this.tier = tier;
            return (T) this;
        }

        public T actions(ToolAction... actions) {
            Collections.addAll(this.actions, actions);
            return (T) this;
        }

        @SafeVarargs
        public final T blockTags(TagKey<Block>... tags) {
            Collections.addAll(this.blockTags, tags);
            return (T) this;
        }
    }
}
