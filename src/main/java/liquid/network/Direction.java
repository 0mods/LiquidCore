package liquid.network;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.DistExecutor.*;

public interface Direction {
    void init();

    boolean clientSide();

    Level clientLevel();

    Player clientPlayer();

    static <T extends Direction> Direction of(SafeSupplier<T> client, SafeSupplier<T> server) {
        return DistExecutor.safeRunForDist(()-> client, ()-> server);
    }

    static <T extends Direction> Direction clientOnly(SafeSupplier<T> client) {
        return Direction.of(client, null);
    }

    static <T extends Direction> Direction serverOnly(SafeSupplier<T> server) {
        return Direction.of(null, server);
    }
}
