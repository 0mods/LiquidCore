package liquid.tool;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class VoxelShapeBuilder {
    private VoxelShape is;
    private VoxelShape last;

    public static VoxelShapeBuilder from(VoxelShape... shapes) {
        VoxelShapeBuilder builder = new VoxelShapeBuilder();
        for (VoxelShape shape : shapes) builder.shape(shape);

        return builder;
    }

    public VoxelShapeBuilder shape(VoxelShape shape) {
        if (this.is == null) this.is = shape;
        else {
            VoxelShape newShape = Shapes.or(this.is, shape);

            if (this.last != null) this.last = Shapes.or(this.last, newShape);
            else this.last = newShape;

            this.is = null;
        }

        return this;
    }

    public VoxelShapeBuilder cube(double xMin, double yMin, double zMin, double xMax, double yMax, double zMax) {
        VoxelShape shape = Block.box(xMin, yMin, zMin, xMax, yMax, zMax);
        return this.shape(shape);
    }

    public VoxelShape of() {
        return this.last;
    }
}
