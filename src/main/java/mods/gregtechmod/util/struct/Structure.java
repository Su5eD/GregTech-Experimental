package mods.gregtechmod.util.struct;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Holds the definition of machine structures, including the abstract layout, relative vectors, and in-world metadata.
 * Constructing and matching the structure consists of 3 main phases:
 * <ul>
 *     <li>Pre-Initialization</li> The user defines the layout of the structure using a pattern, elements map and an instance factory function (see {@link #Structure(List, Map, BiFunction)})
 *     <li>Initialization</li></li> The elements map's keys are transformed into relative {@link Vec3iRotatable} positions.
 *     <li>Application</li> Upon request, using the mapped elements map, root {@link BlockPos} and {@link EnumFacing}, creates a new {@link WorldStructure}
 * </ul>
 * @param <T> Generic type of {@link WorldStructure#instance}
 */
public class Structure<T> {
    private final Map<Character, Collection<StructureElement>> elements;
    public final List<List<String>> pattern;
    private final BiFunction<EnumFacing, Map<Character, Collection<BlockPos>>, T> factory;
    private final Consumer<T> onInvalidate;
    
    public final Collection<Triple<Character, Collection<StructureElement>, Collection<Vec3iRotatable>>> applied = new HashSet<>();
    
    private WorldStructure worldStructure;

    public Structure(List<List<String>> pattern, Map<Character, Collection<StructureElement>> elements, BiFunction<EnumFacing, Map<Character, Collection<BlockPos>>, T> factory) {
        this(pattern, elements, factory, instance -> {});
    }
    
    /**
     * @param pattern an abstract layout of the structure, in a format similar to the one of a crafting recipe. 
     *                Each element in the list represents a single layer (<b>bottom to top</b>), and every <code>String</code> inside it defines blocks.
     *                NOTE: max dimensions aren't calculated automatically, and you need to include empty spaces, too.
     * @param elements a map containing blockstate predicates for each block key (<code>char</code>) in the pattern, which are used to match them.
     *                 Keep in mind 'X' is a <b>reserved</b> key which is used for the root block of the structure.
     *                 The map shouldn't contain it.
     * @param factory used to construct {@link WorldStructure#instance}
     * @param onInvalidate Called before {@link #worldStructure} is invalidated if {@link WorldStructure#instance} isn't null
     */
    public Structure(List<List<String>> pattern, Map<Character, Collection<StructureElement>> elements, BiFunction<EnumFacing, Map<Character, Collection<BlockPos>>, T> factory, Consumer<T> onInvalidate) {
        this.elements = Collections.unmodifiableMap(elements);
        this.pattern = pattern;
        this.factory = factory;
        this.onInvalidate = onInvalidate;
        
        Vec3iRotatable root = findRoot(this.pattern);
        if (root == null) throw new RuntimeException("Could not find structure pattern root");
                
        Map<Character, Collection<Vec3iRotatable>> vecs = new HashMap<>();
        this.elements.forEach((id, element) -> vecs.put(id, new HashSet<>()));
        
        for (int y = 0; y < this.pattern.size(); y++) {
            List<String> layer = this.pattern.get(y);
            
            int size = layer.size();
            for (int z = 0; z < size; z++) {
                char[] row = layer.get(z).toCharArray();
                
                for (int x = 0; x < row.length; x++) {
                    char ch = row[x];
                    
                    if (ch != ' ' && ch != 'X') {
                        Collection<StructureElement> pred = this.elements.get(ch);
                        if (pred == null) throw new IllegalArgumentException(String.format("Unknown element identifier '%s' in structure pattern %s. Known identifiers: %s", ch, this.pattern, this.elements.keySet()));
                        
                        Vec3iRotatable vec = new Vec3iRotatable(x, y, z);
                        Vec3iRotatable relative = vec.relativise(root);
                        vecs.get(ch).add(relative);
                    }
                }
            }
        }
        
        vecs.forEach((id, vectors) -> this.applied.add(Triple.of(id, this.elements.get(id), vectors)));
    }
    
    private Vec3iRotatable findRoot(List<List<String>> pattern) {
        Vec3iRotatable root = null;
        for (int y = 0; y < pattern.size(); y++) {
            List<String> layer = pattern.get(y);
                
            int size = layer.size();
            for (int z = 0; z < size; z++) {
                char[] row = layer.get(z).toCharArray();
                    
                for (int x = 0; x < row.length; x++) {
                    char ch = row[x];
                        
                    if (ch == 'X') {
                        if (root == null) root = new Vec3iRotatable(x, y, z);
                        else throw new IllegalArgumentException("Duplicate structure root found");
                    }
                }
            }
        }
            
        return root;
    }
    
    public void checkWorldStructure(BlockPos root, EnumFacing facing) {
        if (this.worldStructure == null || this.worldStructure.facing != facing) {
            Collection<Triple<Character, Collection<StructureElement>, Collection<BlockPos>>> worldApplied = this.applied.stream()
                    .map(triple -> {
                        Collection<Vec3iRotatable> vecs = triple.getRight();
                        Collection<BlockPos> positions = vecs.stream()
                                .map(vec -> {
                                    Vec3i rotated = vec.rotateHorizontal(facing);
                                    return root.add(rotated);
                                })
                                .collect(Collectors.toSet());
                        return Triple.of(triple.getLeft(), triple.getMiddle(), positions);
                    })
                    .collect(Collectors.toSet());
            
            this.worldStructure = new WorldStructure(facing, worldApplied);
        }
        
        boolean oldValid = this.worldStructure.valid;
        boolean valid = this.worldStructure.elements.stream()
                .allMatch(triple -> {
                    Collection<StructureElement> elements = triple.getMiddle();
                    Map<StructureElement, Integer> counts = elements.stream()
                            .collect(Collectors.toMap(Function.identity(), e -> 0));

                    boolean validPositions = triple.getRight().stream()
                            .allMatch(pos -> elements.stream()
                                    .filter(e -> e.predicate.test(pos))
                                    .findFirst()
                                    .map(structElement -> {
                                        //noinspection ConstantConditions
                                        counts.compute(structElement, (e, count) -> count + 1);
                                        return true;
                                    })
                                    .orElse(false));
                    
                    boolean validCounts = counts.entrySet().stream()
                            .allMatch(entry -> {
                                StructureElement structElement = entry.getKey();
                                int count = entry.getValue();
                                return (structElement.minCount < 1 || count >= structElement.minCount) && (structElement.maxCount < 1 || count <= structElement.maxCount);
                            });
                    
                    return validPositions && validCounts;
                });
        
        if (valid) {
            if (!oldValid) {
                Map<Character, Collection<BlockPos>> elements = this.worldStructure.elements.stream()
                        .collect(Collectors.toMap(Triple::getLeft, Triple::getRight));
                this.worldStructure.valid = true;
                this.worldStructure.instance = this.factory.apply(this.worldStructure.facing, elements);
            }
        }
        else {
            this.worldStructure.valid = false;
            if (this.worldStructure.instance != null) this.onInvalidate.accept(this.worldStructure.instance);
            this.worldStructure.instance = null;
        }

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
        public final Collection<Triple<Character, Collection<StructureElement>, Collection<BlockPos>>> elements;
        private boolean valid;
        /**
         * Optional generic metadata object, present only when {@link #valid} is true and {@link #factory} returns a non-null value
         */
        private T instance;
        
        public WorldStructure(EnumFacing facing, Collection<Triple<Character, Collection<StructureElement>, Collection<BlockPos>>> elements) {
            this.facing = facing;
            this.elements = elements;
        }
        
        public boolean isValid() {
            return this.valid;
        }

        public T getInstance() {
            return this.instance;
        }
    }
}
