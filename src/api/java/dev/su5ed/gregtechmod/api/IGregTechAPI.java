package dev.su5ed.gregtechmod.api;

import com.fasterxml.jackson.databind.JsonNode;
import dev.su5ed.gregtechmod.api.util.SonictronSound;
import dev.su5ed.gregtechmod.api.util.TurbineRotor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface IGregTechAPI {
    void addJackHammerMinableBlock(ItemStack stack);
    void addJackHammerMinableBlocks(Collection<ItemStack> stacks);
    Collection<ItemStack> getJackHammerMinableBlocks();
    
    void registerSonictronSound(SonictronSound sound);
    void registerSonictronSounds(Collection<SonictronSound> sounds);
    List<SonictronSound> getSonictronSounds();
    
    void registerCondition(String type, Predicate<JsonNode> predicate);
    boolean testCondition(String type, JsonNode node);
        
    void registerTurbineRotor(ItemStack stack, int efficiency, int efficiencyMultiplier, int damageToComponent);
    
    Optional<TurbineRotor> getTurbineRotor(ItemStack stack);
    
    void addComputerCubeGuidePage(String translationKey, int length, List<ItemStack> stacks);
    
    void registerWrench(Item item);
    void registerWrench(ItemStack stack);
    Collection<ItemStack> getWrenches();
    
    void registerScrewdriver(Item item);
    void registerScrewdriver(ItemStack stack);
    Collection<ItemStack> getScrewdrivers();
    
    void registerSoftHammer(Item item);
    void registerSoftHammer(ItemStack stack);
    Collection<ItemStack> getSoftHammers();
    
    void registerHardHammer(Item item);
    void registerHardHammer(ItemStack stack);
    Collection<ItemStack> getHardHammers();
    
    void registerCrowbar(Item item);
    void registerCrowbar(ItemStack stack);
    Collection<ItemStack> getCrowbars();
}
