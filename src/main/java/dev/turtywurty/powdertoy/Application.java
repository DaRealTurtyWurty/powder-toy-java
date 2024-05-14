package dev.turtywurty.powdertoy;

import dev.turtywurty.powdertoy.core.game.Game;
import dev.turtywurty.powdertoy.core.game.InputHandler;
import dev.turtywurty.powdertoy.core.rendering.Renderer;
import dev.turtywurty.powdertoy.core.window.Scene;
import dev.turtywurty.powdertoy.core.window.Window;

import static org.lwjgl.glfw.GLFW.glfwInit;

public class Application {
    public static void main(String[] args) {
        // setup glfw
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

        // create window
        var window = Window.Builder.create("Powder Toy").maximized().focusOnShow().disableVsync()
                .exitOnEsc().build();
        Window.init(window);

        // create the game
        var game = new Game();

        // create the renderer
        var renderer = new Renderer(window, game);

        // set the scene
        var scene = new Scene(window);
        renderer.setScene(scene);

        // create the input handler
        var inputHandler = new InputHandler(window);

        // start the game
        game.start();

        // main loop
        while (!window.shouldClose()) {
            // update the window
            window.update();

            // render the scene
            renderer.render();

            // handle input
            inputHandler.handleInput();

            // update the game
            game.update();
        }

        window.destroy();
    }
}
