package core.liquid;

import core.liquid.dynamic.container.DynamicContainer;
import core.liquid.objects.annotations.LiquidMod;
import core.liquid.objects.annotations.Register;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@LiquidMod
@Mod(LiquidCore.ModId)
public class LiquidCore {
    public static final Logger log = LogManager.getLogger(LiquidCore.class);
    public static final String ModId = "liquid";

    public static MenuType<DynamicContainer> CONTAINER_TYPE;

    public LiquidCore() {
        log.info("LIQUID CORE SUCCESSFULLY STARTED");
    }
}