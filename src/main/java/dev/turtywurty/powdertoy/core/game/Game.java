package dev.turtywurty.powdertoy.core.game;

import dev.turtywurty.powdertoy.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private final List<Entity> entities = new ArrayList<>();

    public void start() {

    }

    public void update() {
        this.entities.forEach(Entity::update);
    }

    public List<Entity> getEntities() {
        return List.copyOf(this.entities);
    }
}
