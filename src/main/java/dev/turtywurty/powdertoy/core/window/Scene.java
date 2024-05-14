package dev.turtywurty.powdertoy.core.window;

import dev.turtywurty.powdertoy.core.game.Game;
import dev.turtywurty.powdertoy.core.game.EntityManager;

public class Scene {
    private final EntityManager entityManager = new EntityManager();
    private final Window window;

    public Scene(Window window) {
        this.window = window;
    }

    public void render(Game game) {
        this.entityManager.render(this, game);
    }

    public Window getWindow() {
        return this.window;
    }

    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}
