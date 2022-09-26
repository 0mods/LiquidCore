package liquid.adapters

import liquid.adapters.kotlin.forge.LOADING_CONTEXT
import net.minecraftforge.eventbus.api.IEventBus

class KModLoadingContext constructor(private val container: KModContainer) {
    fun getEventBus() : IEventBus {
        return container.eventBus
    }

    companion object {
        fun get() : KModLoadingContext {
            return LOADING_CONTEXT.extension()
        }
    }
}