package core.liquid.objects.tab;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

public class TabMethod extends CreativeModeTab {
    private final ItemStack icon;

    public TabMethod(String label, ItemStack icon) {
        super(label);
        this.icon = icon;
    }

    public TabMethod(String label, ItemStack icon, ResourceLocation bgTexture) {
        this(label, icon);
        this.setBackgroundImage(bgTexture);
    }

    public static TabMethod of(String name, ItemStack icon) {
        return new TabMethod(name, icon);
    }

    public static TabMethod of(String name, ItemStack icon, ResourceLocation bgTexture) {
        return new TabMethod(name, icon, bgTexture);
    }

    public static TabMethod of(String name, ItemLike icon) {
        return new TabMethod(name, new ItemStack(icon));
    }
    public static TabMethod of(String name, ItemLike icon, ResourceLocation bgTexture) {
        return new TabMethod(name, new ItemStack(icon), bgTexture);
    }

    @Override
    public ItemStack makeIcon() {
        return icon;
    }
}
