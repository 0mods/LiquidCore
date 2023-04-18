package liquid.objects.model;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static liquid.objects.model.ResourceGenType.ITEM;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResourceGen {
    ResourceGenType type() default ITEM;
}
