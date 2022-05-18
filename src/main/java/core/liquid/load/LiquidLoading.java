package core.liquid.load;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.language.ModFileScanData;

public class LiquidLoading {
    public static void start() {
        ModLoadingContext context = ModLoadingContext.get();
        ModContainer container = context.getActiveContainer();

        if (container.getModId().equals("minecraft") || container.getCurrentState() != ModLoadingStage.CONSTRUCT) {
            throw new IllegalStateException("This method should be called only in mod constructor!");
        }

        IModInfo modInfo = container.getModInfo();
        ModFileScanData scanResult = modInfo.getOwningFile().getFile().getScanResult();
        ModInit.run(container.getModId(), scanResult);
    }
}
