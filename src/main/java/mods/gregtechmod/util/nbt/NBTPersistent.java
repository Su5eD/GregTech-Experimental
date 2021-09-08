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
    
    Class<? extends INBTSerializer<?, ?>> using() default Serializers.None.class;
    
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
