package liquid.objects.tick;

public class TickEvent {
    private final int ticks;

    protected TickEvent(int ticks) {
        this.ticks = ticks;
    }

    public boolean elapsed() {
        return this.ticks == 0;
    }

    public int elapsingTick() {
        return this.ticks - 1;
    }

    public static native TickEvent get();

    public static TickEvent event(int ticks) {
        return new TickEvent(ticks);
    }
}
