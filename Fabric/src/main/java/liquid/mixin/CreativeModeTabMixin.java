package liquid.mixin;

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
        CreativeModeTab[] method$0 = TABS;
        TABS = new CreativeModeTab[method$0.length + 1];

        for (int method$1 = 0; method$1 < method$0.length; method$1++)
            TABS[method$1] = method$0[method$1];
    }
}
