package liquid.objects.tick;

import liquid.LiquidCore;

public class TickEvent {
    private final int ticks;

    protected TickEvent(int ticks) {
        this.ticks = ticks;
    }

    public boolean elapsedFinal() {
        return this.ticks == 0;
    }

    public int elapsingTick() {
        LiquidCore.LOGGER.debug("Ticking. Full value: " + this.ticks);
        return this.ticks - 1;
    }

    public static TickEvent event(int ticks) {
        return new TickEvent(ticks);
    }

    public static TickEvent getTicking() {
        int tick = 0;
        tick = event(tick).ticks;

        return event(tick);
    }
}
