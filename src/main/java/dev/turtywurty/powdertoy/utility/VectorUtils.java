package dev.turtywurty.powdertoy.utility;

import dev.turtywurty.powdertoy.utility.exception.IllegalInstantiationError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;

public final class VectorUtils {
    public static final Vector2i ZERO = new Vector2i(0, 0);
    public static final Vector2i DONT_CARE = new Vector2i(GLFW_DONT_CARE, GLFW_DONT_CARE);

    private VectorUtils() {
        throw new IllegalInstantiationError(VectorUtils.class);
    }

    @NotNull
    public static Vector2i clamp(@Nullable Vector2i min, @Nullable Vector2i max, int valX, int valY) {
        return new Vector2i(MathUtils.clamp(min == null ? 1 : min.x(), max == null ? GLFW_DONT_CARE : max.x(), valX),
                MathUtils.clamp(min == null ? 1 : min.y(), max == null ? GLFW_DONT_CARE : max.y(), valY));
    }

    @NotNull
    public static Vector2i clamp(@Nullable Vector2i min, @Nullable Vector2i max, @NotNull Vector2i value) {
        return clamp(min, max, value.x(), value.y());
    }
}