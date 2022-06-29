package liquid.mixin.common;

import liquid.dynamic.item.DynamicItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ItemStack stack) {
            var thisStack = (ItemStack) (Object) this;
            var thisItem = thisStack.getItem();
            var thatItem = stack.getItem();

            if (thisItem instanceof DynamicItem && thatItem instanceof DynamicItem)
                return true;
        }

        return super.equals(obj);
    }
}
