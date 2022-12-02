package liquid.objects.capability.serializer;

import liquid.objects.capability.tag.TagUtils;

public abstract class LiquidTagSerializer<T> implements TagSerializer<T> {
    public LiquidTagSerializer(String tagName) {
        TagUtils.addSerializer(this, tagName);
    }
}
