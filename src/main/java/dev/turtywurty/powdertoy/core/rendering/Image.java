package dev.turtywurty.powdertoy.core.rendering;

import java.nio.ByteBuffer;

public record Image(ByteBuffer image, int width, int height) {

}