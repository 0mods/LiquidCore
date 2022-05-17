package core.liquid.registry.base;

import core.liquid.util.Pair;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.ParallelDispatchEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LiquidRegistry {
    private final String modId;
    @Nullable
    private Class<?> owner;
    private final AtomicReference<RuntimeException> error = new AtomicReference<>();

    public LiquidRegistry(String modId) {
        this.modId = modId;
    }

    public void regToBus(IEventBus modEventBus) {
        modEventBus.addListener(this::handleLoadException);
    }

    private void handleLoadException(FMLLoadCompleteEvent event) {
        RuntimeException e = error.get();

        if (e != null) {
            throw e;
        }
    }

    public String getModId() {
        return modId;
    }

    public void setOwner(Class<?> owner) {
        this.owner = owner;
    }

    public @Nullable Class<?> getOwner() {
        return owner;
    }

    protected void enqueueWork(ParallelDispatchEvent event, Runnable action) {
        event.enqueueWork(catchable(event, action));
    }

    protected Runnable catchable(Event event, Runnable runnable) {
        return () -> catchErrors(event, runnable);
    }

    public void catchErrors(Event event, Runnable runnable) {
        catchErrors(event.getClass().getName(), runnable);
    }

    public void catchErrors(String action, Runnable runnable) {
        try {
            runnable.run();
        } catch (Throwable e) {
            String culpritInfo = getOwner() != null ? "Currently handling stuff from class: " + getOwner().getName() : "Unknown owner";
            storeException(new RuntimeException("Caught exception during " + action + ". " + culpritInfo, e));
        }
    }

    public void catchErrors(String action, Runnable runnable, Supplier<List<Pair<?, ?>>> extraInfo) {
        try {
            runnable.run();
        } catch (Throwable e) {
            String culpritInfo = getOwner() != null ? "Currently handling stuff from class: " + getOwner().getName() + "." : "Unknown owner.";
            String extra = "Extra Info:\n" + extraInfo.get().stream().map(pair -> pair.left().toString() + " -> " + pair.right().toString() + "\n").collect(Collectors.joining());
            RuntimeException exception = new RuntimeException("Caught exception during " + action + ". \n" + culpritInfo + "\n" + extra, e);
            storeException(exception);
        }
    }

    protected void storeException(RuntimeException exception) {
        error.compareAndSet(null, exception);
    }
}
