package core.liquid.objects.enums;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

public enum LiquidDirectory {
    CONFIG_DIR("liquidCore");

    private final Path relativePath;
    private final boolean isDirectory;
    private Path absolutePath;

    LiquidDirectory(String... path) {
        relativePath = computePath(path);
        this.isDirectory = true;
    }

    private Path computePath(String... path) {
        return Paths.get(path[0], Arrays.copyOfRange(path, 1, path.length));
    }

    public Path get() {
        return absolutePath;
    }
}
