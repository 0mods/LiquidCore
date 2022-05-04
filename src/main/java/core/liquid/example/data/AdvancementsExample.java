package core.liquid.example.data;

import core.liquid.objects.data.gen.AdvancementGenerator;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

class AdvancementsExample extends AdvancementGenerator {
    public AdvancementsExample(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement main = Advancement.Builder.advancement()
                .display(Items.ACACIA_BOAT,
                        new TranslatableComponent("advancement.aaaaaa.main"),
                        new TranslatableComponent("advancement.aaaaaa.main.desc"),
                        new ResourceLocation("minecraft", "textures/gui/advancements/backgrounds/stone.png"),
                        FrameType.TASK, true, true, false)
                .addCriterion("lll", InventoryChangeTrigger.TriggerInstance.hasItems(Items.ACACIA_BOAT))
                .save(consumer, "aaaaaa/main/main");
        advancement(main, Items.ACACIA_BOAT, "core/liquid/example", FrameType.TASK, true, true, false, "aaaaa");
    }
}
