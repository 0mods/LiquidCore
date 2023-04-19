package liquid.objects.model;

import liquid.objects.model.resource.LiquidResourceGenPack;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * @author AlgorithmLX
 * I have an idea, for model loader.
 * <p>
 *     <p>
 * Preview:
 * <p>
 *     <p>
 * <code>@Model</code>
 * <p>
 * <code>
 *     public static final RegistryObject<Item> ITEM_NAME = ITEM.register("item_name", ()-> new Item(new Item.Properties()));
 * </code>
 *
 * <p>
 *     <p>
 *     in main class:
 *     <p>
 *         <p>
 *        <code>ModelLoader.initializeSystem(Registry.class)</code>
 */
@Deprecated(forRemoval = true)
public class ModelLoader {
    private final Class<?> className;

    public ModelLoader(Class<?> className) {
        this.className = className;
        this.init();
    }

    public static ModelLoader initializeSystem(Class<?> clazz) {
        return new ModelLoader(clazz);
    }

    private void init() {
        var reGen = this.getAnnotatedFields();
        for (var rGen : reGen.entrySet()) {
            var field = rGen.getKey();
            var value = rGen.getValue();

//            if (field.getDeclaringClass() == RegistryObject.class) {
//                if (value.type() == ResourceGenType.ITEM) LiquidResourceGenPack.genItemModels.add(ForgeRegistries.ITEMS.getRegistryName());
//                if (value.type() == ResourceGenType.BLOCK) LiquidResourceGenPack.genBlockModel.add(ForgeRegistries.BLOCKS.getRegistryName());
//            }

            if (field.getDeclaringClass().equals(RegistryObject.class)) {
                RegistryObject<Item> regObj = null;

                try {
                    regObj = (RegistryObject<Item>) field.get(RegistryObject.class.newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (value.type() == ResourceGenType.ITEM) LiquidResourceGenPack.genItemModels.add(regObj.getId());
                if (value.type() == ResourceGenType.BLOCK) LiquidResourceGenPack.genBlockModel.add(regObj.getId());
            }
        }
    }

    private HashMap<Field, ResourceGen> getAnnotatedFields() {
        final HashMap<Field, ResourceGen> fieldMap = new HashMap<>();
        for (Field field : className.getDeclaredFields()) {
            if (!field.isAnnotationPresent(ResourceGen.class)) {
                continue;
            }
            if (!Modifier.isStatic(field.getModifiers())) {
                throw new UnsupportedOperationException("Field \"" + field.getName() + "\" is not static");
            }
            ResourceGen annotation = field.getAnnotation(ResourceGen.class);
            fieldMap.put(field, annotation);
        }
        return fieldMap;
    }
}
