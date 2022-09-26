package liquid.adapters.kotlin

import java.util.*
import java.util.function.Supplier

fun <T> sup(value: T) : Supplier<T> {
    return Supplier {
        value
    }
}

inline fun <reified T : Enum<T>, V> enumMap(): MutableMap<T, V> {
    return EnumMap(T::class.java)
}

inline fun <reified T : Enum<T>, V> enumMap(vararg pairs: Pair<T, V>): MutableMap<T, V> {
    return EnumMap<T, V>(T::class.java).apply { putAll(pairs) }
}

inline fun <reified T : Enum<T>> enumSet(): EnumSet<T> {
    return EnumSet.noneOf(T::class.java)
}

fun <T : Enum<T>> enumSet(e: T): EnumSet<T> {
    return EnumSet.of(e)
}

fun <T : Enum<T>> enumSet(e1: T, e2: T): EnumSet<T> {
    return EnumSet.of(e1, e2)
}

fun <T : Enum<T>> enumSet(e1: T, e2: T, e3: T): EnumSet<T> {
    return EnumSet.of(e1, e2, e3)
}

fun <T : Enum<T>> enumSet(e1: T, e2: T, e3: T, e4: T): EnumSet<T> {
    return EnumSet.of(e1, e2, e3, e4)
}

fun <T : Enum<T>> enumSet(e1: T, e2: T, e3: T, e4: T, e5: T): EnumSet<T> {
    return EnumSet.of(e1, e2, e3, e4, e5)
}

fun <T : Enum<T>> enumSet(first: T, vararg rest: T): EnumSet<T> {
    return EnumSet.of(first, *rest)
}