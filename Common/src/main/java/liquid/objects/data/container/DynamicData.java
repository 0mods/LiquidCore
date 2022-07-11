package liquid.objects.data.container;

import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public record DynamicData(int rowWidth, int rowHeight, SoundEvent soundEvent) {
    public int getRowWidth() {
        return this.rowWidth;
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }
    @Contract("_, _, _ -> new")
    public static @NotNull DynamicData of(int rowWidth, int rowHeight, SoundEvent soundEvent) {
        return new DynamicData(rowWidth, rowHeight, soundEvent);
    }
}
