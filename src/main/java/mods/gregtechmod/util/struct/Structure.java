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

public class Structure<T> {
    public final Map<Character, Predicate<IBlockState>> elements;
    public final List<List<String>> pattern;
    private final Function<Collection<IBlockState>, T> factory;
    
    public final Map<Vec3iRotatable, Predicate<IBlockState>> applied = new HashMap<>();
    
    private WorldStructure worldStructure;
    
    public Structure(List<List<String>> pattern, Map<? extends Character, Predicate<IBlockState>> elements, Function<Collection<IBlockState>, T> factory) {
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
                        Predicate<IBlockState> pred = this.elements.get(ch);
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
            Map<BlockPos, Predicate<IBlockState>> map = new HashMap<>();
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
                .allMatch(entry -> {
                    IBlockState state = world.getBlockState(entry.getKey());
                    return entry.getValue().test(state);
                });
        
        if (valid) {
            if (!oldValid) {
                Collection<IBlockState> states = this.worldStructure.positions.keySet()
                        .stream()
                        .map(world::getBlockState)
                        .collect(Collectors.toList());
                this.worldStructure.valid = true;
                this.worldStructure.instance = this.factory.apply(states);
            }
        }
        else {
            this.worldStructure.valid = false;
            this.worldStructure.instance = null;
        }
        
        return this.worldStructure;
    }
    
    public class WorldStructure {
        public final EnumFacing facing;
        public final Map<BlockPos, Predicate<IBlockState>> positions;
        public boolean valid;
        public T instance;
        
        public WorldStructure(EnumFacing facing, Map<BlockPos, Predicate<IBlockState>> positions) {
            this.facing = facing;
            this.positions = positions;
        }
    }
}
