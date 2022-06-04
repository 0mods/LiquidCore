package liquid.recipes;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class LiquidRecipeType<T extends Recipe<?>> implements RecipeType<T> {
    private final String modId;
    private final String recipeName;

    public LiquidRecipeType(String modId, String recipeName) {
        this.modId = modId;
        this.recipeName = recipeName;
    }

    @Override
    public String toString() {
        return modId + ":" + recipeName;
    }
}
