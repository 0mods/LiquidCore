package core.liquid.registry.base.tasks;

import core.liquid.registry.base.LiquidRegistry;
import core.liquid.util.PollHandler;

import java.util.ArrayList;
import java.util.List;

public class StartRegister extends LiquidRegistry {
    private final PollHandler<List<Runnable>> pollHandler = PollHandler.of(new ArrayList<>());

    public StartRegister(String modId) {
        super(modId);
    }

    protected void add(Runnable runnable) {
        pollHandler.get().add(runnable);
    }

    protected void startFull() {
        pollHandler.doOrRemove(pollHandlers -> {
            for (Runnable task : pollHandlers) {
                task.run();
            }
        });
    }
}
