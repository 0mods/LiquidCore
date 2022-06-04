package liquid.keybind;

public class KeyBindBuilder {
    public static String build(String modid) {
        return String.format("key.categories.mod.%s", modid);
    }
    public static String keyBuild(String name, String modid) {
        return String.format("key.%s.%s", modid, name);
    }
}
