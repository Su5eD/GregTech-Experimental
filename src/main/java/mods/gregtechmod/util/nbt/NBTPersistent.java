package mods.gregtechmod.util.nbt;

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
    String name() default "";
    
    /**
     * Specifies the handler of this field. Don't use in combination with {@link #serializer()} or {@link #deserializer()} as their value will be overwritten with this.
     */
    Class<? extends INBTHandler<?, ?>> handler() default Serializers.None.class;
    
    Class<? extends INBTSerializer<?, ?>> serializer() default Serializers.None.class;
    
    Class<? extends INBTDeserializer<?, ?>> deserializer() default Serializers.None.class;
    
    Include include() default Include.ALWAYS;
    
    enum Include {
        ALWAYS(o -> true),
        NON_NULL(Objects::nonNull),
        NOT_EMPTY(o -> o instanceof Collection ? !((Collection<?>) o).isEmpty() : o instanceof Map ? !((Map<?, ?>) o).isEmpty() : o instanceof String ? !((String) o).isEmpty() : o != null);
        
        public final Predicate<Object> predicate;

        Include(Predicate<Object> predicate) {
            this.predicate = predicate;
        }
    }
}
