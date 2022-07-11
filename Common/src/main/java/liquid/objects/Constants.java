package liquid.objects;

import liquid.dynamic.container.DynamicContainer;
import liquid.objects.logging.LogHelper;
import net.minecraft.world.inventory.MenuType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {
	public static final String ModId = "liquid";
	public static final String ModName = "LiquidCore";
	public static final Logger LOGGER = LogHelper.get(ModName);
	public static MenuType<DynamicContainer> CONTAINER_TYPE;
}