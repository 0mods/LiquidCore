package core.liquid.util.reflect.provide;

import org.jetbrains.annotations.Nullable;

public enum ClassHandlers {
    JAVA(new JavaClassHolder());

    private final ClassHolder provider;

    ClassHandlers(ClassHolder provider) {
        this.provider = provider;
    }

    public ClassHolder get() {
        return provider;
    }

    @Nullable
    public static ClassHolder findHandler(Class<?> clazz) {
        for (ClassHandlers provider : ClassHandlers.values()) {
            if (provider.get().canHandle(clazz)) {
                return provider.get();
            }
        }

        return null;
    }
}
