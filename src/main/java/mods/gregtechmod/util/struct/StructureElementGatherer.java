package mods.gregtechmod.util.struct;

import mods.gregtechmod.util.GtUtil;
import mods.gregtechmod.util.LazyValue;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class StructureElementGatherer {
    private final LazyValue<IBlockAccess> world;
    private final Map<Character, Collection<StructureElement>> elements = new HashMap<>();
    
    public StructureElementGatherer(Supplier<IBlockAccess> worldSupplier) {
        this.world = new LazyValue<>(worldSupplier);
    }
    
    public StructureElementGatherer block(char id, Block... blocks) {
        return id(id, builder -> builder.block(blocks));
    }
    
    public StructureElementGatherer tileEntity(char id, Class<? extends TileEntity> tileEntity, int minCount) {
        return tileEntity(id, tileEntity, minCount, 0);
    }
    
    public StructureElementGatherer tileEntity(char id, Class<? extends TileEntity> tileEntity, int minCount, int maxCount) {
        return id(id, builder -> builder.tileEntity(tileEntity));
    }
    
    public StructureElementGatherer predicate(char id, Predicate<BlockPos> predicate) {
        return id(id, builder -> builder.predicate(predicate));
    }
    
    public StructureElementGatherer id(char id, Consumer<ElementBuilder> consumer) {
        ElementBuilder gatherer = new ElementBuilder();
        consumer.accept(gatherer);
        this.elements.put(id, gatherer.gather());
        return this;
    }

    public Map<Character, Collection<StructureElement>> gather() {
        return Collections.unmodifiableMap(elements);
    }
    
    public class ElementBuilder {
        private final Set<StructureElement> elements = new HashSet<>();
        
        public ElementBuilder block(Block... blocks) {
            this.elements.add(new StructureElement(pos -> GtUtil.findBlock(world.get(), pos, blocks)));
            return this;
        }
        
        public ElementBuilder tileEntity(Class<? extends TileEntity> tileEntity) {
            return tileEntity(tileEntity, 0);
        }
        
        public ElementBuilder tileEntity(Class<? extends TileEntity> tileEntity, int minCount) {
            return tileEntity(tileEntity, minCount, 0);
        }
        
        public ElementBuilder tileEntity(Class<? extends TileEntity> tileEntity, int minCount, int maxCount) {
            element(new StructureElement(pos -> GtUtil.findTileEntity(world.get(), pos, tileEntity), minCount, maxCount));
            return this;
        }
        
        public ElementBuilder predicate(Predicate<BlockPos> predicate) {
            return element(new StructureElement(predicate));
        }
        
        public ElementBuilder element(StructureElement element) {
            this.elements.add(element);
            return this;
        }
        
        public Collection<StructureElement> gather() {
            return Collections.unmodifiableSet(this.elements);
        }
    }
}
