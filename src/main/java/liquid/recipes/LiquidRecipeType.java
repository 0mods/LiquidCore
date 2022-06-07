package liquid.recipes;

import liquid.LiquidCore;
import net.minecraft.world.item.crafting.RecipeType;

public record LiquidRecipeType<T extends LiquidRecipes>(String modId, String recipeName) implements RecipeType<T> {
    @Override
    public String toString() {
        if (modId == null)
            return LiquidCore.ModId + ":" + recipeName;
        return modId + ":" + recipeName;
    }
}
