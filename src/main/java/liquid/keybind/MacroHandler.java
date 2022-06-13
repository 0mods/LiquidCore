package liquid.keybind;

import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.InputEvent;

public interface MacroHandler {
    void keyInput(InputEvent.KeyInputEvent event);

    void registryKey();

    static Component build(String modid) {
        return Component.keybind("key.categories.mod." + modid);
    }

    static Component keyBuild(String name, String modid) {
        return Component.keybind("key." + modid + "." + name);
    }
}
