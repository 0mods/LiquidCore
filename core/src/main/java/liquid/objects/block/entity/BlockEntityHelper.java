package liquid.objects.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

final class BlockEntityHelper {
    public static void playerDispatch(BlockEntity blockEntity) {
        Level level = blockEntity.getLevel();
        if (level == null)
            return;

        Packet<ClientGamePacketListener> packet = blockEntity.getUpdatePacket();
        if (packet == null)
            return;

        var players = level.players();
        BlockPos pos = blockEntity.getBlockPos();

        for (Player player : players) {
            if (player instanceof ServerPlayer serverPlayer) {
                if (getPlayerMath(serverPlayer.getX(), serverPlayer.getZ(), pos.getX() + 0.5, pos.getZ() + 0.5)) {
                    serverPlayer.connection.send(packet);
                }
            }
        }
    }

    private static boolean getPlayerMath(double x1, double z1, double x2, double z2) {
        return Math.hypot(x1 - x2, z1 - z2) < 64;
    }
}
