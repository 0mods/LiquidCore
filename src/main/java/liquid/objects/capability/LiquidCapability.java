package liquid.objects.capability;

import liquid.objects.capability.packet.LiquidPacket;
import liquid.objects.capability.packet.LiquidPackets;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
        LiquidPacket<LiquidCapability<?>> packet = LiquidPackets.CAPABILITY_PACKET;

        if (player.level.isClientSide) packet.sendToServer(this);
        else packet.sendToClient((ServerPlayer) player, this);
    }
}
