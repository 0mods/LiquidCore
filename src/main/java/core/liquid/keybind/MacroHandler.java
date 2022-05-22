package core.liquid.keybind;

import net.minecraftforge.client.event.InputEvent;

public interface MacroHandler {
    void keyInput(InputEvent.KeyInputEvent event);

    void registryKey();
}
