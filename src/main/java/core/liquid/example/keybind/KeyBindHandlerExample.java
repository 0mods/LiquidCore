package core.liquid.example.keybind;

import core.liquid.LiquidCore;
import core.liquid.keybind.KeyBindBuilder;
import core.liquid.keybind.MacroHandler;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.InputEvent;

class KeyBindHandlerExample implements MacroHandler {
    public static final String CATEGORY = KeyBindBuilder.build("your_modid_there");

    public static final KeyMapping OPEN_EVENT;

    static {
        OPEN_EVENT = new KeyMapping(keyName("bbababoy"), 400, CATEGORY);
    }

    private KeyBindHandlerExample() {
        registryKey();
    }

    public static String keyName(String name) {
        return KeyBindBuilder.keyBuild(name, LiquidCore.ModId);
    }

    @Override
    public void keyInput(InputEvent.KeyInputEvent event) {

    }

    @Override
    public void registryKey() {

    }
}
