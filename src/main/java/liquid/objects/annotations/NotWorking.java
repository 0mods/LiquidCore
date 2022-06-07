package liquid.objects.annotations;

import java.lang.annotation.*;
import static java.lang.annotation.ElementType.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(value={CONSTRUCTOR, FIELD, LOCAL_VARIABLE, METHOD, PACKAGE, PARAMETER, TYPE})
public @interface NotWorking {
    /**
    * This annotation working like {@link java.lang.Deprecated}, but when this class annotated,
    * element not considered deprecated, and just temporarily not working.
    */
    boolean deprecated() default false;

    Deprecated deprecation() default @Deprecated;

    boolean toFix() default false;
}
