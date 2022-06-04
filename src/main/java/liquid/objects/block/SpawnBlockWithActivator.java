package liquid.objects.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("all")
public class SpawnBlockWithActivator extends Block {
    public final EntityType<?> entity;
    public final Item activator;
    public final String modIdForMessage;

    public SpawnBlockWithActivator(Properties p_49795_, EntityType<?> entity, Item activator, String modIdForMessage) {
        super(p_49795_);
        this.entity = entity;
        this.activator = activator;
        this.modIdForMessage = modIdForMessage;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack mainItem = player.getMainHandItem();
        if (!level.isClientSide) {
            if ((level.getDifficulty() != Difficulty.PEACEFUL) && (mainItem.getItem() == this.activator && hand == InteractionHand.MAIN_HAND)) {
                if (!player.isCreative()) {
                    mainItem.shrink(1);
                }
                entity.spawn((ServerLevel) level, mainItem, player, pos, MobSpawnType.SPAWN_EGG, true, false);

                player.sendMessage(new TranslatableComponent("msg." + modIdForMessage + "." + ForgeRegistries.BLOCKS.getRegistryName().getPath() + ".success"), player.getUUID());
                return InteractionResult.SUCCESS;
            } else if ((level.getDifficulty() == Difficulty.PEACEFUL) && (mainItem.getItem() == activator && hand == InteractionHand.MAIN_HAND)) {
                player.sendMessage(new TranslatableComponent("msg." + modIdForMessage + ".amdanorSpawner.peaceful"), player.getUUID());
                return InteractionResult.FAIL;
            }
        }

        return InteractionResult.SUCCESS;
    }
}
