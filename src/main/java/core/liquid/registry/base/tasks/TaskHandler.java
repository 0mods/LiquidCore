package core.liquid.registry.base.tasks;

import core.liquid.util.PollHandler;
import net.minecraftforge.eventbus.api.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class TaskHandler<T> extends PollHandler<List<T>> {
    private TaskHandler(List<T> value, String errorMessage) {
        super(value, errorMessage);
    }

    public void add(T element) {
        this.get().add(element);
    }

    public void eachOrDelete(Consumer<T> action) {
        this.doOrRemove(ts -> ts.forEach(action));
    }

    public static <T> TaskHandler<T> of(Class<? extends Event> event) {
        return new TaskHandler<>(new ArrayList<>(),"You attempted to access tasks for event '" + event.getName() + "' while it has been already fired.");
    }
}
