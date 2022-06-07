package liquid.objects.block;

import liquid.objects.block.entity.TickingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
abstract class BlockBaseEntity extends Block implements EntityBlock {
    protected BlockBaseEntity(Properties p_49224_) {
        super(p_49224_);
    }

    @Override
    public void onRemove(BlockState state, Level p_60516_, BlockPos p_60517_, BlockState newState, boolean p_60519_) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity entity = p_60516_.getBlockEntity(p_60517_);
            if (entity instanceof TickingBlockEntity e) {
                e = (TickingBlockEntity) newBlockEntity(p_60517_, state);
                e.drops();
            }
        }
        super.onRemove(state, p_60516_, p_60517_, newState, p_60519_);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_153212_, BlockState p_153213_, BlockEntityType<T> p_153214_) {
        return (level, pos, state, entity) -> {
            if (entity instanceof TickingBlockEntity entity1) {
                entity1 = (TickingBlockEntity) newBlockEntity(pos, state);
                entity1.tick(level, pos, state, entity1);
            }
        };
    }
}
