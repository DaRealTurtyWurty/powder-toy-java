package dev.turtywurty.powdertoy.core.window;

import static org.lwjgl.glfw.GLFW.GLFW_DONT_CARE;

public enum AspectRatio {
    ONE_ONE(1, 1), THREE_TWO(3, 2), FOUR_THREE(4, 3), FIVE_FOUR(5, 4), FOURTEEN_NINE(14, 9), SIXTEEN_NINE(16, 9),
    SIXTEEN_TEN(16, 10), SEVENTEEN_NINE(17, 9), TWENTYONE_NINE(21, 9), THIRTYTWO_NINE(32, 9),
    NONE(GLFW_DONT_CARE, GLFW_DONT_CARE);

    public final int x, y;

    AspectRatio(int x, int y) {
        this.x = x;
        this.y = y;
    }
}