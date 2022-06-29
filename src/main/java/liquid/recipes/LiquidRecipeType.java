package liquid.recipes;

import liquid.LiquidCore;
import net.minecraft.world.item.crafting.RecipeType;

public class LiquidRecipeType<T extends LiquidRecipes> implements RecipeType<T> {
    private final String modId;
    private final String name;

    public LiquidRecipeType(String modId, String name) {
        this.modId = modId;
        this.name = name;
    }

    public LiquidRecipeType(String name) {
        this.modId = "";
        this.name = name;
    }

    @Override
    public String toString() {
        if (modId.isEmpty() || modId.equals(LiquidCore.ModId)) {
            return LiquidCore.ModId + ":" + name;
        }
        return modId + ":" + name;
    }
}
