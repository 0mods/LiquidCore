package liquid.recipes;

import liquid.helper.IngredientHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistry;
import org.jetbrains.annotations.Nullable;

public class LiquidRecipeSerializers<T extends LiquidRecipes> implements RecipeSerializer<T> {
    protected final SerializerFactory<T> serializerFactory;

    public LiquidRecipeSerializers(SerializerFactory<T> serializerFactory) {
        this.serializerFactory = serializerFactory;
    }

    @Override
    public T fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
        JsonElement jsonIngredient = GsonHelper.isArrayNode(jsonObject, "ingredients")
                ? GsonHelper.getAsJsonArray(jsonObject, "ingredients"): GsonHelper.getAsJsonObject(jsonObject, "ingredients");
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

        float xp = GsonHelper.getAsFloat(jsonObject, "xp", 0.0F);
        int time = GsonHelper.getAsInt(jsonObject, "time", 0);

        return this.getSerializerFactory().create(pRecipeId, ingredient, itemStack, xp, time);
    }

    @Nullable
    @Override
    public T fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
        Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
        ItemStack itemStack = pBuffer.readItem();
        float xp = pBuffer.readFloat();
        int time = pBuffer.readInt();
        return this.getSerializerFactory().create(pRecipeId, ingredient, itemStack, xp, time);
    }

    @Override
    public void toNetwork(FriendlyByteBuf pBuffer, T pRecipe) {
        pRecipe.ingredient.toNetwork(pBuffer);
        pBuffer.writeItem(pRecipe.result);
        pBuffer.writeFloat(pRecipe.experience);
        pBuffer.writeVarInt(pRecipe.time);
    }

    public SerializerFactory<T> getSerializerFactory() {
        return this.serializerFactory;
    };

    public interface SerializerFactory<T extends LiquidRecipes> {
        T create(ResourceLocation id, Ingredient ingredient, ItemStack result, float xp, int time);
    }
}
