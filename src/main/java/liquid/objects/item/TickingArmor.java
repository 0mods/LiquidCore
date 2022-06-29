package liquid.objects.item;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public class TickingArmor extends ArmorItem {
    public TickingArmor(ArmorMaterial material, EquipmentSlot slot, Properties properties) {
        super(material, slot, properties);
    }

    public void onArmorTick(ItemStack stack, Player player) {}
    public native void onRemove(Player player);
}
