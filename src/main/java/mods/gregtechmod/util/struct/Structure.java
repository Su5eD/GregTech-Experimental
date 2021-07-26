package mods.gregtechmod.util.struct;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Holds the definition of machine structures, including the abstract layout, relative vectors, and in-world metadata.
 * Constructing and matching the structure consists of 3 main phases:
 * <ul>
 *     <li>Initialization</li> The user defines the layout of the structure using a pattern, elements map and an instance factory function (see {@link #Structure(List, Map, Function)})
 *     <li>Construction</li> The elements map's keys are transformed into relative {@link Vec3iRotatable} positions.
 *     <li>Application</li> Upon request, using the mapped elements map, root {@link BlockPos} and {@link EnumFacing}, creates a new {@link WorldStructure}
 * </ul>
 * @param <T> Optional generic type of {@link WorldStructure#instance}
 */
public class Structure<T> {
    public final Map<Character, Predicate<BlockPos>> elements;
    public final List<List<String>> pattern;
    private final Function<Map<BlockPos, IBlockState>, T> factory;
    
    public final Map<Vec3iRotatable, Predicate<BlockPos>> applied = new HashMap<>();
    
    private WorldStructure worldStructure;

    /**
     * @param pattern an abstract layout of the structure, in a format similar to the one of a crafting recipe. 
     *                Each element in the list represents a single layer (<b>bottom to top</b>), and every <code>String</code> inside it defines blocks.
     *                NOTE: max dimensions aren't calculated automatically, and you need to include empty spaces, too.
     * @param elements a map containing blockstate predicates for each block key (<code>char</code>) in the pattern, which are used to match them.
     *                 Keep in mind 'X' is a <b>mandatory</b>, <b>reserved</b> key which is used for the root block of the structure.
     *                 The map shouldn't contain it.
     * @param factory used to construct {@link WorldStructure#instance}
     */
    public Structure(List<List<String>> pattern, Map<? extends Character, Predicate<BlockPos>> elements, Function<Map<BlockPos, IBlockState>, T> factory) {
        this.elements = Collections.unmodifiableMap(elements);
        this.pattern = pattern;
        this.factory = factory;
        
        Vec3iRotatable root = findRoot(this.pattern);
        if (root == null) throw new RuntimeException("Could not find structure pattern root");
                
        for (int y = 0; y < this.pattern.size(); y++) {
            List<String> layer = this.pattern.get(y);
            
            int size = layer.size();
            for (int z = 0; z < size; z++) {
                char[] row = layer.get(size - 1 - z).toCharArray();
                
                for (int x = 0; x < row.length; x++) {
                    char ch = row[x];
                    
                    if (ch != ' ' && ch != 'X') {
                        Predicate<BlockPos> pred = this.elements.get(ch);
                        if (pred == null) throw new IllegalArgumentException(String.format("Unknown element '%s' in structure pattern %s. Known elements: %s", ch, this.pattern, this.elements.keySet()));
                        
                        Vec3iRotatable vec = new Vec3iRotatable(x, y, z);
                        Vec3iRotatable relative = vec.relativise(root);
                        this.applied.put(relative, pred);
                    }
                }
            }
        }
    }
    
    private Vec3iRotatable findRoot(List<List<String>> pattern) {
        for (int y = 0; y < pattern.size(); y++) {
            List<String> layer = pattern.get(y);
                
            int size = layer.size();
            for (int z = 0; z < size; z++) {
                char[] row = layer.get(size - 1 - z).toCharArray();
                    
                for (int x = 0; x < row.length; x++) {
                    char ch = row[x];
                        
                    if (ch == 'X') return new Vec3iRotatable(x, y, z);
                }
            }
        }
            
        return null;
    }
    
    public WorldStructure checkWorldStructure(BlockPos root, EnumFacing facing, IBlockAccess world) {
        if (this.worldStructure == null || this.worldStructure.facing != facing) {
            Map<BlockPos, Predicate<BlockPos>> map = new HashMap<>();
            this.applied
                    .forEach((key, value) -> { 
                        Vec3i vec = key.rotateHorizontal(facing);
                        BlockPos pos = root.add(vec);
                        
                        map.put(pos, value);
                    });
            
            this.worldStructure = new WorldStructure(facing, map);
        }
        
        boolean oldValid = this.worldStructure.valid;
        boolean valid = this.worldStructure.positions.entrySet()
                .stream()
                .allMatch(entry -> entry.getValue().test(entry.getKey()));
        
        if (valid) {
            if (!oldValid) {
                // TODO only use positions
                Map<BlockPos, IBlockState> stateMap = this.worldStructure.positions.keySet()
                        .stream()
                        .collect(Collectors.toMap(Function.identity(), world::getBlockState));
                this.worldStructure.valid = true;
                this.worldStructure.instance = this.factory.apply(stateMap);
            }
        }
        else {
            this.worldStructure.valid = false;
            this.worldStructure.instance = null;
        }
        
        return this.worldStructure;
    }
    
    public Optional<WorldStructure> getWorldStructure() {
        return Optional.ofNullable(this.worldStructure);
    }
    
    public boolean isValid() {
        return getWorldStructure()
                .map(WorldStructure::isValid)
                .orElse(false);
    }

    /**
     * An in-world instance of this Structure.
     * This is cached based on the root position and facing.
     */
    public class WorldStructure {
        public final EnumFacing facing;
        public final Map<BlockPos, Predicate<BlockPos>> positions;
        private boolean valid;
        /**
         * Optional generic metadata object, present only when {@link #valid} is true and {@link #factory} returns a non-null value
         */
        private T instance;
        
        public WorldStructure(EnumFacing facing, Map<BlockPos, Predicate<BlockPos>> positions) {
            this.facing = facing;
            this.positions = positions;
        }
        
        public boolean isValid() {
            return this.valid;
        }

        public T getInstance() {
            return this.instance;
        }
    }
}
