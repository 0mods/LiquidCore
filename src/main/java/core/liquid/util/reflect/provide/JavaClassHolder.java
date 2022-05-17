package core.liquid.util.reflect.provide;

import core.liquid.util.reflect.PublicMethod;
import core.liquid.util.reflect.ReflectionHelper;

import java.lang.reflect.Method;

public class JavaClassHolder implements ClassHolder {
    @Override
    public boolean canHandle(Class<?> clazz) {
        return true;
    }

    @Override
    public <O, R> boolean isStatic(PublicMethod<O, R> method) {
        return method.isStatic();
    }

    @Override
    public <O, R> void requireStatic(PublicMethod<O, R> method) {
        if (!isStatic(method)) {
            throw new IllegalArgumentException("Method " + method + " should be static!");
        }
    }

    @Override
    public <O, R> PublicMethod<O, R> findMethod(Class<O> clazz, String signature) {
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            if (ReflectionHelper.getMethodSignature(declaredMethod).equals(signature)) {
                return new PublicMethod<>(declaredMethod);
            }
        }
        return null;
    }

    @Override
    public <O, R> Object invokeStaticMethod(PublicMethod<O, R> method, Object... args) {
        return method.invoke(null, args);
    }
}
