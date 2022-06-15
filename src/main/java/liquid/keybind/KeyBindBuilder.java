package liquid.keybind;

import net.minecraft.network.chat.Component;

@Deprecated(forRemoval = true)
public abstract class KeyBindBuilder implements MacroHandler {
    public static Component build(String modid) {
        return Component.keybind("key.categories.mod." + modid);
    }
    public static Component keyBuild(String name, String modid) {
        return Component.keybind("key." + modid + "." + name);
    }

    @Deprecated
    public static String build0(String modid) {
        return String.format("key.categories.mod.%s", modid);
    }

    @Deprecated
    public static String keyBuild0(String name, String modid) {
        return String.format("key.%s.%s", modid, name);
    }
}
