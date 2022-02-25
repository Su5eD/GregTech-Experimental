package dev.su5ed.gregtechmod.api;

import com.fasterxml.jackson.databind.JsonNode;
import dev.su5ed.gregtechmod.api.item.TurbineRotor;
import dev.su5ed.gregtechmod.api.util.SonictronSound;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IGregTechAPI {
    void registerSonictronSound(SonictronSound sound);
    void registerSonictronSounds(Collection<SonictronSound> sounds);
    List<SonictronSound> getSonictronSounds();
    String getSoundFor(ItemStack stack);
    
    void registerCondition(String type, Predicate<JsonNode> predicate);
    boolean testCondition(String type, JsonNode node);
        
    <T extends Item & TurbineRotor> void registerTurbineRotor(T rotor);
    
    Optional<TurbineRotor> getTurbineRotor(Item item);
    
    void addComputerCubeGuidePage(String translationKey, int length, List<ItemStack> stacks);
}
