package core.liquid.network.messages;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class LiquidPacket<V> {
    private V val;

    public void setVal(V val) {
        this.val = val;
    }

    public V getVal() {
        return val;
    }

    public abstract LiquidPacket<V> decode(FriendlyByteBuf buf);

    public abstract void encode(LiquidPacket<V> hollowPacket, FriendlyByteBuf buf);

    public abstract void onReceived(LiquidPacket<V> message, Supplier<NetworkEvent.Context> ctxSupplier);
}
