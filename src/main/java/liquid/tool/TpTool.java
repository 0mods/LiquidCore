package liquid.tool;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class TpTool {
    public static void tp(ServerPlayer player, ServerLevel level) {
        player.changeDimension(level);
    }
}
