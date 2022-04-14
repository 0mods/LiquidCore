package core.liquid.objects.block.entity;

import core.liquid.recipes.LiquidRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.RecipeHolder;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractRecipedBlockEntity extends AbstractBlockEntity implements RecipeHolder {
    private final RecipeType<? extends LiquidRecipes> recipeType;
    
    public AbstractRecipedBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, RecipeType<? extends LiquidRecipes> pRecipeType) {
        super(pType, pWorldPosition, pBlockState);
        this.recipeType = pRecipeType;
    }

    @Nullable
    @Override
    public Recipe<?> getRecipeUsed() {
        return null;
    }
}
