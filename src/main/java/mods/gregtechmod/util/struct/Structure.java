package mods.gregtechmod.util.struct;

import com.google.common.primitives.Chars;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import one.util.streamex.EntryStream;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.tuple.Triple;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
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
    public static final Character STRUCTURE_ROOT = 'X'; 
    private static final List<Character> IGNORED = Arrays.asList(STRUCTURE_ROOT, ' ');
    
    private final Map<Character, Collection<StructureElement>> elements;
    private final List<List<String>> pattern;
    private final BiFunction<EnumFacing, Map<Character, Collection<BlockPos>>, T> factory;
    private final Consumer<T> onInvalidate;
    private final Collection<Triple<Character, Collection<StructureElement>, List<Vec3iRotatable>>> applied;
    
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
        this.pattern = Collections.unmodifiableList(pattern);
        this.factory = factory;
        this.onInvalidate = onInvalidate;
        
        Vec3iRotatable root = findRoot(this.pattern);
        this.applied = EntryStream.of(this.pattern)
                .mapValues(layer -> EntryStream.of(layer)
                        .mapValues(row -> EntryStream.of(Chars.asList(row.toCharArray()))
                                .removeValues(IGNORED::contains)))
                .flatMapKeyValue((y, layer) -> 
                        layer.flatMapKeyValue((z, row) -> row.invert()
                                .mapValues(x -> new Vec3iRotatable(x, y, z).relativise(root))
                        )
                )
                .mapToEntry(Entry::getKey, Entry::getValue)
                .sorted(Entry.comparingByKey())
                .collapseKeys()
                .mapKeyValue((ch, list) -> {
                    Collection<StructureElement> elms = Optional.ofNullable(this.elements.get(ch)).orElseThrow(() -> {
                        String message = String.format("Unknown element identifier '%s' in structure pattern %s. Known identifiers: %s", ch, this.pattern, this.elements.keySet());
                        return new IllegalArgumentException(message);
                    });
                    
                    StreamEx.of(elms)
                            .filter(element -> element.minCount == 0 && element.maxCount == 0)
                            .forEach(element -> element.minCount = element.maxCount = list.size());
                    
                    return Triple.of(ch, elms, list);
                })
                .toList();
    }
    
    private Vec3iRotatable findRoot(List<List<String>> pattern) {
        return EntryStream.of(pattern)
                .mapValues(EntryStream::of)
                .flatMapValues(layer -> 
                        layer.flatMapValues(row -> EntryStream.of(Chars.asList(row.toCharArray()))
                                .filterValues(STRUCTURE_ROOT::equals))
                )
                .mapKeyValue((y, layer) -> new Vec3iRotatable(layer.getValue().getKey(), y, layer.getKey()))
                .toListAndThen(list -> {
                    if (list.isEmpty()) throw new RuntimeException("Could not find structure pattern root");
                    else if (list.size() > 1) throw new RuntimeException("Duplicate structure root found");
                    else return list.get(0);
                });
    }
    
    public void checkWorldStructure(BlockPos root, EnumFacing facing) {
        if (this.worldStructure == null || this.worldStructure.facing != facing) {
            Collection<Triple<Character, Collection<StructureElement>, Collection<BlockPos>>> worldApplied = StreamEx.of(this.applied)
                    .map(triple -> {
                        Collection<BlockPos> positions = StreamEx.of(triple.getRight())
                                .map(vec -> {
                                    Vec3i rotated = vec.rotateHorizontal(facing);
                                    return root.add(rotated);
                                })
                                .toSet();
                        return Triple.of(triple.getLeft(), triple.getMiddle(), positions);
                    })
                    .toSet();
            
            this.worldStructure = new WorldStructure(facing, worldApplied);
        }
        
        boolean valid = this.worldStructure.elements.stream()
                .allMatch(triple -> StreamEx.of(triple.getMiddle())
                        .mapToEntry(element -> StreamEx.of(triple.getRight())
                                .filter(element.predicate)
                                .count())
                        .allMatch((element, count) -> (element.minCount < 1 || count >= element.minCount) && (element.maxCount < 1 || count <= element.maxCount)));
        
        if (valid) {
            if (!this.worldStructure.valid) {
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
     * This is cached based on the facing.
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
