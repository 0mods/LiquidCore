package liquid.keys;

import com.mojang.blaze3d.platform.InputConstants;
import liquid.LiquidCore;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = LiquidCore.ModId)
public class MacroRegister {
    static List<KeyMapping> listOfKeyMapping = new ArrayList<>();

    public static KeyMapping addKeyMapping(String keyName, InputConstants.Type constantsType, int keyNum) {
        String modId = ModLoadingContext.get().getActiveContainer().getModId();

        KeyMapping keyMapping =
                new KeyMapping(MacroHandler.keyBuild(keyName, modId), constantsType, keyNum, MacroHandler.build(modId));

        listOfKeyMapping.add(keyMapping);
        return keyMapping;
    }

    public static KeyMapping addKeyMapping(String keyName, KeyConflictContext conflictContext,
                                           KeyModifier modifier,
                                           InputConstants.Type constantsType,
                                           int keyNum
    ) {
        String modId = ModLoadingContext.get().getActiveContainer().getModId();

        KeyMapping keyMapping =
                new KeyMapping(MacroHandler.keyBuild(keyName, modId), conflictContext, modifier, constantsType, keyNum,
                        MacroHandler.build(modId));

        listOfKeyMapping.add(keyMapping);
        return keyMapping;
    }

    @SubscribeEvent
    public void registerKeyMapping(RegisterKeyMappingsEvent e) {
        if (!listOfKeyMapping.isEmpty())
            for (KeyMapping mapping : listOfKeyMapping)
                e.register(mapping);
        else LiquidCore.log.atDebug().log("Keys to registry is not found! Skipping...");
    }
}
