package pl.tuso.xentities.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Objects;

public class ReflectionUtil {
    public static void setField(final Field field, final Object holder, final Object object) throws NoSuchFieldException, IllegalAccessException {
        final Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        final Unsafe unsafe = (Unsafe) theUnsafeField.get(null);
        Objects.requireNonNull(field, "field must not be null");
        final Object ufo = holder != null ? holder : unsafe.staticFieldBase(field);
        final long offset = unsafe.staticFieldOffset(field);
        unsafe.putObject(ufo, offset, object);
    }

    @Contract(value = "null, _ -> false", pure = true)
    public static boolean isInstance(Object object, @NotNull Class<?> type) {
        return type.isInstance(object);
    }
}