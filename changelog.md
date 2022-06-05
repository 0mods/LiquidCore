# LiquidCore Version 1.12

### Changes:
    1. Fix bug with recipe serializer using
    2. Improved Config creation
    3. Deleted Deprecated classes

# LiquidCore Version 1.11

### Changes:

    1. Small redact in VoxelShapeBuilder. Method "build()" renamed to "of()"
    2. LiquidCookingRecipeSerializers is now deprecated. It is being replaced LiquidRecipeSerializers
    3. Fill renamed to FillContext
    4. LiquidRecipeSerializer now supports 4 recipe types
    5. LiquidRecipe now supports 4 recipe types
    6. TabMethod is now deprecated. Reason: MC Crash
    7. Method "create()" in DynamicContainerData renamed to "of()"
    8. Change super(DataGenerator) in AdvancementGenerator to super(DataGenerator, ExistingFileHelper)
    9. Repackage. core.liquid -> liquid
    11. BEHelper renamed to BlockEntityHelper

### Added:

    1. ExtendableConfig class. Simplification in the Config system!
    2. Created AdvancedBlockEntity class, for container blocks

### Removed:

    Removed function from AbstractBlockEntity (playerUsage), which was not used in any way.
    