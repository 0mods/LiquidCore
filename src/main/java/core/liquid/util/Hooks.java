package core.liquid.util;

public class Hooks {
    public static <T> T create() {
        return null;
    }

    public static <R, T extends R> R safe(T object) {
        return object;
    }

}
