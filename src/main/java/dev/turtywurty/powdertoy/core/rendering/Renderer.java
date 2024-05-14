package dev.turtywurty.powdertoy.core.rendering;

import dev.turtywurty.powdertoy.core.game.Game;
import dev.turtywurty.powdertoy.core.window.Scene;
import dev.turtywurty.powdertoy.core.window.Window;

public class Renderer {
    private final Window window;
    private final Game game;

    private Scene scene;

    public Renderer(Window window, Game game) {
        this.window = window;
        this.game = game;
    }

    public void render() {
        if (this.scene != null) {
            this.scene.render(this.game);
        }
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Window getWindow() {
        return this.window;
    }

    public Game getGame() {
        return this.game;
    }
}
