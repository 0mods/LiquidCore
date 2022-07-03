package liquid.objects.registry;

import com.mojang.serialization.Codec;
import liquid.recipes.LiquidRecipeSerializers;
import liquid.recipes.LiquidRecipes;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;
import java.util.function.Supplier;

@ApiStatus.Experimental
public abstract class RegistryBase {
    String modId;
    public abstract void init();
    public static native RegistryBase get();

    public String setModId(String newId) {
        modId = newId;
        return modId;
    }

    public <T extends Entity> EntityType<T> entity(String id, Supplier<EntityType<T>> toReg) {
        return Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(modId, id), toReg.get());
    }

    public <T extends Item> T item(String id, Supplier<T> toReg) {
        return Registry.register(Registry.ITEM, new ResourceLocation(modId, id), toReg.get());
    }

    public <T extends Block> T block(String id, Function<T, Item> toItemBlock, Supplier<T> toReg) {
        T b = Registry.register(Registry.BLOCK, new ResourceLocation(modId, id), toReg.get());
        item(id, ()-> toItemBlock.apply(b));
        return b;
    }

    public <T extends AbstractContainerMenu> MenuType<T> container(String id, MenuType.MenuSupplier<T> toReg) {
        return Registry.register(Registry.MENU, new ResourceLocation(modId, id), new MenuType<>(toReg));
    }

    public <S extends Structure> StructureType<S> structure(String id, Supplier<Codec<S>> codec) {
        return Registry.register(Registry.STRUCTURE_TYPES, new ResourceLocation(modId, id), codec::get);
    }

    public <T extends LiquidRecipes> LiquidRecipeSerializers<T> recipe(String id, Supplier<LiquidRecipeSerializers.SerializerFactory<T>> factorySupplier) {
        LiquidRecipeSerializers<T> serializer = new LiquidRecipeSerializers<>(factorySupplier.get());
        return Registry.register(Registry.RECIPE_SERIALIZER, new ResourceLocation(modId, id), serializer);
    }
}
