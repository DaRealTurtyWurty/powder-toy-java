package dev.turtywurty.powdertoy.core.window;

import dev.turtywurty.powdertoy.core.rendering.Image;
import dev.turtywurty.powdertoy.utility.ImageUtils;
import dev.turtywurty.powdertoy.utility.VectorUtils;
import org.joml.Vector2i;
import org.joml.Vector4f;
import org.joml.Vector4i;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private long handle;
    private final StringBuilder title;
    private Vector2i minSize, maxSize, defaultSize;
    private Vector2i size, position;
    private boolean useVsync;
    private boolean resizable;
    private boolean visible;
    private Image windowIcon = null;
    private AspectRatio aspectRatio;
    private boolean focused = false;
    private boolean fullscreen;
    private long monitorHandle;
    private boolean minimized;
    private boolean maximized;
    private boolean transparent;
    private float opacity;
    private boolean decorated;
    private boolean autoMinimize;
    private boolean alwaysOnTop;
    private boolean focusOnShow;
    private boolean initialized;
    private boolean exitOnEsc;
    private boolean startCentered;
    private Vector4f clearColor;

    private Window(Window.Builder builder) {
        this.title = builder.title;
        this.minSize = builder.minSize;
        this.maxSize = builder.maxSize;
        this.defaultSize = builder.defaultSize;
        this.size = this.defaultSize;
        this.position = builder.position;
        this.useVsync = builder.vsync;
        this.resizable = builder.resizable;
        this.visible = builder.visible;
        setIcon(builder.iconPath);
        this.aspectRatio = builder.aspectRatio;
        this.fullscreen = builder.fullscreen;
        this.monitorHandle = builder.monitorHandle;
        this.minimized = builder.minimized;
        this.maximized = builder.maximized;
        this.transparent = builder.transparent;
        this.opacity = builder.opacity;
        this.decorated = builder.decorated;
        this.autoMinimize = builder.autoMinimize;
        this.alwaysOnTop = builder.alwaysOnTop;
        this.focusOnShow = builder.focusOnShow;
        this.exitOnEsc = builder.exitOnEsc;
        this.startCentered = builder.startCentered;
        this.clearColor = builder.clearColor;
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public Window clearAspectRatio() {
        return setAspectRatio(AspectRatio.NONE);
    }

    public Window exitFullscreen() {
        if (!this.fullscreen)
            return this;

        this.fullscreen = true;
        this.monitorHandle = -1L;

        glfwSetWindowMonitor(this.handle, -1L, this.position.x(), this.position.y(), this.size.x(), this.size.y(), 0);
        return this;
    }

    public Window focus() {
        if (this.focused)
            return this;

        glfwFocusWindow(this.handle);
        this.focused = true;

        return this;
    }

    public long getHandle() {
        return this.handle;
    }

    public boolean isInitialized() {
        return this.initialized;
    }

    public Window maximize() {
        if (this.fullscreen || this.maximized)
            return this;

        glfwMaximizeWindow(this.handle);
        this.maximized = true;

        return this;
    }

    public Window minimize() {
        if (this.minimized)
            return this;

        glfwIconifyWindow(this.handle);
        this.minimized = true;

        return this;
    }

    public Window requestAttention() {
        glfwRequestWindowAttention(this.handle);
        return this;
    }

    public Window restore() {
        if (!this.minimized || !this.maximized)
            return this;

        glfwRestoreWindow(this.handle);
        this.minimized = false;

        return this;
    }

    public Window setAspectRatio(AspectRatio ratio) {
        if (this.aspectRatio == ratio)
            return this;

        this.aspectRatio = ratio;
        glfwSetWindowAspectRatio(this.handle, this.aspectRatio.x, this.aspectRatio.y);

        return this;
    }

    public Window setDefaultSize(Vector2i size) {
        this.defaultSize = size;
        return this;
    }

    public Window setIcon(String path) {
        if (path == null)
            return this;

        this.windowIcon = ImageUtils.load(path);
        if (this.windowIcon != null && this.handle != 0L) {
            glfwSetWindowIcon(this.handle, ImageUtils.toGLFWImage(this.windowIcon));
        }

        return this;
    }

    public Window setIconToDefault() {
        this.windowIcon = null;
        glfwSetWindowIcon(this.handle, null);

        return this;
    }

    public Window setMaxSize(int width, int height) {
        if (this.maxSize.x() == width && this.maxSize.y() == height)
            return this;

        this.maxSize = VectorUtils.clamp(this.minSize, VectorUtils.DONT_CARE, width, height);
        glfwSetWindowSizeLimits(this.handle, this.minSize == null ? GLFW_DONT_CARE : this.minSize.x(),
                this.minSize == null ? GLFW_DONT_CARE : this.minSize.y(), this.maxSize.x(), this.maxSize.y());

        return this;
    }

    public Window setMinSize(int width, int height) {
        if (this.minSize.x() == width && this.minSize.y() == height)
            return this;

        this.minSize = VectorUtils.clamp(VectorUtils.ZERO, this.maxSize, width, height);

        glfwSetWindowSizeLimits(this.handle, this.size.x(), this.size.y(),
                this.maxSize == null ? GLFW_DONT_CARE : this.maxSize.x(),
                this.maxSize == null ? GLFW_DONT_CARE : this.maxSize.y());

        return this;
    }

    public Window setMonitorFullscreen(long monitor) {
        if (this.fullscreen)
            return this;

        glfwSetWindowMonitor(this.handle, monitor, 0, 0, this.size.x(), this.size.y(),
                this.useVsync ? 60 : GLFW_DONT_CARE);

        this.monitorHandle = monitor;
        this.fullscreen = true;

        return this;
    }

    public Window setOpacity(float opacity) {
        if (this.opacity == opacity)
            return this;
        if (!this.transparent) {
            toggleTransparency(opacity != 1.0f);
        }

        this.opacity = opacity;
        glfwSetWindowOpacity(this.handle, opacity);

        return this;
    }

    public Window setPosition(int x, int y) {
        if (this.position.x() == x && this.position.y() == y)
            return this;

        final Vector4i bounds = getMonitorBounds(this.handle);
        final Vector2i min = new Vector2i(bounds.x(), bounds.y());
        final Vector2i max = new Vector2i(bounds.z(), bounds.w());
        this.position = VectorUtils.clamp(min, max, x, y);
        glfwSetWindowPos(this.handle, this.position.x(), this.position.y());

        return this;
    }

    public Window setSize(int width, int height) {
        if (this.size.x() == width && this.size.y() == height)
            return this;

        this.size = VectorUtils.clamp(this.minSize, this.maxSize, width, height);

        glfwSetWindowSize(this.handle, this.size.x(), this.size.y());

        return this;
    }

    public Window setTitle(String title) {
        this.title.replace(0, this.title.length(), title);
        glfwSetWindowTitle(this.handle, title);

        return this;
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(this.handle);
    }

    public void swapBuffers() {
        glfwSwapBuffers(this.handle);
    }

    public void destroy() {
        glfwFreeCallbacks(this.handle);
        glfwDestroyWindow(this.handle);

        glfwTerminate();

        GLFWErrorCallback callback = glfwSetErrorCallback(null);
        if(callback != null) {
            callback.free();
            callback.close();
        }
    }

    public void update() {
        swapBuffers();
        glfwPollEvents();
    }

    public Window toggleAlwaysOnTop(boolean alwaysOnTop) {
        if (this.alwaysOnTop == alwaysOnTop)
            return this;

        this.alwaysOnTop = alwaysOnTop;
        glfwWindowHint(GLFW_FLOATING, this.alwaysOnTop ? GLFW_TRUE : GLFW_FALSE);

        return this;
    }

    public Window toggleAutoMinimize(boolean autoMinimize) {
        if (this.autoMinimize == autoMinimize)
            return this;

        this.autoMinimize = autoMinimize;
        glfwWindowHint(GLFW_AUTO_ICONIFY, this.autoMinimize ? GLFW_TRUE : GLFW_FALSE);

        return this;
    }

    public Window toggleDecoration(boolean decorated) {
        if (this.decorated == decorated)
            return this;

        glfwSetWindowAttrib(this.handle, GLFW_DECORATED, this.decorated ? GLFW_TRUE : GLFW_FALSE);
        return this;
    }

    public Window toggleFocusOnShow(boolean focusOnShow) {
        if (this.focusOnShow == focusOnShow)
            return this;

        this.focusOnShow = focusOnShow;
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, this.focusOnShow ? GLFW_TRUE : GLFW_FALSE);

        return this;
    }

    public Window toggleResizable(boolean resizable) {
        if (this.resizable == resizable)
            return this;

        this.resizable = resizable;
        glfwSetWindowAttrib(this.handle, GLFW_RESIZABLE, this.resizable ? GLFW_TRUE : GLFW_FALSE);

        return this;
    }

    public Window toggleTransparency(boolean transparent) {
        if (this.transparent == transparent)
            return this;

        glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, this.transparent ? GLFW_TRUE : GLFW_FALSE);
        return this;
    }

    public Window toggleVisibility(boolean visible) {
        if (this.visible == visible)
            return this;

        this.visible = visible;
        glfwWindowHint(GLFW_VISIBLE, this.visible ? GLFW_TRUE : GLFW_FALSE);

        return this;
    }

    public Window toggleVsync(boolean enabled) {
        if (this.useVsync == enabled)
            return this;

        this.useVsync = enabled;
        glfwSwapInterval(this.useVsync ? 1 : 0);

        return this;
    }

    public static Vector4i getMonitorBounds(long monitor) {
        int xMin, yMin, xMax, yMax;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            final IntBuffer xMinBuf = stack.mallocInt(1);
            final IntBuffer yMinBuf = stack.mallocInt(1);
            final IntBuffer xMaxBuf = stack.mallocInt(1);
            final IntBuffer yMaxBuf = stack.mallocInt(1);

            glfwGetMonitorWorkarea(monitor, xMinBuf, yMinBuf, xMaxBuf, yMaxBuf);

            xMin = xMinBuf.get();
            yMin = yMinBuf.get();
            xMax = xMaxBuf.get();
            yMax = xMaxBuf.get();
        }

        return new Vector4i(xMin, yMin, xMax, yMax);
    }

    public static Window init(Window window) {
        // Set all the window hints
        glfwWindowHint(GLFW_RESIZABLE, window.resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_VISIBLE, window.visible ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_FOCUSED, window.focused ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_MAXIMIZED, window.maximized ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_DECORATED, window.decorated ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_AUTO_ICONIFY, window.autoMinimize ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_FLOATING, window.alwaysOnTop ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_FOCUS_ON_SHOW, window.focusOnShow ? GLFW_TRUE : GLFW_FALSE);

        // Get the monitor that should be used
        final long monitor = window.monitorHandle == -1L ? glfwGetPrimaryMonitor() : window.monitorHandle;

        // Create the window
        window.handle = glfwCreateWindow(window.size.x(), window.size.y(), window.title.toString(),
                window.monitorHandle == -1L ? 0 : monitor, window.monitorHandle == -1L ? 0 : monitor);
        if (window.handle == 0)
            throw new RuntimeException("Failed to create the GLFW window");

        // Exit on escape if it is enabled
        if (window.exitOnEsc) {
            glfwSetKeyCallback(window.handle, (Window, key, scanCode, action, mods) -> {
                if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                    glfwSetWindowShouldClose(Window, true);
                }
            });
        }

        // Set position of the window
        if (window.startCentered) {
            final GLFWVidMode vidMode = glfwGetVideoMode(monitor);
            final int xPos = (vidMode.width() - window.size.x()) / 2;
            final int yPos = (vidMode.height() - window.size.y()) / 2;

            glfwSetWindowPos(window.handle, xPos, yPos);
            window.position = new Vector2i(xPos, yPos);

        } else {
            glfwSetWindowPos(window.handle, window.position.x(), window.position.y());
        }

        // Make OpenGL the current context
        glfwMakeContextCurrent(window.handle);

        // Setup V-Sync
        glfwSwapInterval(window.useVsync ? 1 : 0);

        // show the window
        glfwShowWindow(window.handle);

        // Create the OpenGL Capabilities
        GL.createCapabilities();

        // Set the color that is used when clearing
        glClearColor(window.clearColor.x(), window.clearColor.y(), window.clearColor.z(), window.clearColor.w());

        return window;
    }

    public static class Builder {
        private Vector2i minSize = new Vector2i(800, 600);
        private Vector2i maxSize = new Vector2i(7680, 4320);
        private Vector2i defaultSize = this.minSize;
        private final StringBuilder title;
        private boolean vsync = true;
        private boolean resizable = true;
        private boolean visible = true;
        private String iconPath;
        private Vector2i position = new Vector2i(0, 0);
        private AspectRatio aspectRatio;
        private boolean fullscreen = false;
        private long monitorHandle = -1L;
        private boolean minimized = false, maximized = false;
        private boolean transparent = false;
        private float opacity = 1.0f;
        private boolean decorated = true;
        private boolean autoMinimize = false;
        private boolean alwaysOnTop = false;
        private boolean focusOnShow = false;
        private boolean exitOnEsc = false;
        private boolean startCentered = false;
        private Vector4f clearColor = new Vector4f(0.0f, 0.0f, 0.0f, 0.0f);

        private Builder(String title) {
            this.title = new StringBuilder(title);
        }

        public Builder alwaysOnTop() {
            this.alwaysOnTop = true;
            return this;
        }

        public Builder appendTitle(String text) {
            this.title.append(text);
            return this;
        }

        public Builder aspectRatio(AspectRatio ratio) {
            this.aspectRatio = ratio;
            return this;
        }

        public Builder autoMinimize() {
            this.autoMinimize = true;
            return this;
        }

        public Window build() {
            return new Window(this);
        }

        public Builder centerOnScreen() {
            this.startCentered = true;
            return this;
        }

        public Builder clearColor(float red, float green, float blue) {
            return clearColor(red, green, blue, 0.0f);
        }

        public Builder clearColor(float red, float green, float blue, float alpha) {
            this.clearColor = new Vector4f(red, green, blue, alpha);
            return this;
        }

        public Builder defaultSize(int width, int height) {
            this.defaultSize = new Vector2i(width, height);
            return this;
        }

        public Builder disableResizing() {
            this.resizable = false;
            return this;
        }

        public Builder disableVsync() {
            this.vsync = false;
            return this;
        }

        public Builder exitOnEsc() {
            this.exitOnEsc = true;
            return this;
        }

        public Builder focusOnShow() {
            this.focusOnShow = true;
            return this;
        }

        public Builder fullscreen(long monitor) {
            this.fullscreen = true;
            this.monitorHandle = monitor;
            return this;
        }

        public Builder icon(String path) {
            this.iconPath = path;
            return this;
        }

        public Builder maximized() {
            this.maximized = true;
            return this;
        }

        public Builder maxSize(int width, int height) {
            this.maxSize = new Vector2i(width, height);
            return this;
        }

        public Builder minimized() {
            this.minimized = true;
            return this;
        }

        public Builder minSize(int width, int height) {
            this.minSize = new Vector2i(width, height);
            return this;
        }

        public Builder notVisible() {
            this.visible = false;
            return this;
        }

        public Builder position(int x, int y) {
            this.position = new Vector2i(x, y);
            return this;
        }

        public Builder transparency(float opacity) {
            if (opacity < 1f) {
                this.transparent = true;
                this.opacity = opacity;
            }

            return this;
        }

        public Builder undecorate() {
            this.decorated = false;
            return this;
        }

        public static Builder create(String title) {
            return new Builder(title);
        }
    }
}
