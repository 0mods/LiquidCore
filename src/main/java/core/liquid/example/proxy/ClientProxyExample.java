package core.liquid.example.proxy;

import core.liquid.network.proxy.Direction;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

@SuppressWarnings("ConstantConditions")
class ClientProxyExample implements Direction {
    @Override
    public void init() {}

    @Override
    public boolean clientSide() {
        return true;
    }

    @Override
    public Level clientLevel() {
        return Minecraft.getInstance().player.getCommandSenderWorld();
    }

    @Override
    public Player clientPlayer() {
        return Minecraft.getInstance().player;
    }
}
