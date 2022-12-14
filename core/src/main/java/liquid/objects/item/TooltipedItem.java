package liquid.objects.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TooltipedItem extends Item {
    public List<Component> tooltip;

    public TooltipedItem(Properties p_41383_, List<Component>... tooltips) {
        super(p_41383_);

        for (List<Component> tooltip : tooltips) {
            this.tooltip = tooltip;
        }
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.addAll(this.tooltip);
    }
}
