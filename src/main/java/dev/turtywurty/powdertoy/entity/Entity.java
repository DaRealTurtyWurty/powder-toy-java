package dev.turtywurty.powdertoy.entity;

public abstract class Entity {
    private final EntityType<?> type;

    public Entity(EntityType<?> type) {
        this.type = type;
    }

    public EntityType<?> getType() {
        return this.type;
    }

    public void update() {

    }
}
