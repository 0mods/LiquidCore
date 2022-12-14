@file:Suppress("PropertyName", "Unused")

package liquid.adapters

import liquid.LiquidCore
import liquid.adapters.kotlin.enumSet
import liquid.adapters.kotlin.forge.DIST
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation
import net.minecraftforge.forgespi.language.ModFileScanData
import org.objectweb.asm.Type

object KEventBusSubscriber {
    private val EVENT_BUS_SUBSCRIBER: Type = Type.getType(Mod.EventBusSubscriber::class.java)
    private val LOGGER = LiquidCore.log;

    private val DIST_ENUM_HOLDERS = listOf(
        ModAnnotation.EnumHolder(null, "CLIENT"),
        ModAnnotation.EnumHolder(null, "DEDICATED_SERVER")
    )

    fun inject(mod: KModContainer, scanData: ModFileScanData, classLoader: ClassLoader) {
        LOGGER.debug("Attempting to inject @EventBusSubscriber kotlin objects in to the event bus for ${mod.modId}")

        val data = scanData.annotations.filter { annotationData ->
            EVENT_BUS_SUBSCRIBER == annotationData.annotationType
        }

        for (annotationData in data) {
            val sidesValue = annotationData.annotationData.getOrDefault("value", DIST_ENUM_HOLDERS) as List<ModAnnotation.EnumHolder>
            val sides = enumSet<Dist>().plus(sidesValue.map { eh -> Dist.valueOf(eh.value) })
            val modid = annotationData.annotationData.getOrDefault("modid", mod.modId)
            val busTargetHolder = annotationData.annotationData.getOrDefault("bus", ModAnnotation.EnumHolder(null, "FORGE")) as ModAnnotation.EnumHolder
            val busTarget = Mod.EventBusSubscriber.Bus.valueOf(busTargetHolder.value)

            if (mod.modId == modid && DIST in sides) {
                val kClass = Class.forName(annotationData.clazz.className, true, classLoader).kotlin

                var ktObject: Any?

                try {
                    ktObject = kClass.objectInstance
                } catch (unsupported: UnsupportedOperationException) {
                    if (unsupported.message?.contains("file facades") == false) {
                        throw unsupported
                    } else {
                        LOGGER.debug("Auto-subscribing kotlin file ${annotationData.annotationType.className} to $busTarget")
                        registerTo(kClass.java, busTarget, mod)
                        continue
                    }
                }

                if (ktObject != null) {
                    try {
                        LOGGER.debug("Auto-subscribing kotlin object ${annotationData.annotationType.className} to $busTarget")

                        registerTo(ktObject, busTarget, mod)
                    } catch (e: Throwable) {
                        LOGGER.error("Failed to load mod class ${annotationData.annotationType} for @EventBusSubscriber annotation", e)
                        throw RuntimeException(e)
                    }
                }
            }
        }
    }

    private fun registerTo(any: Any, target: Mod.EventBusSubscriber.Bus, mod: KModContainer) {
        if (target == Mod.EventBusSubscriber.Bus.FORGE) {
            target.bus().get().register(any)
        } else {
            mod.eventBus.register(any)
        }
    }
}