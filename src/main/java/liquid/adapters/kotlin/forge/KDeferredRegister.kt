@file:Suppress("NOTHING_TO_INLINE")

package liquid.adapters.kotlin.forge

import net.minecraftforge.registries.DeferredRegister
import java.util.function.Supplier
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

typealias KDeferredRegister<T> = DeferredRegister<T>

inline fun <V, T : V> KDeferredRegister<V>.registerObject(name: String, noinline supplier: ()-> T)
        : ReadOnlyProperty<Any?, T> {
    val registryObject = this.register(name, supplier)

    return object : ReadOnlyProperty<Any?, T>, Supplier<T>, ()-> T {
        override fun getValue(thisRef: Any?, property: KProperty<*>): T = get()

        override fun get(): T = registryObject.get()

        override fun invoke(): T = get()
    }
}