package core.liquid.keybind;

import core.liquid.LiquidCore;
import net.minecraftforge.client.event.InputEvent;

public interface MacroHandler {

    void keyInput(InputEvent.KeyInputEvent event);

    void registryKey();
}
