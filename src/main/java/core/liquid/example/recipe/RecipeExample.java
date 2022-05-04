package core.liquid.example.recipe;

import core.liquid.recipes.LiquidRecipeSerializers;
import core.liquid.recipes.LiquidRecipes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.RegistryObject;

class RecipeExample extends LiquidRecipes {
    public static final RecipeType<RecipeExample> RECIPE_TYPE = new RecipeType<>() {
        @Override
        public String toString() {
            return "your_modid:recipe_name";
        }
    };
    public RecipeExample(ResourceLocation idIn, Ingredient ingredientIn, ItemStack resultIn) {
        super(RECIPE_TYPE, idIn, ingredientIn, resultIn);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return Reg.EXAMPLE_RC.get();
    }

    private class Reg {
        public static RegistryObject<LiquidRecipeSerializers<RecipeExample>> EXAMPLE_RC;
    }
}
