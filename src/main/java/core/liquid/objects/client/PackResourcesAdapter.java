package core.liquid.objects.client;

import core.liquid.objects.annotations.NotWorking;
import net.minecraft.server.packs.PackResources;

import java.util.ArrayList;
import java.util.List;

@NotWorking(deprecated = true)
public class PackResourcesAdapter {
    public static final List<PackResources> BUILTIN_PACKS = new ArrayList<>();

    public static void registerResourcePack(PackResources pack) {
        if(!BUILTIN_PACKS.contains(pack)) BUILTIN_PACKS.add(pack);
    }
}
