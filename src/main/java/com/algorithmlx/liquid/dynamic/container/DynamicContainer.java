package com.algorithmlx.liquid.dynamic.container;

import com.algorithmlx.liquid.LiquidCore;
import com.algorithmlx.liquid.base.container.BasedContainer;
import com.algorithmlx.liquid.base.data.DynamicContainerData;
import com.algorithmlx.liquid.base.data.another.Dim;
import com.algorithmlx.liquid.base.data.another.Point;
import com.algorithmlx.liquid.base.data.tag.ContainerTag;
import com.algorithmlx.liquid.dynamic.item.DynamicItem;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DynamicContainer extends BasedContainer {
    public static final int BACKPACK_INVENTORY = 1;
    private final Player player;
    private final InteractionHand hand;
    private ItemStack itemStack;
    int padding = 8;
    int titleSpace = 10;

    public DynamicContainer(int windowId, Inventory playerInventory, InteractionHand hand) {
        super(LiquidCore.CONTAINER_TYPE, windowId, playerInventory);
        this.player = playerInventory.player;
        this.hand = hand;
        ItemStack stack = player.getItemInHand(hand);

        if (stack.getItem() instanceof DynamicItem) {
            this.loadContainer(playerInventory, stack);
        }
    }

    private void loadContainer(Inventory inventory, ItemStack itemStack) {
        Dim dimension = this.getDimension();
        DynamicContainerData type = this.getDynamicItem().getType();
        int rowWidth = type.getRowWidth();
        int rows = type.getRowHeight();

        ListTag tags = itemStack.getOrCreateTag().getList("Inventory", 10);
        SimpleContainer simpleContainer = new SimpleContainer(rowWidth * rows) {
            @Override
            public void setChanged() {
                itemStack.getOrCreateTag().put("Inventory", ContainerTag.toNBT(this));
                super.setChanged();
            }
        };

        ContainerTag.fromNBT(tags, simpleContainer);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < rowWidth; x++) {
                Point backpackSlotPosition = getBackpackSlotPosition(dimension, x, y);
                addSlot(new LockableSlot(simpleContainer, y * rowWidth + x, backpackSlotPosition.x + 1, backpackSlotPosition.y + 1));
            }
        }

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, x, y);
                this.addSlot(new LockableSlot(inventory, x + y * 9 + 9, playerInvSlotPosition.x + 1, playerInvSlotPosition.y + 1));
            }
        }

        for (int x = 0; x < 9; ++x) {
            Point playerInvSlotPosition = getPlayerInvSlotPosition(dimension, x, 3);
            this.addSlot(new LockableSlot(inventory, x, playerInvSlotPosition.x + 1, playerInvSlotPosition.y + 1));
        }
    }

    public DynamicItem getDynamicItem() {
        return (DynamicItem) player.getItemInHand(hand).getItem();
    }

    public Dim getDimension() {
        DynamicContainerData type = getDynamicItem().getType();
        return new Dim(padding * 2 + Math.max(type.getRowWidth(), 9) * 18, padding * 2 + titleSpace * 2 + 8 + (type.getRowHeight() + 4) * 18);
    }

    public Point getBackpackSlotPosition(Dim dimension, int x, int y) {
        DynamicContainerData type = getDynamicItem().getType();
        return new Point(dimension.width / 2 - type.getRowWidth() * 9 + x * 18, padding + titleSpace + y * 18);
    }

    public Point getPlayerInvSlotPosition(Dim dimension, int x, int y) {
        return new Point(dimension.width / 2 - 9 * 9 + x * 18, dimension.height - padding - 4 * 18 - 3 + y * 18 + (y == 3 ? 4 : 0));
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        ItemStack stack = pPlayer.getItemInHand(hand);
        return stack.getItem() instanceof DynamicItem;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack toInsert = slot.getItem();
            itemStack = toInsert.copy();
            DynamicContainerData type = getDynamicItem().getType();
            if (index < type.getRowHeight() * type.getRowWidth()) {
                if (!this.moveItemStackTo(toInsert, type.getRowHeight() * type.getRowWidth(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(toInsert, 0, type.getRowHeight() * type.getRowWidth(), false)) {
                return ItemStack.EMPTY;
            }

            if (toInsert.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
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
