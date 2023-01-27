package liquid.objects.capability.packet;

import liquid.objects.capability.packet.except.PacketNotFoundException;
import liquid.objects.capability.packet.except.PacketNotRegisteredException;
import liquid.objects.capability.serializer.LiquidTagSerializer;
import liquid.objects.capability.tag.TagUtils;
import liquid.objects.utils.JVMUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class LiquidPacketManager {
    public static final Map<String, LiquidPacket<?>> PACKETS = new HashMap<>();

    public static void registrate(String name, LiquidPacket<?> packet) {
        try {
            PACKETS.put(name, packet);
        } catch (Exception e) {
            throw new PacketNotRegisteredException(String.format("Fatal loading %s packet. Check your code or send this error to developer", name));
        }
    }

    public static LiquidPacket<?> getFromTag(CompoundTag tag, String serializerName, String name) {
        LiquidPacket<?> packet = PACKETS.get(name);
        LiquidTagSerializer<?> tagSerializer = TagUtils.SERIALIZERS.get(new ResourceLocation(serializerName));
        if (serializerName.equals("liquid:notag")) {
            return packet;
        }
        else {
            try {
                packet.value = JVMUtils.castDarkMagic(tagSerializer.readTag(tag));
                return packet;
            } catch (Exception e) {
                throw new PacketNotFoundException(String.format("Packet %s is not found. Check your registration", name));
            }
        }
    }
}
