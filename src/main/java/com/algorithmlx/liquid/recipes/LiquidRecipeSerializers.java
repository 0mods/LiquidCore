package com.algorithmlx.liquid.recipes;

import com.algorithmlx.liquid.recipes.helper.IngredientHelper;
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
    public final SerializerFactory<T> serializerFactory;

    public LiquidRecipeSerializers(SerializerFactory<T> serializerFactory) {
        this.serializerFactory = serializerFactory;
    }

    @Override
    public T fromJson(ResourceLocation pRecipeId, JsonObject jsonObject) {
        JsonElement jsonElement = GsonHelper.getAsJsonArray(jsonObject, "ingredient");
        Ingredient ingredient = IngredientHelper.fromJson(jsonElement);
        ItemStack itemStack;

        if (!jsonObject.has("result"))
            throw new JsonSyntaxException("com.google.gson.JsonSyntaxException; com.algorithmlx.liquid.recipes.LiquidRecipeSerializers; - have a message: \"RECIPE CANT BEEN CREATED! MISSING THIS ARGUMENT: 'result'\"");

        if (jsonObject.get("result").isJsonObject())
            itemStack = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));
        else {
            String string = GsonHelper.getAsString(jsonObject, "result");
            ResourceLocation location = new ResourceLocation(string);
            itemStack = new ItemStack(ForgeRegistries.ITEMS.getValue(location));
        }

        float xp = GsonHelper.getAsFloat(jsonObject, "exp", 0.0F);
        int time = GsonHelper.getAsInt(jsonObject, "cooktime", 0);

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
    }

    public interface SerializerFactory<T extends LiquidRecipes> {
        T create(ResourceLocation id, Ingredient ingredient, ItemStack result, float xp, int time);
    }
}
