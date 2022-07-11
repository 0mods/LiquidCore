package liquid.dynamic.container;

import liquid.dynamic.item.DynamicItem;
import liquid.objects.data.tag.ContainerTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import liquid.objects.data.scale.ScaleArray.*;

public class DynamicContainer extends AbstractContainerMenu {
    private final Player player;
    private final InteractionHand hand;
    int padding = 8;
    int titleSpace = 10;

    public DynamicContainer(int windowId, Inventory playerInventory, InteractionHand hand) {
        super(null, windowId);

        this.player = playerInventory.player;
        this.hand = hand;
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof DynamicItem) {
            this.start(playerInventory, stack);
        }
    }

    private void start(Inventory inventory, ItemStack itemStack) {
        var size = this.getSize();
        var type = this.getDynamicItem().getData();
        var rowWidth = type.getRowWidth();
        var rows = type.getRowHeight();

        var tags = itemStack.getOrCreateTag().getList("Inventory", 10);
        var simpleContainer = new SimpleContainer(rowWidth * rows) {
            @Override
            public void setChanged() {
                itemStack.getOrCreateTag().put("Inventory", ContainerTag.toTag(this));
                super.setChanged();
            }
        };

        ContainerTag.fromTag(tags, simpleContainer);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < rowWidth; x++) {
                var backpackSlotPosition = getBackpackSlots(size, x, y);
                addSlot(new LockableSlot(simpleContainer, y * rowWidth + x, backpackSlotPosition.x + 1, backpackSlotPosition.y + 1));
            }
        }

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                var playerInvSlotPosition = getPlayerSlots(size, x, y);
                this.addSlot(new LockableSlot(inventory, x + y * 9 + 9, playerInvSlotPosition.x + 1, playerInvSlotPosition.y + 1));
            }
        }

        for (int x = 0; x < 9; ++x) {
            var playerInvSlotPosition = getPlayerSlots(size, x, 3);
            this.addSlot(new LockableSlot(inventory, x, playerInvSlotPosition.x + 1, playerInvSlotPosition.y + 1));
        }
    }

    public DynamicItem getDynamicItem() {
        return (DynamicItem) player.getItemInHand(hand).getItem();
    }

    public Size getSize() {
        var type = getDynamicItem().getData();
        return new Size(padding * 2 + Math.max(type.getRowWidth(), 9) * 18, padding * 2 + titleSpace * 2 + 8 + (type.getRowHeight() + 4) * 18);
    }

    public Point getBackpackSlots(Size size, int x, int y) {
        var type = getDynamicItem().getData();
        return new Point(size.width / 2 - type.getRowWidth() * 9 + x * 18, padding + titleSpace + y * 18);
    }

    public Point getPlayerSlots(Size size, int x, int y) {
        return new Point(size.width / 2 - 9 * 9 + x * 18, size.height - padding - 4 * 18 - 3 + y * 18 + (y == 3 ? 4 : 0));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        var stack = pPlayer.getItemInHand(hand);
        return stack.getItem() instanceof DynamicItem;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        var itemStack = ItemStack.EMPTY;
        var slot = this.slots.get(index);
        if (slot.hasItem()) {
            var stack = slot.getItem();
            itemStack = stack.copy();
            var type = getDynamicItem().getData();

            if (index < type.getRowHeight() * type.getRowWidth())
                if (!this.moveItemStackTo(stack, type.getRowHeight() * type.getRowWidth(), this.slots.size(), true))
                    return ItemStack.EMPTY;
                else if (!this.moveItemStackTo(stack, 0, type.getRowHeight() * type.getRowWidth(), false))
                    return ItemStack.EMPTY;

            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return itemStack;
    }

    public class LockableSlot extends Slot {
        public LockableSlot(Container container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPickup(Player player) {
            return !(getItem().getItem() instanceof DynamicItem) && getItem() != player.getItemInHand(hand);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return !(stack.getItem() instanceof DynamicItem) && stack != player.getItemInHand(hand);
        }
    }
}
