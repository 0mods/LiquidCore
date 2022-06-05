package liquid.objects.annotations;

import net.minecraftforge.fml.config.ModConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * This function is currently in development.
 * Automatically added "of" method.
 *
 * Usage:
 *
 * &#064;Config(type  = Type.CLIENT)
 * public static ConfigClient CLIENT;
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value={FIELD})
public @interface Config {
    ModConfig.Type type();
     boolean reloadable() default false;
}
