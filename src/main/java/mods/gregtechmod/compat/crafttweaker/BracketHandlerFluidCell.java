package mods.gregtechmod.compat.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.zenscript.IBracketHandler;
import mods.gregtechmod.util.ProfileDelegate;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.natives.IJavaMethod;

import java.util.List;

@BracketHandler(priority = 50)
@ZenRegister
public class BracketHandlerFluidCell implements IBracketHandler {
    private final IJavaMethod method;

    public BracketHandlerFluidCell() {
        this.method = CraftTweakerAPI.getJavaMethod(BracketHandlerFluidCell.class, "getFluidCell", String.class);
    }

    @SuppressWarnings("unused")
    public static IItemStack getFluidCell(String fluid) {
        return CraftTweakerMC.getIItemStackWildcardSize(ProfileDelegate.getCell(fluid));
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        if (tokens.size() > 2 && tokens.get(0).getValue().equals("fluid_cell") && tokens.get(1).getValue().equals(":")) {
            return find(environment, tokens, 2, tokens.size());
        }
        return null;
    }

    private IZenSymbol find(IEnvironmentGlobal environment, List<Token> tokens, int startIndex, int endIndex) {
        StringBuilder valueBuilder = new StringBuilder();
        for (int i = startIndex; i < endIndex; i++) {
            Token token = tokens.get(i);
            valueBuilder.append(token.getValue());
        }
        return position -> new ExpressionCallStatic(position, environment, method, new ExpressionString(position, valueBuilder.toString()));
    }

    @Override
    public Class<?> getReturnedClass() {
        return IItemStack.class;
    }

    @Override
    public String getRegexMatchingString() {
        return "fluid_cell:.*";
    }
}
