package dev.su5ed.gregtechmod.util.nbt;

import dev.su5ed.gregtechmod.api.util.NBTTarget;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NBTPersistent {
    NBTTarget target() default NBTTarget.SAVE;

    String name() default "";

    /**
     * Specifies the handler of this field. Don't use in combination with {@link #serializer()} or {@link #deserializer()} as their value will be overwritten with this.
     */
    Class<? extends NBTHandler<?, ?, ?>> handler() default Serializers.None.class;

    Class<? extends NBTSerializer<?, ?>> serializer() default Serializers.None.class;

    Class<? extends NBTDeserializer<?, ?, ?>> deserializer() default Serializers.None.class;

    Include include() default Include.ALWAYS;

    enum Include {
        ALWAYS(o -> true),
        NON_NULL(Objects::nonNull),
        NOT_EMPTY(o -> o instanceof Collection<?> c ? !c.isEmpty() : o instanceof Map<?, ?> m ? !m.isEmpty() : o instanceof String s ? !s.isEmpty() : o != null);

        public final Predicate<Object> predicate;

        Include(Predicate<Object> predicate) {
            this.predicate = predicate;
        }
    }
}
