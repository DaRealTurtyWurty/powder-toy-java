package dev.turtywurty.powdertoy.core.registry;

import dev.turtywurty.powdertoy.entity.Entity;
import dev.turtywurty.powdertoy.entity.EntityType;

public class BuiltinRegistries {
    @SuppressWarnings("unchecked")
    public static final Registry<EntityType<? extends Entity>> ENTITY_TYPE = new Registry<>("entity_type",
            (Class<EntityType<?>>) (Class<?>) EntityType.class);
}
