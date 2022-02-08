package dev.su5ed.gregtechmod.model;

import com.google.common.base.MoreObjects;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Property;
import one.util.streamex.IntStreamEx;
import one.util.streamex.StreamEx;
import org.apache.commons.lang3.builder.CompareToBuilder;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class DirectionsProperty extends Property<DirectionsProperty.DirectionsWrapper> {

    public DirectionsProperty(String name) {
        super(name, DirectionsWrapper.class);
    }

    @Override
    public Collection<DirectionsWrapper> getPossibleValues() {
        return DirectionsWrapper.VALUES;
    }

    @Override
    public String getName(DirectionsWrapper directions) {
        return StreamEx.of(directions.directions)
            .map(Direction::getName)
            .ifEmpty("none")
            .joining("_");
    }

    @Override
    public Optional<DirectionsWrapper> getValue(String pValue) {
        DirectionsWrapper directions;
        if (pValue.equals("none")) directions = DirectionsWrapper.from();
        else directions = DirectionsWrapper.from(
            StreamEx.of(pValue.split("_"))
                .map(String::toUpperCase)
                .map(Direction::valueOf)
                .toArray(Direction[]::new)
        );
        return Optional.of(directions);
    }

    public static class DirectionsWrapper implements Comparable<DirectionsWrapper> {
        public static final Collection<DirectionsWrapper> VALUES = calculateValues();

        private final Direction[] directions;

        private DirectionsWrapper(Direction[] directions) {
            this.directions = directions;
        }

        public static DirectionsWrapper from(Direction... directions) {
            return StreamEx.of(VALUES)
                .findFirst(wrapper -> Arrays.equals(directions, wrapper.directions))
                .orElseThrow(() -> new IllegalArgumentException("No cached value for " + Arrays.toString(directions) + " found"));
        }

        private static Collection<DirectionsWrapper> calculateValues() {
            return IntStreamEx.rangeClosed(0, 0b111111)
                .mapToObj(i -> IntStreamEx.range(Direction.values().length)
                    .filter(j -> (i & 1 << j) != 0)
                    .mapToObj(j -> Direction.values()[j])
                    .toArray(Direction[]::new))
                .map(DirectionsWrapper::new)
                .toImmutableList();
        }

        public boolean isSideConnected(Direction side) {
            return StreamEx.of(this.directions)
                .anyMatch(side::equals);
        }

        @Override
        public int compareTo(@Nonnull DirectionsWrapper o) {
            return new CompareToBuilder().append(this.directions, o.directions).toComparison();
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("directions", directions)
                .toString();
        }
    }
}
