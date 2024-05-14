package dev.turtywurty.powdertoy.utility;

public class MathUtils {
    public static int clamp(int min, int max, int value) {
        return Math.max(min, Math.min(max, value));
    }
}
