package dev.turtywurty.powdertoy.utility.exception;

public class WindowCreateException extends RuntimeException {
    private static final long serialVersionUID = 6388761230094421475L;
    private static final String MESSAGE = "Failed to create GLFW window!";

    public WindowCreateException() {
        super(MESSAGE);
    }

    public WindowCreateException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public WindowCreateException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}