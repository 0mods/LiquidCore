package liquid.objects.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class TickingBlockEntity extends AbstractBlockEntity {
    public TickingBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public abstract void tick(Level pLevel, BlockPos pPos, BlockState pState, TickingBlockEntity pBlockEntity);
}
