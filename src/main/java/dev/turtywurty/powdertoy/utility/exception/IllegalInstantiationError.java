package dev.turtywurty.powdertoy.utility.exception;

public class IllegalInstantiationError extends IllegalAccessError {
    private static final long serialVersionUID = -4048669845215296218L;

    public IllegalInstantiationError(Class<?> clazz) {
        super(clazz.getSimpleName() + " cannot be instantiated!");
    }

    public IllegalInstantiationError(Class<?> clazz, String extra) {
        super(clazz.getSimpleName() + " cannot be instantiated! " + extra);
    }
}