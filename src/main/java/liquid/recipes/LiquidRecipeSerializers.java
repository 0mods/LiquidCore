package liquid.recipes;

import liquid.helper.IngredientHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;
import org.jetbrains.annotations.Nullable;

public class LiquidRecipeSerializers<T extends LiquidRecipes> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
    protected final SerializerFactory<T> serializerFactory;

    public LiquidRecipeSerializers(SerializerFactory<T> serializerFactory) {
        this.serializerFactory = serializerFactory;
    }

    @Override
    public T fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
        JsonElement jsonIngredient = GsonHelper.getAsJsonArray(jsonObject, "ingredient");
        Ingredient ingredient = IngredientHelper.fromJson(jsonIngredient);
        ItemStack itemStack;

        if (!jsonObject.has("result"))
            throw new JsonSyntaxException("com.google.gson.JsonSyntaxException; " + LiquidRecipeSerializers.class +"; - have a message: \"RECIPE CANT BEEN CREATED! MISSING THIS ARGUMENT: 'result'\"");

        if (jsonObject.get("result").isJsonObject())
            itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
        else {
            String string = GsonHelper.getAsString(jsonObject, "result");
            ResourceLocation location = new ResourceLocation(string);
            itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(location));
        }

        return this.getSerializerFactory().create(pRecipeId, ingredient, itemStack);
    }

    @Nullable
    @Override
    public T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
        ItemStack itemStack = pBuffer.readItem();
        return this.getSerializerFactory().create(pRecipeId, ingredient, itemStack);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
        pRecipe.ingredient.toNetwork(pBuffer);
    }

    public SerializerFactory<T> getSerializerFactory() {
        return this.serializerFactory;
    };

    public interface SerializerFactory<T extends LiquidRecipes> {
        T create(ResourceLocation id, Ingredient ingredient, ItemStack result);
    }
}
