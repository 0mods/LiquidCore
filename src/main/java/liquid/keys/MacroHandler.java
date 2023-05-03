package liquid.keys;

import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.fml.ModLoadingContext;

interface MacroHandler {
    @Deprecated
    void keyInput(InputEvent.Key event);

    @Deprecated
    void registryKey();

    @Deprecated
    static Component category() {
        var modidGenerated = ModLoadingContext.get().getActiveContainer().getModId();
        return MacroHandler.category(modidGenerated);
    }

    @Deprecated
    static Component key(String name) {
        var modidGenerated = ModLoadingContext.get().getActiveContainer().getModId();
        return MacroHandler.key(name, modidGenerated);
    }

    @Deprecated
    static Component category(String modid) {
        return Component.keybind(String.format("key.categories.mod.%s", modid));
    }

    @Deprecated
    static Component key(String name, String modid) {
        return Component.keybind(String.format("key.%s.%s", modid, name));
    }

    static String build(String modid) {
        return String.format("key.categories.mod.%s", modid);
    }

    static String keyBuild(String name, String modid) {
        return String.format("key.%s.%s", modid, name);
    }
}
