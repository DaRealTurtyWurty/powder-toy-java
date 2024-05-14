package dev.turtywurty.powdertoy.core.game;

import dev.turtywurty.powdertoy.core.window.Scene;
import dev.turtywurty.powdertoy.entity.Entity;
import dev.turtywurty.powdertoy.entity.EntityRenderer;
import dev.turtywurty.powdertoy.entity.EntityType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntityManager {
    private static final Map<EntityType<?>, EntityRenderer.Constructor> ENTITY_RENDERERS = new HashMap<>();
    private static final Map<EntityType<?>, EntityRenderer> ENTITY_RENDERER_CACHE = new HashMap<>();

    public static void registerRenderer(EntityType<?> entityType, EntityRenderer.Constructor renderer) {
        ENTITY_RENDERERS.put(entityType, renderer);
    }

    public static EntityRenderer getEntityRenderer(EntityType<?> entityType) {
        return ENTITY_RENDERER_CACHE.computeIfAbsent(entityType, et -> ENTITY_RENDERERS.get(et).create());
    }

    public void render(Scene scene, Game game) {
        List<Entity> entities = game.getEntities();

        // TODO: sort entities by z-index and render them in order
        // TODO: only render entities that are visible in the scene

        for(Entity entity : entities) {
            getEntityRenderer(entity.getType()).render(entity);
        }
    }
}
