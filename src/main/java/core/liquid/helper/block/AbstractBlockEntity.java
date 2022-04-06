package core.liquid.helper.block;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractBlockEntity extends BlockEntity {
    public AbstractBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    public abstract void readTag(CompoundTag compoundTag, boolean descPacket);
    public abstract void saveTag(CompoundTag compoundTag, boolean descPacket);

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        this.readTag(pTag, false);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        this.saveTag(pTag, false);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, blockEntity -> {
            CompoundTag tag = new CompoundTag();
            this.saveTag(tag, true);
            return tag;
        });
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag tag = pkt.getTag() != null ? pkt.getTag() : new CompoundTag();
        this.readTag(tag, true);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.readTag(tag, true);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveTag(tag, true);
        return tag;
    }
}
