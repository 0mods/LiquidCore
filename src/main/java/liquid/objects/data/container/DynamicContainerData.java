package liquid.objects.data.container;

import net.minecraft.sounds.SoundEvent;

public record DynamicContainerData(int rowWidth, int rowHeight, SoundEvent soundEvent) {
    public int getRowWidth() {
        return this.rowWidth;
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    public SoundEvent getSoundEvent() {
        return this.soundEvent;
    }

    public static DynamicContainerData of(int rowWidth, int rowHeight, SoundEvent soundEvent) {
        return new DynamicContainerData(rowWidth, rowHeight, soundEvent);
    }
}
