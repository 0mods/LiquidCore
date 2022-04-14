package core.liquid.mixin;

import core.liquid.settings.CustomModProcessor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FMLModContainer.class, remap = false)
public abstract class FMLModContainerMixin {
    @Shadow @Final
    private Class<?> modClass;

    @Shadow(remap = false) @Final
    private ModFileScanData scanResults;

    @Inject(method = "constructMod", at = @At(value = "TAIL"), remap = false)
    public void fmlModConstructingHook(CallbackInfo ci) {
        FMLModContainer modContainer = (FMLModContainer) (Object) this;
        String modId = modContainer.getModId();
        if (modClass.isAnnotationPresent(Mod.class)) {
            CustomModProcessor.run(modId, scanResults);
        }
    }
}
