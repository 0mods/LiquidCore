package core.liquid.mixin;

import core.liquid.objects.interfaces.LiquidMod;
import core.liquid.settings.CustomModProcessor;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FMLModContainer.class, remap = false)
public abstract class ModProcessorMixin {
    @Shadow @Final
    private ModFileScanData scanResults;
    @Shadow
    public abstract Object getMod();

    @Inject(method = "constructMod", at = @At(value = "TAIL"))
    public void fmlModConstructingHook(CallbackInfo ci) {
        FMLModContainer modContainer = (FMLModContainer) (Object) this;
        Object mod = this.getMod();
        String modId = modContainer.getModId();
        if (mod instanceof LiquidMod) {
            CustomModProcessor.run(scanResults, modId);
        }
    }
}
