package liquid.objects.capability.serializer;

import net.minecraft.nbt.CompoundTag;

public interface TagSerializer<T> {
    T readTag(CompoundTag tag);
    CompoundTag toTag(T tag);
}
