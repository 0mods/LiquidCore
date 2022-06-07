package liquid.objects.block.entity;

import liquid.helper.StackHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractBlockEntity extends BlockEntity {
    private final LazyOptional<IItemHandler> stackHandlerCap = LazyOptional.of(this::getStackHandler);
    private final StackHelper handler;

    public AbstractBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
        this.handler = this.load(this::runSystem);
    }

    public abstract @NotNull ItemStackHandler getStackHandler();
    public abstract StackHelper load(Runnable runnable);

    public void drops() {
        SimpleContainer container = new SimpleContainer(handler.getSlots());

        for (int i = 0; i < handler.getSlots(); i++)
            container.setItem(i, handler.getStackInSlot(i));

        Containers.dropContents(this.level, this.worldPosition, container);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithFullMetadata);
    }

    @Override
    @NotNull
    public CompoundTag getUpdateTag() {
        return this.saveWithFullMetadata();
    }

    @Override
    public void load(@NotNull CompoundTag p_155245_) {
        super.load(p_155245_);
        this.getStackHandler().deserializeNBT(p_155245_);
    }

    @Override
    protected void saveAdditional(CompoundTag p_187471_) {
        p_187471_.merge(this.getStackHandler().serializeNBT());
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
        if (!this.isRemoved() && cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.orEmpty(cap, this.stackHandlerCap);
        }

        return super.getCapability(cap, side);
    }

    public void runSystem() {
        super.setChanged();
        BlockEntityHelper.playerDispatch(this);
    }
}
