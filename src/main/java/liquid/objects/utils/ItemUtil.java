package liquid.objects.utils;

import net.minecraft.world.item.ItemStack;

public class ItemUtil {
    public static boolean hasItem(final ItemStack a, final ItemStack b, final boolean calcTag) {
        if (a.isEmpty() || b.isEmpty()) {
            return false;
        }
        if (a.getItem() != b.getItem()) {
            return false;
        }
        return !calcTag || ItemStack.tagMatches(a, b);
    }

    public static boolean hasItem(ItemStack a, ItemStack b, boolean calcTag, boolean useTags) {
        if (a.isEmpty() && b.isEmpty()) {
            return true;
        }
        if (hasItem(a, b, calcTag)) {
            return true;
        }
        if (a.isEmpty() || b.isEmpty()) {
            return false;
        }
        if (useTags) {

        }
        return false;
    }
}
