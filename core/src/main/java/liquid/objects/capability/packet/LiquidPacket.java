package liquid.objects.capability.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class LiquidPacket<T> implements Packet<T> {
    public T value;

    @OnlyIn(Dist.CLIENT)
    @Override
    public void process() {
        this.onReceived(Minecraft.getInstance().player, this.value);
    }

    @Override
    public void process(ServerPlayer serverPlayer) {
        this.onReceived(serverPlayer, this.value);
    }

    @Override
    public void sendToServer(T value) {
        this.value = value;
    }

    @Override
    public void sendToClient(ServerPlayer playerEntity, T value) {

    }
}
