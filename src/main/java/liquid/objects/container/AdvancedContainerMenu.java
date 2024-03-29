package liquid.objects.container;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class AdvancedContainerMenu extends AbstractContainerMenu {
    private final IItemHandler playerInventory;

    public AdvancedContainerMenu(MenuType<?> container, int windowId, Inventory inv) {
        super(container, windowId);
        this.playerInventory = new InvWrapper(inv);
    }

    public int addSlotRange(IItemHandler handler, int index, int x, int y, int amount, int dx) {
        for (int i = 0 ; i < amount ; i++) {
            addSlot(new SlotItemHandler(handler, index, x, y));
            x += dx;
            index++;
        }
        return index;
    }

    public void addSlotBox(IItemHandler handler, int index, int x, int y, int horAmount, int dx, int verAmount, int dy) {
        for (int j = 0 ; j < verAmount ; j++) {
            index = addSlotRange(handler, index, x, y, horAmount, dx);
            y += dy;
        }
    }

    public void makeInventorySlots(int leftColX, int topRowY) {
        addSlotBox(playerInventory, 9, leftColX, topRowY, 9, 18, 3, 18);

        topRowY += 58;
        addSlotRange(playerInventory, 0, leftColX, topRowY, 9, 18);
    }
}
