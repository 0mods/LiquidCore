package liquid.dynamic.item;

import liquid.objects.data.container.DynamicContainerData;
import liquid.dynamic.container.DynamicContainer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ContainedItem extends Item {
    private final DynamicContainerData data;
    private static final Properties properties = new Properties();

    public ContainedItem(DynamicContainerData data) {
        super(properties);
        this.data = data;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        pPlayer.startUsingItem(pUsedHand);

        if (pLevel.isClientSide()) {
            pLevel.playSound(pPlayer, pPlayer.blockPosition(), data.getSoundEvent(), SoundSource.PLAYERS, 1, 1);
        }

        if (!pLevel.isClientSide()) {
            pPlayer.openMenu(new SimpleMenuProvider((id, inv, player) -> new DynamicContainer(id, inv, pUsedHand), this.getDescription()));
        }

        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    public DynamicContainerData getType() {
        return this.data;
    }
}