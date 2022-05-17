package core.liquid.registry.base.tasks;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import core.liquid.registry.base.LiquidRegistry;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.*;

public class SubscribeRegistry {
    public static void regToBus(List<LiquidRegistry> registers, IEventBus modEventBus) {
        Multimap<LiquidRegistry, LiquidRegistry> children = ArrayListMultimap.create();
        Map<LiquidRegistry, Integer> remainingDeps = new HashMap<>();

        Queue<LiquidRegistry> independents = new ArrayDeque<>();
        for (LiquidRegistry register : registers) {
            int depCount = 0;

            if (register instanceof RegistryOrder ) {
                for (Class<?> dependency : ((RegistryOrder) register).getDeps()) {
                    for (LiquidRegistry possibleParent : registers) {
                        if (dependency.isAssignableFrom(possibleParent.getClass())) {
                            children.put(possibleParent, register);
                            depCount++;
                        }
                    }
                }
            }

            remainingDeps.put(register, depCount);

            if (depCount == 0) {
                independents.add(register);
            }
        }

        while (!independents.isEmpty()) {
            LiquidRegistry register = independents.poll();
            register.regToBus(modEventBus);

            remainingDeps.remove(register);

            for (LiquidRegistry child : children.get(register)) {
                int depCount = remainingDeps.get(child) - 1;
                remainingDeps.put(child, depCount);

                if (depCount == 0) {
                    independents.add(child);
                }
            }
        }

        if (!remainingDeps.isEmpty()) {

            for (LiquidRegistry parent : remainingDeps.keySet()) {
                Deque<LiquidRegistry> path = new ArrayDeque<>();
                findCycles(parent, children, path);
            }
        }

    }

    private static void findCycles(LiquidRegistry parent, Multimap<LiquidRegistry, LiquidRegistry> children, Deque<LiquidRegistry> path) {
        if (path.contains(parent)) {
            StringBuilder builder = new StringBuilder();
            builder.append(parent.getClass().getName());
            while (!path.isEmpty()) {
                LiquidRegistry last = path.removeLast();
                builder.append(" -> ").append(last.getClass().getName());

                if (last == parent) break;
            }

            throw new IllegalStateException("Found cycle in register dependencies:\n" + builder);
        }

        path.addLast(parent);

        for (LiquidRegistry child : children.get(parent)) {
            findCycles(child, children, path);
        }

        path.removeLast();
    }
}
