package core.liquid.objects.config;

public class ConfigVariable<T> {
    private T value;
    private final String name;

    public ConfigVariable(T value, String name) {
        this.value = value;
        this.name = name;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
        if (value instanceof Boolean) {
            MainConfig.setBool(name, (Boolean) value);
        } else if (value instanceof Integer) {
            MainConfig.setInt(name, (Integer) value);
        } else if (value instanceof Float) {
            MainConfig.setFloat(name, (Float) value);
        }
    }

    public String getName() {
        return name;
    }
}
