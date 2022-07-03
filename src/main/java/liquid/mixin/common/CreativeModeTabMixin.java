package liquid.mixin.common;

import liquid.objects.data.tab.CreativeModeTabExtension;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CreativeModeTab.class)
public class CreativeModeTabMixin implements CreativeModeTabExtension {
    @Shadow @Final @Mutable
    public static CreativeModeTab[] TABS;

    @Override
    public void tabArray() {
        CreativeModeTab[] tabs = TABS;
        TABS = new CreativeModeTab[tabs.length + 1];

        for(int i = 0; i < tabs.length; i++) {
            TABS[i] = tabs[i];
        }
    }
}
