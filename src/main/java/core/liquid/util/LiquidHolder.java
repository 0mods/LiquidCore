package core.liquid.util;

import java.util.Objects;

public class LiquidHolder<T> {
    private T value;

    public LiquidHolder(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "null";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof LiquidHolder<?> wrapper))
            return false;

        return Objects.equals(value, wrapper.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
