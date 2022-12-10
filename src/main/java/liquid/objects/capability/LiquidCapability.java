package liquid.objects.capability;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class LiquidCapability<T extends LiquidCapability<?>> implements ILiquidCapability<LiquidCapability<?>> {
    public static final Map<ResourceLocation, ForgeCapability<LiquidCapability<?>>> CAPABILITIES = new HashMap<>();
    private final ResourceLocation resourceLocation;

    public LiquidCapability(ResourceLocation resourceLocation) {
        CAPABILITIES.put(resourceLocation, this.getCapability());
        this.resourceLocation = resourceLocation;
    }

    @Override
    public void onEntityDeath(Entity player, Entity oldPlayer) {

    }

    @Override
    public ResourceLocation getRegistryLocation() {
        return this.resourceLocation;
    }

    @Override
    public void updateCapability() {
        this.updateCapability(Minecraft.getInstance().player);
    }

    @Override
    public void updateCapability(Player player) {

    }
}
