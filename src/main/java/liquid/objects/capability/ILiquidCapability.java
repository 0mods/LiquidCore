package liquid.objects.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface ILiquidCapability<T extends LiquidCapability<?>> {
    ForgeCapability<T> getCapability();
    CompoundTag toTag();
    void readTag(CompoundTag tag);
    void onEntityDeath(Entity player, Entity oldPlayer);
    ResourceLocation getRegistryLocation();
    void updateCapability();
    void updateCapability(Player player);
}
