package example.proxy;

import core.liquid.network.proxy.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

class ServerProxyExample implements Direction {
    @Override
    public void init() {

    }

    @Override
    public boolean clientSide() {
        return false;
    }

    @Override
    public Level clientLevel() {
        throw new IllegalStateException("your error message here");
    }

    @Override
    public Player clientPlayer() {
        throw new IllegalStateException("your error message here");
    }
}
