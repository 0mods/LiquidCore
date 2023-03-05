package liquid.objects.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;

public interface ILiquidCapability<T extends LiquidCapability<?>> {
    Capability<T> getCapability();
    CompoundTag toTag();
    void readTag(CompoundTag tag);
    void onEntityDeath(Entity entity, Entity oldEntity);
    ResourceLocation getRegistryLocation();
    void updateCapability();
    void updateCapability(Player player);
}
