package liquid.objects.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.function.Supplier;

public final class LogHelper {
    public static final String FATAL_MARKER_ID = "FATAL";
    public static final Marker FATAL = MarkerFactory.getMarker(FATAL_MARKER_ID);
    private static final StackWalker WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static Logger get(String modId) {
        return LoggerFactory.getLogger(modId);
    }

    public static Logger get() {
        return LoggerFactory.getLogger(WALKER.getCallerClass());
    }

    public static void configureRootLoggingLevel(final org.slf4j.event.Level level) {
        final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        final Configuration config = ctx.getConfiguration();
        final LoggerConfig loggerConfig = config.getLoggerConfig(LogManager.ROOT_LOGGER_NAME);
        loggerConfig.setLevel(convertLevel(level));
        ctx.updateLoggers();
    }

    private static Level convertLevel(final org.slf4j.event.Level level) {
        return switch (level) {
            case INFO -> Level.INFO;
            case WARN -> Level.WARN;
            case DEBUG -> Level.DEBUG;
            case ERROR -> Level.ERROR;
            case TRACE -> Level.TRACE;
        };
    }

    public static Object defer(final Supplier<Object> result) {
        class ToString {
            @Override
            public String toString() {
                return result.get().toString();
            }
        }

        return new ToString();
    }


}
