package dev.su5ed.gtexperimental.compat.jei;

import dev.su5ed.gtexperimental.compat.ModHandler;
import dev.su5ed.gtexperimental.object.Tool;
import dev.su5ed.gtexperimental.util.ItemProvider;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import one.util.streamex.StreamEx;

import static dev.su5ed.gtexperimental.api.Reference.location;

@JeiPlugin
public class JEIModule implements IModPlugin {
    public static final ResourceLocation NAME = location("jei");

    @Override
    public ResourceLocation getPluginUid() {
        return NAME;
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        Item[] electricItems = StreamEx.of(Tool.values())
            .map(ItemProvider::getItem)
            .filter(ModHandler::isEnergyItem)
            .toArray(Item[]::new);
        registration.useNbtForSubtypes(electricItems);
    }
}
