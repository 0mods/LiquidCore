package liquid.objects.capability.packet;

import liquid.objects.capability.serializer.LiquidTagSerializer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface Packet<T> {
    @OnlyIn(Dist.CLIENT)
    void process();
    void process(ServerPlayer serverPlayer);
    LiquidTagSerializer<T> serializer();
    void onReceived(Player player, T arg);
    void sendToServer(T value);
    void sendToClient(ServerPlayer playerEntity, T value);
}
