package liquid.keybind;

import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.InputEvent;

public interface MacroHandler {
    void keyInput(InputEvent.Key event);

    void registryKey();

    static Component category(String modid) {
        return Component.keybind("key.categories.mod." + modid);
    }

    static Component key(String name, String modid) {
        return Component.keybind("key." + modid + "." + name);
    }

    @Deprecated
    static String build(String modid) {
        return String.format("key.categories.mod.%s", modid);
    }

    @Deprecated
    static String keyBuild(String name, String modid) {
        return String.format("key.%s.%s", modid, name);
    }
}
