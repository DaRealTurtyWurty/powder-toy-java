package dev.turtywurty.powdertoy.core.registry;

import dev.turtywurty.powdertoy.entity.Entity;
import dev.turtywurty.powdertoy.entity.EntityType;
import dev.turtywurty.powdertoy.entity.ParticleEntity;

import java.util.function.Supplier;

public class EntityRegistry {
    public static final Supplier<EntityType<? extends Entity>> PARTICLE = BuiltinRegistries.ENTITY_TYPE.register(
            "particle", () -> new EntityType<>("particle", ParticleEntity::new));
}
