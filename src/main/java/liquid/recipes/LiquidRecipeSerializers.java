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

@SuppressWarnings("ConstantConditions")
public abstract class LiquidRecipeSerializers<T extends LiquidRecipes> extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<T> {
    protected final SerializerFactory<T> serializerFactory;

    public LiquidRecipeSerializers(SerializerFactory<T> serializerFactory) {
        this.serializerFactory = serializerFactory;
    }

    @Override
    public T fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
        JsonElement jsonIngredient = GsonHelper.getAsJsonArray(jsonObject, "ingredient");
        JsonElement jsonCatalyst = GsonHelper.getAsJsonArray(jsonObject, "catalyst");

        Ingredient ingredient = IngredientHelper.fromJson(jsonIngredient);
        Ingredient catalyst = IngredientHelper.fromJson(jsonCatalyst);

        ItemStack itemStack;

        if (!jsonObject.has("result"))
            throw new JsonSyntaxException("com.google.gson.JsonSyntaxException; core.liquid.recipes.LiquidRecipeSerializers; - have a message: \"RECIPE CANT BEEN CREATED! MISSING THIS ARGUMENT: 'result'\"");

        if (jsonObject.get("result").isJsonObject())
            itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
        else {
            String string = GsonHelper.getAsString(jsonObject, "result");
            ResourceLocation location = new ResourceLocation(string);
            itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(location));
        }

        int time = GsonHelper.getAsInt(jsonObject, "time", 0);
        float xp = GsonHelper.getAsFloat(jsonObject, "xp", 0.0F);

        T typeClassic = this.getSerializerFactory().create(pRecipeId, ingredient, itemStack);
        T typeDoubleIngredient = this.getSerializerFactory().create(pRecipeId, ingredient, catalyst, itemStack);
        T typeCooking = this.getSerializerFactory().create(pRecipeId, ingredient, itemStack, time, xp);
        T typeCookingWithCatalyst = this.getSerializerFactory().create(pRecipeId, ingredient, catalyst, itemStack, time, xp);

        if (time != 0 && xp != 0) return typeCooking;
        if (catalyst != null) return typeDoubleIngredient;
        if ((time != 0 && xp != 0) && (catalyst != null)) return typeCookingWithCatalyst;
        return typeClassic;
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
        pBuffer.writeItem(pRecipe.result);
        pBuffer.writeFloat(pRecipe.experience);
        pBuffer.writeVarInt(pRecipe.time);
    }

    public abstract SerializerFactory<T> getSerializerFactory();

    public interface SerializerFactory<T extends LiquidRecipes> {
        T create(ResourceLocation id, Ingredient ingredient, ItemStack result);
        T create(ResourceLocation id, Ingredient ingredient, Ingredient catalyst, ItemStack result);
        T create(ResourceLocation id, Ingredient ingredient, ItemStack result, int cookingTime, float xp);
        T create(ResourceLocation id, Ingredient ingredient, Ingredient catalyst, ItemStack result, int cookingTime, float xp);
    }
}
