package liquid.keys;

import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.common.MinecraftForge;

@Deprecated
public abstract class MacroCaller implements MacroHandler {
    public MacroCaller() {
        MinecraftForge.EVENT_BUS.addListener(this::keyEventCaller);
    }

    private void keyEventCaller(InputEvent.Key e) {
        this.keyInput(e);
    }
}
