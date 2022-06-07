package liquid.helper;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class StackHelper extends ItemStackHandler {
    private final Runnable runContext;
    private final Map<Integer, Integer> integerMap;
    private BiFunction<Integer, ItemStack, Boolean> isValid = null;
    private int dynamicStackable = 64;
    private int[] slotContext = null;


    public StackHelper(int size) {
        this(size, null);
    }

    public StackHelper(int size, Runnable runnable) {
        super(size);

        this.runContext = runnable;
        this.integerMap = new HashMap<>();
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (this.slotContext != null && ArrayUtils.contains(this.slotContext, slot)) {
            return stack;
        }

        return super.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.slotContext != null && !ArrayUtils.contains(this.slotContext, slot)) {
            return ItemStack.EMPTY;
        }

        return super.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.integerMap.containsKey(slot) ? this.integerMap.get(slot) : this.dynamicStackable;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.isValid == null || this.isValid.apply(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (this.runContext != null) {
            this.runContext.run();
        }
    }

    public ItemStack insertStack(int slot, ItemStack stack, boolean simulate) {
        return super.insertItem(slot, stack, simulate);
    }

    public ItemStack extractStack(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    public NonNullList<ItemStack> getStacks() {
        return this.stacks;
    }

    public int[] getOutputs() {
        return this.slotContext;
    }

    public void setDefault(int size) {
        this.dynamicStackable = size;
    }

    public void addLimit(int slot, int size) {
        this.integerMap.put(slot, size);
    }

    public void setValid(BiFunction<Integer, ItemStack, Boolean> valid) {
        this.isValid = valid;
    }

    public void setOutputs(int... slots) {
        this.slotContext = slots;
    }

    public Container forContainer() {
        return new SimpleContainer(this.stacks.toArray(new ItemStack[0]));
    }


    public StackHelper copy() {
        StackHelper stackHandler = new StackHelper(this.getSlots(), this.runContext);

        stackHandler.setDefault(this.dynamicStackable);
        stackHandler.setValid(this.isValid);
        stackHandler.setOutputs(this.slotContext);

        this.integerMap.forEach(stackHandler::addLimit);

        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i);

            stackHandler.setStackInSlot(i, stack.copy());
        }

        return stackHandler;
    }

    public static StackHelper of(int size) {
        return of(size, b -> {});
    }

    public static StackHelper of(int size, Consumer<StackHelper> c) {
        return of(size, null, c);
    }

    public static StackHelper of(int size, Runnable runnable, Consumer<StackHelper> c) {
        StackHelper h = new StackHelper(size, runnable);
        c.accept(h);

        return h;
    }
}
