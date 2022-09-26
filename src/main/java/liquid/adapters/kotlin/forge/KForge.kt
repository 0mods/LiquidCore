package liquid.adapters.kotlin.forge

import liquid.adapters.KModLoadingContext
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.eventbus.api.EventPriority
import net.minecraftforge.eventbus.api.GenericEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.loading.FMLEnvironment
import java.util.function.Consumer
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

inline val FORGE_BUS: IEventBus
    get() = MinecraftForge.EVENT_BUS

inline val MOD_BUS: IEventBus
    get() = KModLoadingContext.get().getEventBus()

inline val MOD_CONTEXT: KModLoadingContext
    get() = KModLoadingContext.get()

inline val LOADING_CONTEXT: ModLoadingContext
    get() = ModLoadingContext.get()

inline val DIST: Dist
    get() = FMLEnvironment.dist

fun <T> callWhenOn(dist: Dist, toRun: () -> T): T? {
    return if (DIST == dist) {
        try {
            toRun()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    } else null
}

inline fun runWhenOn(dist: Dist, toRun: () -> Unit) {
    if (DIST == dist) {
        toRun()
    }
}

inline fun <T> runForDist(clientTarget: () -> T, serverTarget: () -> T): T {
    return if (DIST == Dist.CLIENT) {
        clientTarget()
    } else {
        serverTarget()
    }
}

fun registerConfig(type: ModConfig.Type, spec: ForgeConfigSpec, fileName: String) {
    LOADING_CONTEXT.registerConfig(type, spec, fileName)
}

fun registerConfig(type: ModConfig.Type, spec: ForgeConfigSpec) {
    LOADING_CONTEXT.registerConfig(type, spec)
}

inline fun <T : GenericEvent<out F>, reified F> IEventBus.addGenericListener(
    listener: Consumer<T>,
    priority: EventPriority = EventPriority.NORMAL,
    receiveCancelled: Boolean = false
) {
    addGenericListener(F::class.java, priority, receiveCancelled, listener)
}

fun <T> lazySidedDelegate(clientValue: () -> T, serverValue: () -> T): ReadOnlyProperty<Any?, T> {
    return LazySidedDelegate(clientValue, serverValue)
}

fun <T> sidedDelegate(clientValue: () -> T, serverValue: () -> T): ReadOnlyProperty<Any?, T> {
    return SidedGetterDelegate(clientValue, serverValue)
}

private class LazySidedDelegate<T>(clientValue: () -> T, serverValue: () -> T) : ReadOnlyProperty<Any?, T> {
    private val clientValue by lazy(clientValue)
    private val serverValue by lazy(serverValue)

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return when (DIST) {
            Dist.CLIENT -> clientValue
            Dist.DEDICATED_SERVER -> serverValue
        }
    }
}

private class SidedGetterDelegate<T>(private val clientValue: () -> T, private val serverValue: () -> T) :
    ReadOnlyProperty<Any?, T> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return runForDist(clientValue, serverValue)
    }
}