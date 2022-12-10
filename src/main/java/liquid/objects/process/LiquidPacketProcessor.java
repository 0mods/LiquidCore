package liquid.objects.process;

import liquid.objects.capability.packet.Packet;
import net.minecraftforge.network.NetworkDirection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

public class LiquidPacketProcessor {
    public static final Map<String, PacketPackage> data = new HashMap<>();

    public static void process(Field field, String path, NetworkDirection direction) {
        if (Modifier.isStatic(field.getModifiers())) {
            try {
                Object some = field.get(null);
                if (some instanceof Packet<?> packet)
                    data.put(path, new PacketPackage(packet, direction));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static PacketPackage getInstance(String name) {
        return data.get(name);
    }

    public static String getName(Packet<?> i) {
        for (var packet : data.entrySet()) {
            if (packet.getValue().instance()==i) {
                return packet.getKey();
            }
        }
        return "";
    }

    public static PacketPackage getPackage(Packet<?> i) {
        for(var packet : data.entrySet()) {
            if (packet.getValue().instance() == i) {
                return packet.getValue();
            }
        }
        return null;
    }

    public record PacketPackage(Packet<?> instance, NetworkDirection direction) {}
}
