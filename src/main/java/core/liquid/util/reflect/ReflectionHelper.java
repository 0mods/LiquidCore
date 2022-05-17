package core.liquid.util.reflect;

import com.google.common.base.Joiner;
import core.liquid.LiquidCore;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;

public class ReflectionHelper {
    public static boolean isFinal(Field f) {
        return Modifier.isFinal(f.getModifiers());
    }

    public static boolean isStatic(Field f) {
        return Modifier.isStatic(f.getModifiers());
    }

    public static boolean isStatic(Method m) {
        return Modifier.isStatic(m.getModifiers());
    }

    @Nullable
    public static Class<?> createClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <O, T> Optional<PublicField<O, T>> findFieldSoftly(Class<O> clazz, String fieldName) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            return Optional.of(new PublicField<>(f));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static <O, T> PublicField<O, T> findField(Class<O> clazz, String fieldName) {
        try {
            Field f = clazz.getDeclaredField(fieldName);
            return new PublicField<>(f);
        } catch (Throwable e) {
            throw new RuntimeException("Can't retrieve field " + fieldName + " from class " + clazz, e);
        }
    }

    public static <O, T> Optional<PublicField<O, T>> findObfFieldSoftly(Class<O> clazz, String srgName) {
        try {
            Field f = ObfuscationReflectionHelper.findField(clazz, srgName);
            return Optional.of(new PublicField<>(f));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


    public static <T> Optional<PublicConstuct<T>> findConstructorSoftly(Class<T> clazz, Class<?>... params) {
        try {
            Constructor<T> constructor = ObfuscationReflectionHelper.findConstructor(clazz, params);
            return Optional.of(new PublicConstuct<>(constructor));
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public static void loadClass(Class<?> clazz) {
        loadClass(clazz.getName());
    }

    public static void loadClass(String className) {
        try {
            Class.forName(className);
        } catch (ClassNotFoundException e) {
            LiquidCore.log.error("Can't load class" + className + ", because it isn't found");
            throw new RuntimeException();
        }
    }

    public static <E extends Enum<E>> E[] getEnumValues(Class<E> enumClass) {
        return enumClass.getEnumConstants();
    }

    public static String getPrettySignature(Class<?> owner, String methodName, Class<?>... params) {
        return owner.getName() + "#" + getPrettySignature(methodName, params);
    }

    public static String getPrettySignature(String methodName, Class<?>... params) {
        return methodName + "(" + Joiner.on(",").join(params) + ")";
    }

    public static String getDescriptorForClass(final Class<?> c) {
        if (c.isPrimitive()) {
            if (c == byte.class)
                return "B";
            if (c == char.class)
                return "C";
            if (c == double.class)
                return "D";
            if (c == float.class)
                return "F";
            if (c == int.class)
                return "I";
            if (c == long.class)
                return "J";
            if (c == short.class)
                return "S";
            if (c == boolean.class)
                return "Z";
            if (c == void.class)
                return "V";
            throw new RuntimeException("Unrecognized primitive " + c);
        }
        if (c.isArray()) return c.getName().replace('.', '/');
        return ('L' + c.getName() + ';').replace('.', '/');
    }

    public static String getMethodSignature(Method m) {
        return m.getName() + getMethodDescriptor(m);
    }

    public static String getMethodDescriptor(Method m) {
        StringBuilder s = new StringBuilder("(");
        for (final Class<?> c : (m.getParameterTypes())) {
            s.append(getDescriptorForClass(c));
        }
        s.append(')');
        return s + getDescriptorForClass(m.getReturnType());
    }
}
