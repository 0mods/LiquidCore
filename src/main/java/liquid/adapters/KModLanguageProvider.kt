@file:Suppress("MemberVisibilityCanBePrivate", "PropertyName", "PrivatePropertyName", "HasPlatformType",
    "UNCHECKED_CAST"
)

package liquid.adapters

import liquid.LiquidCore
import net.minecraftforge.fml.ModLoadingException
import net.minecraftforge.fml.ModLoadingStage
import net.minecraftforge.forgespi.language.ILifecycleEvent
import net.minecraftforge.forgespi.language.IModInfo
import net.minecraftforge.forgespi.language.IModLanguageProvider
import net.minecraftforge.forgespi.language.ModFileScanData
import org.objectweb.asm.Type
import org.slf4j.Logger
import java.lang.reflect.InvocationTargetException
import java.util.function.Consumer
import java.util.function.Supplier

class KModLanguageProvider : IModLanguageProvider {
    override fun name() = "java/liquid"
    val MOD_ANNOTATION: Type = Type.getType("Lnet/minecraftforge/fml/common/Mod;")
    private val LOGGER : Logger = LiquidCore.log;

    override fun getFileVisitor(): Consumer<ModFileScanData> {
        return Consumer { scanData ->
            scanData.addLanguageLoader(scanData.annotations.filter { data ->
                data.annotationType == MOD_ANNOTATION
            }.associate { data ->
                val modid = data.annotationData["value"] as String
                val modClass = data.clazz.className

                LOGGER.debug("Found @Mod class $modClass with mod id $modid")
                modid to KotlinModTarget(modClass)
            })
        }
    }

    override fun <R : ILifecycleEvent<R>?> consumeLifecycleEvent(consumeEvent: Supplier<R>?) {}

    class KotlinModTarget(private val className: String) : IModLanguageProvider.IModLanguageLoader {
        val LOGGER = LiquidCore.log;
        override fun <T> loadMod(info: IModInfo, modFileScanResults: ModFileScanData, gameLayer: ModuleLayer): T {

            try {
                val ktContainer = Class.forName("KModContainer", true,
                    Thread.currentThread().contextClassLoader)
                LOGGER.debug("Loading KModContainer from classloader ${Thread.currentThread()
                    .contextClassLoader} - got ${ktContainer.classLoader}}")
                val constructor = ktContainer.getConstructor(IModInfo::class.java, String::class.java,
                    ModFileScanData::class.java, ModuleLayer::class.java)
                return constructor.newInstance(info, className, modFileScanResults, gameLayer) as T
            } catch (e: InvocationTargetException) {
                LOGGER.error("Failed to build mod", e)

                val targetException = e.targetException

                if (targetException is ModLoadingException) {
                    throw targetException
                } else {
                    throw ModLoadingException(info, ModLoadingStage.CONSTRUCT, "fml.modloading.failedtoloadmodclass", e)
                }
            } catch (e: NoSuchMethodException) {
                bbMod(info, e)
            } catch (e: ClassNotFoundException) {
                bbMod(info, e)
            } catch (e: InstantiationException) {
                bbMod(info, e)
            } catch (e: IllegalAccessException) {
                bbMod(info, e)
            }
        }

        private fun bbMod(info: IModInfo, exception: Exception): Nothing {
            LOGGER.error("Unable to load KModContainer, what???????", exception)

            val mle = Class.forName("net.minecraftforge.fml.ModLoadingException", true, Thread.currentThread().contextClassLoader) as Class<RuntimeException>
            val mls = Class.forName("net.minecraftforge.fml.ModLoadingStage", true, Thread.currentThread().contextClassLoader) as Class<ModLoadingStage>
            throw mle.getConstructor(IModInfo::class.java, mls, String::class.java, Throwable::class.java)
                .newInstance(info, java.lang.Enum.valueOf(mls, "CONSTRUCT"),
                    "fml.modloading.failedtoloadmodclass", exception)
        }
    }
}