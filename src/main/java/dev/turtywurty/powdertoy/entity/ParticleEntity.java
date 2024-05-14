package dev.turtywurty.powdertoy.entity;

import dev.turtywurty.powdertoy.core.registry.EntityRegistry;

public class ParticleEntity extends Entity {

    public ParticleEntity() {
        super(EntityRegistry.PARTICLE.get());
    }

    @Override
    public void update() {

    }
}
