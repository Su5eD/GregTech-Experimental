package dev.su5ed.gtexperimental.blockentity.component;

import dev.su5ed.gtexperimental.api.recipe.BaseRecipe;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.ListRecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredient;
import dev.su5ed.gtexperimental.api.recipe.RecipeIngredientType;
import dev.su5ed.gtexperimental.api.recipe.RecipeManager;
import dev.su5ed.gtexperimental.api.recipe.RecipeOutputType;
import dev.su5ed.gtexperimental.blockentity.SimpleMachineBlockEntity;
import dev.su5ed.gtexperimental.recipe.MIMORecipe;
import dev.su5ed.gtexperimental.recipe.MISORecipe;
import dev.su5ed.gtexperimental.recipe.SIMORecipe;
import dev.su5ed.gtexperimental.recipe.SISORecipe;
import dev.su5ed.gtexperimental.recipe.setup.ModRecipeOutputTypes;
import dev.su5ed.gtexperimental.util.inventory.InventorySlot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import one.util.streamex.StreamEx;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static dev.su5ed.gtexperimental.util.GtUtil.location;

public class ManagedRecipeHandler<R extends BaseRecipe<?, ?, IN, OUT, ? super R>, IN, OUT> extends RecipeHandler<SimpleMachineBlockEntity, R, IN, OUT> {
    private static final ResourceLocation RECIPE_HANDLER_SISO = location("siso_recipe_handler");
    private static final ResourceLocation RECIPE_HANDLER_MISO = location("miso_recipe_handler");
    private static final ResourceLocation RECIPE_HANDLER_MIMO = location("mimo_recipe_handler");
    private static final ResourceLocation RECIPE_HANDLER_SIMO = location("simo_recipe_handler");

    private final ResourceLocation name;
    private final Function<ItemStack, IN> inputMapper;
    private final Function<ManagedRecipeHandler<R, IN, OUT>, IN> inputGetter;
    private final BiPredicate<ManagedRecipeHandler<R, IN, OUT>, R> outputChecker;
    private final BiConsumer<ManagedRecipeHandler<R, IN, OUT>, R> inputConsumer;
    private final BiConsumer<ManagedRecipeHandler<R, IN, OUT>, R> outputAdder;

    public static RecipeHandler<SimpleMachineBlockEntity, SISORecipe<ItemStack, ItemStack>, ItemStack, ItemStack> createSISO(SimpleMachineBlockEntity parent, RecipeManager<SISORecipe<ItemStack, ItemStack>, RecipeIngredientType<? extends RecipeIngredient<ItemStack>, ItemStack>, ItemStack, ItemStack> manager) {
        return new ManagedRecipeHandler<>(parent, manager, ModRecipeOutputTypes.ITEM, RECIPE_HANDLER_SISO, Function.identity(), ManagedRecipeHandler::getSingleInput, ManagedRecipeHandler::canAddSingleOutput, ManagedRecipeHandler::consumeSingleInput, ManagedRecipeHandler::addSingleOutput);
    }

    public static RecipeHandler<SimpleMachineBlockEntity, MISORecipe<ItemStack, ItemStack>, List<ItemStack>, ItemStack> createMISO(SimpleMachineBlockEntity parent, RecipeManager<MISORecipe<ItemStack, ItemStack>, ListRecipeIngredientType<List<RecipeIngredient<ItemStack>>, ItemStack>, List<ItemStack>, ItemStack> manager) {
        int inputCount = manager.getRecipeType().getInputType().getIngredientCount();
        return new ManagedRecipeHandler<>(parent, manager, ModRecipeOutputTypes.ITEM.listOf(inputCount), RECIPE_HANDLER_MISO, List::of, ManagedRecipeHandler::getMultiInput, ManagedRecipeHandler::canAddSingleOutput, ManagedRecipeHandler::consumeMultiInput, ManagedRecipeHandler::addSingleOutput);
    }

    public static RecipeHandler<SimpleMachineBlockEntity, MIMORecipe, List<ItemStack>, List<ItemStack>> createMIMO(SimpleMachineBlockEntity parent, RecipeManager<MIMORecipe, ListRecipeIngredientType<List<RecipeIngredient<ItemStack>>, ItemStack>, List<ItemStack>, List<ItemStack>> manager) {
        int inputCount = manager.getRecipeType().getInputType().getIngredientCount();
        return new ManagedRecipeHandler<>(parent, manager, ModRecipeOutputTypes.ITEM.listOf(inputCount), RECIPE_HANDLER_MIMO, List::of, ManagedRecipeHandler::getMultiInput, ManagedRecipeHandler::canAddMultiOutput, ManagedRecipeHandler::consumeMultiInput, ManagedRecipeHandler::addMultiOutput);
    }

    public static RecipeHandler<SimpleMachineBlockEntity, SIMORecipe<ItemStack, List<ItemStack>>, ItemStack, List<ItemStack>> createSIMO(SimpleMachineBlockEntity parent, RecipeManager<SIMORecipe<ItemStack, List<ItemStack>>, RecipeIngredientType<? extends RecipeIngredient<ItemStack>, ItemStack>, ItemStack, List<ItemStack>> manager) {
        return new ManagedRecipeHandler<>(parent, manager, ModRecipeOutputTypes.ITEM, RECIPE_HANDLER_SIMO, Function.identity(), ManagedRecipeHandler::getSingleInput, ManagedRecipeHandler::canAddMultiOutput, ManagedRecipeHandler::consumeSingleInput, ManagedRecipeHandler::addMultiOutput);
    }

    public ManagedRecipeHandler(SimpleMachineBlockEntity parent, RecipeManager<R, ?, IN, OUT> manager, RecipeOutputType<IN> inputSerializer,
                                ResourceLocation name, Function<ItemStack, IN> inputMapper, Function<ManagedRecipeHandler<R, IN, OUT>, IN> inputGetter,
                                BiPredicate<ManagedRecipeHandler<R, IN, OUT>, R> outputChecker, BiConsumer<ManagedRecipeHandler<R, IN, OUT>, R> inputConsumer,
                                BiConsumer<ManagedRecipeHandler<R, IN, OUT>, R> outputAdder) {
        super(parent, manager, inputSerializer);

        this.name = name;
        this.inputMapper = inputMapper;
        this.inputGetter = inputGetter;
        this.outputChecker = outputChecker;
        this.inputConsumer = inputConsumer;
        this.outputAdder = outputAdder;
    }

    @Override
    public ResourceLocation getName() {
        return this.name;
    }

    @Override
    public boolean accepts(ItemStack input) {
        IN mapped = this.inputMapper.apply(input);
        return !this.parent.getLevel().isClientSide && this.manager.hasRecipeFor(this.parent.getLevel(), mapped);
    }

    @Override
    protected IN getInput() {
        return this.inputGetter.apply(this);
    }

    @Override
    protected boolean canAddOutput(R recipe) {
        return this.outputChecker.test(this, recipe);
    }

    @Override
    protected void consumeInput(R recipe) {
        this.inputConsumer.accept(this, recipe);
    }

    @Override
    protected void addOutput(R recipe) {
        this.outputAdder.accept(this, recipe);
    }

    public static ItemStack getSingleInput(ManagedRecipeHandler<?, ?, ?> handler) {
        return handler.getParent().inputSlot.get();
    }

    public static void consumeSingleInput(ManagedRecipeHandler<?, ?, ?> handler, BaseRecipe<?, ?, ItemStack, ?, ?> recipe) {
        handler.getParent().inputSlot.shrink(0, recipe.getInput().getCount());
    }

    public static boolean canAddSingleOutput(ManagedRecipeHandler<?, ?, ?> handler, BaseRecipe<?, ?, ?, ItemStack, ?> recipe) {
        ItemStack remainder = handler.getParent().outputSlot.add(0, recipe.getOutput(), true);
        return remainder.isEmpty() || handler.getParent().queueOutputSlot.add(0, remainder, true).isEmpty();
    }

    public static void addSingleOutput(ManagedRecipeHandler<?, ?, ?> handler, BaseRecipe<?, ?, ?, ItemStack, ?> recipe) {
        ItemStack remainder = handler.getParent().outputSlot.add(0, recipe.getOutput().copy());
        handler.getParent().queueOutputSlot.add(0, remainder);
        handler.getParent().ejectOutput();
    }

    public static List<ItemStack> getMultiInput(ManagedRecipeHandler<?, ?, ?> handler) {
        return StreamEx.of(handler.getParent().queueInputSlot.get(), handler.getParent().inputSlot.get())
            .remove(ItemStack::isEmpty)
            .toList();
    }

    public static void consumeMultiInput(ManagedRecipeHandler<?, ?, ?> handler, BaseRecipe<?, ListRecipeIngredient<ItemStack>, ?, ?, ?> recipe) {
        List<RecipeIngredient<ItemStack>> ingredients = new ArrayList<>(recipe.getInput().getSubIngredients());
        List<InventorySlot> slots = List.of(handler.getParent().queueInputSlot, handler.getParent().inputSlot);
        for (InventorySlot slot : slots) {
            for (RecipeIngredient<ItemStack> ingredient : ingredients) {
                if (ingredient.test(slot.get())) {
                    slot.shrink(0, ingredient.getCount());
                    ingredients.remove(ingredient);
                    break;
                }
            }
        }
    }

    public static boolean canAddMultiOutput(ManagedRecipeHandler<?, ?, ?> handler, BaseRecipe<?, ?, ?, List<ItemStack>, ?> recipe) {
        List<ItemStack> output = recipe.getOutput();
        return handler.getParent().queueOutputSlot.canAdd(0, output.get(0))
            && (output.size() <= 1 || handler.getParent().outputSlot.canAdd(0, output.get(1)));
    }

    public static void addMultiOutput(ManagedRecipeHandler<?, ?, ?> handler, BaseRecipe<?, ?, ?, List<ItemStack>, ?> recipe) {
        List<ItemStack> output = recipe.getOutput();
        handler.getParent().queueOutputSlot.add(0, output.get(0));
        if (output.size() > 1) {
            handler.getParent().outputSlot.add(0, output.get(1));
        }
        handler.getParent().ejectOutput();
    }
}
