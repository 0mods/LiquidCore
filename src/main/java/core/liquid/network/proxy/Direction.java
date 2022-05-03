package core.liquid.network.proxy;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public interface Direction {
    void init();

    boolean clientSide();

    Level clientLevel();

    Player clientPlayer();
}
