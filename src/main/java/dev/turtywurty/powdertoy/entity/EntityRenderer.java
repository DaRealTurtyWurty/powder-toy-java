package dev.turtywurty.powdertoy.entity;

public abstract class EntityRenderer {
    public abstract void render(Entity entity);

    @FunctionalInterface
    public interface Constructor {
        EntityRenderer create();
    }
}
