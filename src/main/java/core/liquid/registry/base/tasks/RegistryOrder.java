package core.liquid.registry.base.tasks;

import core.liquid.registry.base.LiquidRegistry;

import java.util.List;

public interface RegistryOrder {
    List<Class<? extends LiquidRegistry>> getDeps();
}
