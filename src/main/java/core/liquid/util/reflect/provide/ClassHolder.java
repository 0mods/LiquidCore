package core.liquid.util.reflect.provide;

import core.liquid.util.reflect.PublicMethod;
import org.jetbrains.annotations.Nullable;

public interface ClassHolder {
    boolean canHandle(Class<?> clazz);

    <O, R> boolean isStatic(PublicMethod<O, R> method);

    <O, R> void requireStatic(PublicMethod<O, R> method);

    @Nullable <O, R> PublicMethod<O, R> findMethod(Class<O> clazz, String signature);

    @Nullable <O, R> Object invokeStaticMethod(PublicMethod<O, R> method, Object... args);
}
