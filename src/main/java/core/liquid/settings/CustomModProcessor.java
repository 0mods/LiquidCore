package core.liquid.settings;

import core.liquid.LiquidCore;
import core.liquid.network.messages.LiquidPacket;
import core.liquid.objects.annotations.NetworkDirectionPacket;
import core.liquid.objects.annotations.NotWorking;
import core.liquid.objects.annotations.Register;
import core.liquid.objects.annotations.Config;
import core.liquid.objects.client.AutoModel;
import core.liquid.objects.client.PackResourcesAdapter;
import core.liquid.objects.config.ConfigVariable;
import core.liquid.objects.config.MainConfig;
import core.liquid.network.NetworkHandler;
//import core.liquid.objects.block.LiquidBlockBase;
import core.liquid.objects.item.ArmorBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Locale;

import static net.minecraftforge.registries.DeferredRegister.*;

@SuppressWarnings("ALL")
@NotWorking
public class CustomModProcessor {
    private static final Type REGISTRATION = Type.getType(Register.class);
    private static final Type NETWORK_DIRECTION = Type.getType(NetworkDirectionPacket.class);
    private static final Type CONFIG = Type.getType(Config.class);

    public static synchronized void run(String modId, ModFileScanData scanData) {
        LiquidCore.log.info("start CMP");
        registry(scanData, modId);
    }

    public static void registry(ModFileScanData scanData, String modId) {
        LiquidCore.log.info("using CMP's Registers");
        DeferredRegister<Block> BLOCKS = create(ForgeRegistries.BLOCKS, modId);
        DeferredRegister<Item> ITEMS = create(ForgeRegistries.ITEMS, modId);
        DeferredRegister<BlockEntityType<?>> TILES = create(ForgeRegistries.BLOCK_ENTITIES, modId);
        DeferredRegister<SoundEvent> SOUNDS = create(ForgeRegistries.SOUND_EVENTS, modId);
        DeferredRegister<StructureFeature<?>> STRUCTURES = create(ForgeRegistries.STRUCTURE_FEATURES, modId);
        DeferredRegister<EntityType<?>> ENTITIES = create(ForgeRegistries.ENTITIES, modId);

        scanData.getAnnotations().stream()
                .filter(annotationData ->
                        annotationData.annotationType().equals(REGISTRATION)

                )
                .forEach(annotationData -> {
                            String containerClassName = annotationData.clazz().getClassName();
                            Class<?> containerClass;
                            try {
                                containerClass = Class.forName(containerClassName);
                            } catch (Throwable e) {
                                throw new RuntimeException(String.format("There was an exception while trying to load %s", containerClassName), e);
                            }

                            if (annotationData.annotationType().equals(REGISTRATION)) {
                                String fieldName = annotationData.memberName();
                                Field field = findField(containerClass, fieldName);
                                field.setAccessible(true);
                                String regName = fieldName.toLowerCase(Locale.ROOT);
                                boolean hasAutoModel = field.getAnnotation(Register.class).auto_model();

                                if (Modifier.isStatic(field.getModifiers())) {
                                    try {
                                        Object someObject = field.get(null);

                                        /*if (someObject instanceof LiquidBlockBase block) {
                                            BLOCKS.register(regName, () -> block);
                                            ITEMS.register(regName, () -> new BlockItem(block, block.itemProperties()));
                                        } else */if (someObject instanceof Item item) {
                                            ITEMS.register(regName, () -> item);
                                            if (hasAutoModel) {
                                                AutoModel.genModels.add(new ResourceLocation(modId, regName));
                                            }
                                        } else if (someObject instanceof BlockEntityType<?> tile) {
                                            TILES.register(regName, () -> tile);
                                        } else if (someObject instanceof SoundEvent sound) {
                                            SOUNDS.register(regName, () -> sound);
                                            if (someObject instanceof EntityType<?>) {
                                                ENTITIES.register(regName, () -> (EntityType<?>) someObject);
                                            } else if (someObject instanceof ArmorBase<?> armor) {
                                                if (hasAutoModel) {
                                                    armor.registerModels(modId, regName);
                                                }
                                                armor.registerItems(ITEMS, regName);
                                            }
                                        }
                                    } catch (IllegalAccessException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else if (annotationData.annotationType().equals(NETWORK_DIRECTION)) {

                                if (LiquidPacket.class.isAssignableFrom(containerClass)) {
                                    NetworkHandler.registerMessage((Class<LiquidPacket>) containerClass);
                                }
                            } else if (annotationData.annotationType().equals(CONFIG)) {
                                String fieldName = annotationData.memberName();
                                Field field = findField(containerClass, fieldName);

                                if (Modifier.isStatic(field.getModifiers())) {
                                    if (Modifier.isFinal(field.getModifiers())) {
                                        try {
                                            Object someObject = field.get(null);
                                            if (someObject instanceof Boolean) {
                                                boolean val = MainConfig.tryGetBool(fieldName, (Boolean) someObject);
                                                setFinalStatic(field, val);
                                            } else if (someObject instanceof Integer) {
                                                int val = MainConfig.tryGetInt(fieldName, (Integer) someObject);
                                                setFinalStatic(field, val);
                                            } else if (someObject instanceof Float) {
                                                float val = MainConfig.tryGetFloat(fieldName, (Float) someObject);
                                                setFinalStatic(field, val);
                                            }
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        try {
                                            Object someObject = field.get(null);
                                            if (someObject instanceof ConfigVariable variable) {
                                                if (variable.getValue() instanceof Boolean) {
                                                    variable.setValue(MainConfig.tryGetBool(variable.getName(), (Boolean) variable.getValue()));
                                                } else if (variable.getValue() instanceof Integer) {
                                                    variable.setValue(MainConfig.tryGetInt(variable.getName(), (Integer) variable.getValue()));
                                                } else if (variable.getValue() instanceof Float) {
                                                    variable.setValue(MainConfig.tryGetFloat(variable.getName(), (Float) variable.getValue()));
                                                }
                                            }
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }

                        }
                );

        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
        SOUNDS.register(FMLJavaModLoadingContext.get().getModEventBus());
        STRUCTURES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        PackResourcesAdapter.registerResourcePack(AutoModel.getPackInstance());
    }

    public static Field findField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Throwable e) {
            throw new RuntimeException("Can't retrieve field " + fieldName + " from class " + clazz, e);
        }
    }

    static void setFinalStatic(Field field, Object newValue)  {
        try {
            field.setAccessible(true);
            Field modifiersField = Field.class.getDeclaredField("modifiers");


            AccessController.doPrivileged((PrivilegedAction) () -> {
                modifiersField.setAccessible(true);
                return null;
            });

            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(null, newValue);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
