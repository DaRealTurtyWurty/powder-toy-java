package dev.turtywurty.powdertoy.utility.exception;

public class GLFWInitException extends IllegalStateException {
    private static final long serialVersionUID = -3149687156956301033L;

    private static final String MESSAGE = "GLFW has failed to initialize";

    public GLFWInitException() {
        super(MESSAGE);
    }

    public GLFWInitException(Throwable cause) {
        super(MESSAGE, cause);
    }
}