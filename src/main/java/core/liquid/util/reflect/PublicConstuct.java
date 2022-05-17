package core.liquid.util.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PublicConstuct<T> {
    private final Constructor<T> c;
    private final boolean accessible;

    public PublicConstuct(Constructor<T> c) {
        this.c = c;
        accessible = c.trySetAccessible();
    }

    public T newInstance(Object... initParams) {
        if (!accessible) {
            throw new UnsupportedOperationException("The constructor isn't accessible");
        }

        try {
            return c.newInstance(initParams);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Constructor<T> unboxed() {
        return c;
    }

    @Override
    public String toString() {
        return c.toString();
    }
}
