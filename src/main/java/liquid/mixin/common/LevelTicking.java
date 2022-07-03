package liquid.mixin.common;

import liquid.objects.tick.TickEvent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class LevelTicking {
    @Shadow @Final
    private ServerLevelData serverLevelData;
    @Shadow @Final
    private MinecraftServer server;

    @Inject(method = "tickTime", at = @At("HEAD"))
    public void tickInject(CallbackInfo ci) {
        TickEvent event = TickEvent.get();
        serverLevelData.getScheduledEvents().tick(this.server, event.elapsingTick());
    }
}
