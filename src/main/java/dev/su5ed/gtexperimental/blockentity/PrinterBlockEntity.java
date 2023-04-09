package dev.su5ed.gtexperimental.blockentity;

import dev.su5ed.gtexperimental.api.recipe.RecipeProvider;
import dev.su5ed.gtexperimental.blockentity.component.ManagedRecipeHandler;
import dev.su5ed.gtexperimental.menu.SimpleMachineMenu;
import dev.su5ed.gtexperimental.object.GTBlockEntity;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeIngredientTypes;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeManagers;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.recipe.type.RecipePropertyMap;
import dev.su5ed.gtexperimental.util.GtUtil;
import dev.su5ed.gtexperimental.util.inventory.InventorySlot;
import dev.su5ed.gtexperimental.util.inventory.SlotDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.items.ItemHandlerHelper;
import one.util.streamex.StreamEx;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class PrinterBlockEntity extends SimpleMachineBlockEntity {

    static {
        ModRecipeManagers.PRINTER.registerProvider(new PrinterRecipeProvider());
    }

    public PrinterBlockEntity(BlockPos pos, BlockState state) {
        super(GTBlockEntity.PRINTER, pos, state, SimpleMachineMenu::printer, be -> {
            int inputCount = ModRecipeManagers.PRINTER.getRecipeType().getInputType().getIngredientCount();
            return new ManagedRecipeHandler<>(be, ModRecipeManagers.PRINTER, ModRecipeOutputTypes.ITEM.listOf(inputCount), List::of, PrinterBlockEntity::getPrinterInput, ManagedRecipeHandler::canAddSingleOutput, ManagedRecipeHandler::consumeMultiInput, ManagedRecipeHandler::addSingleOutput);
        }, SlotQueueMode.OUTPUT);
    }

    @Override
    protected InventorySlot createExtraSlot() {
        return this.inventoryHandler.addSlot(handler -> new InventorySlot(handler, "book", InventorySlot.Mode.INPUT, SlotDirection.BOTTOM, 1, stack -> stack.is(Items.WRITTEN_BOOK) || stack.is(Items.FILLED_MAP), stack -> {}));
    }

    private static List<ItemStack> getPrinterInput(ManagedRecipeHandler<?, ?, ?> handler) {
        return StreamEx.of(handler.getParent().queueInputSlot.get(), handler.getParent().inputSlot.get(), handler.getParent().extraSlot.get())
            .remove(ItemStack::isEmpty)
            .toList();
    }

    public static class PrinterRecipeProvider implements RecipeProvider<MISORecipe<ItemStack, ItemStack>, List<ItemStack>> {
        private final Map<ResourceLocation, MISORecipe<ItemStack, ItemStack>> byName = new HashMap<>();
        
        @Nullable
        @Override
        public MISORecipe<ItemStack, ItemStack> getRecipeFor(Level level, List<ItemStack> input) {
            if (!input.isEmpty()) {
                ItemStack primaryInput = input.get(0);
                if (input.size() > 1) {
                    ItemStack secondaryInput = input.get(1);
                    if (secondaryInput.is(Tags.Items.DYES)) {
                        Optional<CraftingRecipe> recipe = GtUtil.getCraftingOutput(level, primaryInput, secondaryInput);
                        if (recipe.isPresent()) {
                            return createRecipe(primaryInput, secondaryInput, ItemStack.EMPTY, recipe.get().getResultItem(), 200, 2);
                        }
                    }
                    if (input.size() > 2) {
                        ItemStack extra = input.get(2);
                        if (!extra.isEmpty() && secondaryInput.is(Items.INK_SAC)) {
                            if (extra.is(Items.WRITTEN_BOOK) && primaryInput.is(Items.BOOK)) {
                                ItemStack copy = extra.copy();
                                return createRecipe(primaryInput, secondaryInput, copy, copy, 200, 1);
                            }
                            else if (extra.is(Items.FILLED_MAP) && primaryInput.is(Items.MAP)) {
                                ItemStack copy = extra.copy();
                                return createRecipe(primaryInput, secondaryInput, copy, copy, 100, 1);
                            }
                        }
                    }
                }
            }
            return null;
        }

        @Nullable
        @Override
        public MISORecipe<ItemStack, ItemStack> getById(Level level, ResourceLocation id) {
            return this.byName.get(id);
        }

        @Override
        public boolean hasRecipeFor(Level level, List<ItemStack> input) {
            return StreamEx.of(input).anyMatch(stack -> stack.is(Items.BOOK) || stack.is(Items.MAP) || stack.is(Tags.Items.DYES) || stack.is(Items.INK_SAC));
        }

        @Override
        public void reset() {
            this.byName.clear();
        }

        private MISORecipe<ItemStack, ItemStack> createRecipe(ItemStack primaryInput, ItemStack secondaryInput, ItemStack copy, ItemStack output, int duration, double energyCost) {
            ResourceLocation id = location("dynamic", "printer", GtUtil.itemName(primaryInput) + "_to_" + GtUtil.itemName(output) + (!copy.isEmpty() ? "_copying_" + GtUtil.itemName(copy) : ""));
            MISORecipe<ItemStack, ItemStack> recipe = MISORecipe.printer(id, List.of(ModRecipeIngredientTypes.ITEM.of(ItemHandlerHelper.copyStackWithSize(primaryInput, 1)), ModRecipeIngredientTypes.ITEM.of(ItemHandlerHelper.copyStackWithSize(secondaryInput, 1))), output, RecipePropertyMap.builder().duration(duration).energyCost(energyCost).build());
            this.byName.put(id, recipe);
            return recipe;
        }
    }
}
