package com.algorithmlx.liquid.recipes;

import com.algorithmlx.liquid.recipes.helper.IngredientHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public abstract class LiquidRecipes implements Recipe<Container> {
    protected final RecipeType<?> recipeType;
    protected final ResourceLocation resourceLocation;
    protected final Ingredient ingredient;
    protected final ItemStack result;
    protected final float xp;
    protected final int time;

    public LiquidRecipes(RecipeType<?> recipeType, ResourceLocation id, Ingredient ingredient, ItemStack result, float xp, int time) {
        this.recipeType = recipeType;
        this.resourceLocation = id;
        this.ingredient = ingredient;
        this.result = result;
        this.xp = xp;
        this.time = time;
    }

    public LiquidRecipes(RecipeType<?> typeIn, ResourceLocation idIn, Ingredient ingredientIn, ItemStack resultIn) {
        this.recipeType = typeIn;
        this.resourceLocation = idIn;
        this.ingredient = ingredientIn;
        this.result = resultIn;
        this.xp = 0;
        this.time = 0;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return IngredientHelper.test(this.ingredient, pContainer);
    }

    @Override
    public ItemStack assemble(Container pContainer) {
        return this.result.copy();
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result.copy();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> nonNull = NonNullList.create();
        nonNull.add(this.ingredient);
        return nonNull;
    }
    public float getXP() {
        return this.xp;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getTime() {
        return this.time;
    }

    @Override
    public ResourceLocation getId() {
        return this.resourceLocation;
    }

    @Override
    public RecipeType<?> getType() {
        return this.recipeType;
    }

    @Override
    public String toString() {
        return "LiquidRecipes {" +
                "recipeType=" + recipeType +
                ", resourceLocation=" + resourceLocation +
                ", ingredient=" + ingredient +
                ", result=" + result +
                ", xp=" + xp +
                ", time=" + time +
                '}';
    }
}
