package liquid.objects.capability.packet.except;

public class PacketNotRegisteredException extends PacketNotFoundException {
    public PacketNotRegisteredException() {
        super();
    }

    public PacketNotRegisteredException(String s) {
        super(s);
    }

    public PacketNotRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public PacketNotRegisteredException(Throwable cause) {
        super(cause);
    }
}
