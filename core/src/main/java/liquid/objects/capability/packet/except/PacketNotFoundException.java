package liquid.objects.capability.packet.except;

public class PacketNotFoundException extends IllegalStateException {
    public PacketNotFoundException() {
        super();
    }

    public PacketNotFoundException(String s) {
        super(s);
    }

    public PacketNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketNotFoundException(Throwable cause) {
        super(cause);
    }
}
