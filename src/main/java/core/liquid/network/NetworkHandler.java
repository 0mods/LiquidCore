package core.liquid.network;

import core.liquid.LiquidCore;
import core.liquid.network.messages.LiquidPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;

public class NetworkHandler {
    public static final String MESSAGE_PROTOCOL_VERSION = "1.0";
    public static final ResourceLocation HOLLOW_CORE_CHANNEL = new ResourceLocation(LiquidCore.ModId, "channel");
    public static SimpleChannel HollowCoreChannel;
    private static int i = 0;

    public static <MSG> void sendMessageToClient(MSG messageToClient, Player player) {
        HollowCoreChannel.sendTo(messageToClient, ((ServerPlayer) player).connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }

    public static <MSG> void sendMessageToServer(MSG messageToServer) {
        HollowCoreChannel.sendToServer(messageToServer);
    }

    public static void register() {
        if(HollowCoreChannel==null) {
            HollowCoreChannel = NetworkRegistry.newSimpleChannel(HOLLOW_CORE_CHANNEL, () -> MESSAGE_PROTOCOL_VERSION,
                    MESSAGE_PROTOCOL_VERSION::equals,
                    MESSAGE_PROTOCOL_VERSION::equals
            );
        }
    }

    public static void registerMessage(Class<LiquidPacket> packetClass) {
        if(HollowCoreChannel==null) {
            HollowCoreChannel = NetworkRegistry.newSimpleChannel(HOLLOW_CORE_CHANNEL, () -> MESSAGE_PROTOCOL_VERSION,
                    MESSAGE_PROTOCOL_VERSION::equals,
                    MESSAGE_PROTOCOL_VERSION::equals
            );
        }
        try {
            LiquidPacket packet = packetClass.getConstructor().newInstance();

            HollowCoreChannel.registerMessage(
                    i++,
                    packetClass,
                    packet::encode,
                    packet::decode,
                    packet::onReceived
            );
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
