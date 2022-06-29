package liquid.mixin.client;

import liquid.objects.item.material.ArmorMaterialRender;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ArmorItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(HumanoidArmorLayer.class)
public class HumanoidArmorLayerMixin {
    @Shadow @Final private static Map<String, ResourceLocation> ARMOR_LOCATION_CACHE;

    @Inject(method = "getArmorLocation", at = @At("HEAD"), cancellable = true)
    public void getArmorModelInject(ArmorItem armorItem, boolean bl, String string, CallbackInfoReturnable<ResourceLocation> cir) {
        if (armorItem.getMaterial() instanceof ArmorMaterialRender render) {
            cir.setReturnValue(ARMOR_LOCATION_CACHE.computeIfAbsent(render.setModID() + ":textures/armor/" + render.getName() + "/" + (bl ? 2 : 1) + (string == null ? "" : string) + ".png", ResourceLocation::new));
        }
    }
}
