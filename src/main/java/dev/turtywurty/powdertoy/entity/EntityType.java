package dev.turtywurty.powdertoy.entity;

import dev.turtywurty.powdertoy.core.registry.Registerable;

public record EntityType<T extends Entity>(String name, EntityConstructor<T> entityConstructor) implements Registerable {
    @FunctionalInterface
    public interface EntityConstructor<T extends Entity> {
        T create();
    }
}
