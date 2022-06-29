package liquid.mixin.common;

import liquid.objects.item.TickingArmor;
import liquid.objects.utils.ItemUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class ArmorTickingMixin extends LivingEntity {
    @Shadow
    public abstract Iterable<ItemStack> getArmorSlots();
    private final NonNullList<ItemStack> armorCache = NonNullList.withSize(4, ItemStack.EMPTY);

    protected ArmorTickingMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    public void tickInject(CallbackInfo info) {
        int i = 0;

        for (ItemStack stack : getArmorSlots()) {
            ItemStack cachedStack = armorCache.get(i);
            if (!ItemUtil.hasItem(cachedStack, stack, false, false)) {
                    if (cachedStack.getItem() instanceof TickingArmor armor)
                        armor.onRemove((Player) (Object) this);

                    armorCache.set(i, stack.copy());
                }
            i++;

            if (cachedStack.getItem() instanceof TickingArmor armor)
                armor.onArmorTick(stack, (Player) (Object) this);
        }
    }
}

