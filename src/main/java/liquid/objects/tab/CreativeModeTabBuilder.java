package liquid.objects.tab;

import liquid.objects.data.tab.CreativeModeTabExtension;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author FabricAPI developers
 */
public final class CreativeModeTabBuilder {
    private final ResourceLocation rl;
    private Supplier<ItemStack> iconSup = ()-> ItemStack.EMPTY;
    private BiConsumer<List<ItemStack>, CreativeModeTab> stacksForDisplay;

    private CreativeModeTabBuilder(ResourceLocation rl) {
        this.rl = rl;
    }

    public static CreativeModeTabBuilder create(ResourceLocation rl) {
        return new CreativeModeTabBuilder(rl);
    }

    public CreativeModeTabBuilder icon(Supplier<ItemStack> icon) {
        this.iconSup = icon;
        return this;
    }

    public CreativeModeTabBuilder iconList(Consumer<List<ItemStack>> stacksForDisplay) {
        return iconList((itemStacks, itemGroup)-> stacksForDisplay.accept(itemStacks));
    }

    public CreativeModeTabBuilder iconList(BiConsumer<List<ItemStack>, CreativeModeTab> stacksForDisplay) {
        this.stacksForDisplay = stacksForDisplay;
        return this;
    }

    public static CreativeModeTab build(ResourceLocation rl, Supplier<ItemStack> iconSup) {
        return new CreativeModeTabBuilder(rl).icon(iconSup).build();
    }

    public CreativeModeTab build() {
        ((CreativeModeTabExtension) CreativeModeTab.TAB_BUILDING_BLOCKS).tabArray();
        return new CreativeModeTab(CreativeModeTab.TABS.length - 1, String.format("%s.%s", rl.getNamespace(), rl.getPath())) {
            @Override
            public ItemStack makeIcon() {
                return iconSup.get();
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> stacks) {
                if (stacksForDisplay != null) {
                    stacksForDisplay.accept(stacks, this);
                    return;
                }

                super.fillItemList(stacks);
            }
        };
    }
}
