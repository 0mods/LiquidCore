package core.liquid.helper.block;

import core.liquid.recipes.LiquidRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class AbstractRecipedBlockEntity extends AbstractBlockEntity implements RecipeHolder {
    
    private final RecipeType<? extends LiquidRecipes> recipeType;
    
    public AbstractRecipedBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, RecipeType<? extends LiquidRecipes> pRecipeType) {
        super(pType, pWorldPosition, pBlockState);
        this.recipeType = pRecipeType;
    }
}
