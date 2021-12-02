package mods.gregtechmod.util.struct;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import net.minecraft.block.properties.PropertyHelper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Rotor implements Comparable<Rotor>, IStringSerializable {
    public static final Rotor DISABLED = new Rotor(null, null, false);
    public static final List<String> TEXTURE_PARTS = Arrays.asList(
            "top_left", "top_mid", "top_right",
            "mid_left", "mid_right",
            "bot_left", "bot_mid", "bot_right"
    );
    public static final Collection<Rotor> VALUES;

    static {
        Collection<Rotor> baseValues = Arrays.stream(EnumFacing.HORIZONTALS)
                .flatMap(facing -> TEXTURE_PARTS.stream()
                        .flatMap(str -> Stream.of(new Rotor(facing, str, false), new Rotor(facing, str, true)))
                )
                .collect(Collectors.toCollection(ArrayList::new));
        baseValues.add(DISABLED);
        VALUES = Collections.unmodifiableCollection(baseValues);
    }

    public final EnumFacing side;
    private final String texture;
    private final boolean active;

    private Rotor(EnumFacing side, String texture, boolean active) {
        this.side = side;
        this.texture = texture;
        this.active = active;
    }

    public static Rotor getRotor(EnumFacing facing, String texture, boolean active) {
        return VALUES.stream()
                .filter(rotor -> rotor.side == facing && rotor.texture.equals(texture) && rotor.active == active)
                .findFirst()
                .orElse(Rotor.DISABLED);
    }

    public String getTexture() {
        return "rotor_" + texture + getActive();
    }

    @Override
    public String getName() {
        return this == DISABLED ? "disabled" : this.side.getName() + "_" + this.texture + getActive();
    }
    
    private String getActive() {
        return this.active ? "_active" : "";
    }

    @Override
    public int compareTo(Rotor other) {
        return ObjectUtils.compare(this.side, other.side) - ObjectUtils.compare(this.texture, other.texture);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("side", this.side)
                .add("texture", this.texture + getActive())
                .add("active", this.active)
                .toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        else if (other == null || getClass() != other.getClass()) return false;
        Rotor rotor = (Rotor) other;
        return this.active == rotor.active && this.side == rotor.side && Objects.equals(this.texture, rotor.texture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.side, this.texture, this.active);
    }

    @SuppressWarnings("Guava")
    public static class PropertyRotor extends PropertyHelper<Rotor> {

        public PropertyRotor() {
            super("turbine_rotor", Rotor.class);
        }

        @Override
        public Collection<Rotor> getAllowedValues() {
            return Rotor.VALUES;
        }

        @Override
        public Optional<Rotor> parseValue(String value) {
            if (value.isEmpty() || value.equals("disabled")) return Optional.of(Rotor.DISABLED);
            
            int firstSeparator = value.indexOf('_');
            boolean active = value.endsWith("_active");
            String facingName = value.substring(0, firstSeparator);
            String texture = active ? value.substring(firstSeparator + 1, value.lastIndexOf('_')) : value.substring(firstSeparator + 1);
            
            EnumFacing facing = EnumFacing.byName(facingName);
            if (facing == null && texture.equals("null") && !active) return Optional.of(Rotor.DISABLED);
            else {
                for (Rotor rotor : Rotor.VALUES) {
                    if (rotor.side == facing && rotor.texture.equals(texture) && rotor.active == active) return Optional.of(rotor);
                }
            }
            return Optional.absent();
        }

        @Override
        public String getName(Rotor value) {
            return value.getName();
        }
    }
}
