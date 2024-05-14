package dev.turtywurty.powdertoy.core.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class Registry<T extends Registerable> {
    private final String id;
    private final Class<T> clazz;

    private final Map<String, Supplier<T>> entries = new HashMap<>();

    public Registry(String id, Class<T> clazz) {
        this.id = id;
        this.clazz = clazz;
    }

    public String getId() {
        return this.id;
    }

    public Class<T> getClazz() {
        return this.clazz;
    }

    public Supplier<T> register(String name, Supplier<T> supplier) {
        if (this.entries.containsKey(name))
            throw new IllegalArgumentException("Entry with name " + name + " already exists in registry " + this.id);

        this.entries.put(name, supplier);
        return supplier;
    }

    public T get(String name) {
        if (!this.entries.containsKey(name))
            throw new IllegalArgumentException("Entry with name " + name + " does not exist in registry " + this.id);

        return this.entries.get(name).get();
    }

    public Supplier<T> getSafe(String name) {
        return this.entries.get(name);
    }

    public Optional<T> getOptional(String name) {
        return Optional.ofNullable(this.entries.get(name)).map(Supplier::get);
    }

    public boolean contains(String name) {
        return this.entries.containsKey(name);
    }

    public Map<String, Supplier<T>> getEntries() {
        return Map.copyOf(this.entries);
    }
}
