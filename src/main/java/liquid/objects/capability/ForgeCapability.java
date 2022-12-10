package liquid.objects.capability;

import com.google.common.base.Throwables;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;

public class ForgeCapability<T> extends Capability<T> {
    private final Callable<? extends T> factory;

    public ForgeCapability(String name, Callable<? extends T> factory) {
        super(name);
        this.factory = factory;
    }

    @Nullable
    public T getDefaultInstance() {
        try {
            return this.factory.call();
        } catch (Exception e) {
            Throwables.throwIfUnchecked(e);
            throw  new RuntimeException(e);
        }
    }
}
