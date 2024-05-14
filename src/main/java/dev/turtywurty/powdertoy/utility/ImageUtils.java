package dev.turtywurty.powdertoy.utility;

import dev.turtywurty.powdertoy.core.rendering.Image;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.MissingResourceException;

import static org.lwjgl.stb.STBImage.stbi_load;

public class ImageUtils {
    public static Image load(String path) {
        ByteBuffer image;
        int width, height;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer channelBuf = stack.mallocInt(1);
            final IntBuffer widthBuf = stack.mallocInt(1);
            final IntBuffer heightBuf = stack.mallocInt(1);

            // 4 channels so that we can handle ARGB
            image = stbi_load(path, widthBuf, heightBuf, channelBuf, 4);
            if (image == null)
                throw new MissingResourceException("Unable to load image!", ByteBuffer.class.getSimpleName(), path);

            width = widthBuf.get();
            height = heightBuf.get();
        }

        return new Image(image, width, height);
    }

    public static GLFWImage.Buffer toGLFWImage(Image image) {
        final GLFWImage img = GLFWImage.malloc();
        final GLFWImage.Buffer imgBuf = GLFWImage.malloc(1);
        img.set(image.width(), image.height(), image.image());
        imgBuf.put(img);

        return imgBuf;
    }
}