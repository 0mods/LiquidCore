package liquid.adapters

import liquid.LiquidCore
import liquid.adapters.kotlin.*
import net.minecraftforge.eventbus.EventBusErrorMessage
import net.minecraftforge.eventbus.api.BusBuilder
import net.minecraftforge.eventbus.api.Event
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.eventbus.api.IEventListener
import net.minecraftforge.fml.ModContainer
import net.minecraftforge.fml.ModLoadingException
import net.minecraftforge.fml.ModLoadingStage
import net.minecraftforge.fml.event.IModBusEvent
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.ModFileScanData
import java.util.*
import java.util.function.Consumer

class KModContainer(info: IModInfo, className: String, private val scanData: ModFileScanData, gLayer: ModuleLayer)
    :ModContainer(info){
    val eventBus: IEventBus
    private var modInstance: Any?=null
    private val modClass: Class<*>
    private val LOGGER = LiquidCore.log;

    init {
        LOGGER.debug("Initializing ModContainer for $className")

        activityMap[ModLoadingStage.CONSTRUCT] = Runnable(::buildMod)
        val builder = BusBuilder.builder().setExceptionHandler(::onFailed).build()
    }

    init {
        LOGGER.debug( "Creating KotlinModContainer instance for $className")

        activityMap[ModLoadingStage.CONSTRUCT] = Runnable(::buildMod)
        val builder = BusBuilder.builder().setExceptionHandler(::onFailed).setTrackPhases(false).markerType(
            IModBusEvent::class.java)
        eventBus = try {
            val busBuilder = Class.forName("net.minecraftforge.eventbus.BusBuilder")
            val m = busBuilder.getDeclaredMethod("useModLauncher")
            m.invoke(builder)
            builder.build()
        } catch (e: NoSuchMethodException) {
            builder.build()
        }

        configHandler = Optional.of(Consumer { event ->
            eventBus.post(event.self())
        })

        contextExtension = sup(KModLoadingContext(this))

        try {
            val layer = gLayer.findModule(info.owningFile.moduleName()).orElseThrow()
            modClass = Class.forName(layer, className)
            LOGGER.trace( "Loaded modclass ${modClass.name} with ${modClass.classLoader}")
        } catch (t: Throwable) {
            LOGGER.error( "Failed to load class $className", t)
            throw ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", t)
        }
    }

    private fun onFailed(iEventBus: IEventBus, event: Event, listeners: Array<IEventListener>, busId: Int, throwable: Throwable) {
        LOGGER.error(EventBusErrorMessage(event, busId, listeners, throwable).toString())
    }

    private fun buildMod() {
        try {
            LOGGER.trace( "Loading mod instance ${getModId()} of type ${modClass.name}")
            modInstance = modClass.kotlin.objectInstance ?: modClass.getDeclaredConstructor().newInstance()
            LOGGER.trace( "Loaded mod instance ${getModId()} of type ${modClass.name}")
        } catch (throwable: Throwable) {
            LOGGER.error( "Failed to create mod instance. ModID: ${getModId()}, class ${modClass.name}", throwable)
            throw ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", throwable, modClass)
        }

        try {
            LOGGER.trace( "Injecting Automatic Kotlin event subscribers for ${getModId()}")
            // Inject into object EventBusSubscribers
            KEventBusSubscriber.inject(this, scanData, modClass.classLoader)
            LOGGER.trace( "Completed Automatic Kotlin event subscribers for ${getModId()}")
        } catch (throwable: Throwable) {
            LOGGER.error( "Failed to register Automatic Kotlin subscribers. ModID: ${getModId()}, class ${modClass.name}", throwable)
            throw ModLoadingException(modInfo, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmod", throwable, modClass)
        }
    }

    override fun matches(mod: Any?): Boolean {
        return mod == modInstance
    }

    override fun getMod(): Any? = modInstance

    public override fun <T> acceptEvent(e: T) where T : Event, T : IModBusEvent {
        try {
            LOGGER.debug("Firing event for modid $modId : $e")
            eventBus.post(e)
            LOGGER.debug("Fired event for modid $modId : $e")
        } catch (t: Throwable) {
            LOGGER.error("Caught exception during event $e dispatch for modid $modId", t)
            throw ModLoadingException(modInfo, modLoadingStage, "fml.modloading.errorduringevent", t)
        }
    }
}