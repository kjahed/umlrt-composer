package ca.jahed.umlrt.composer;

import java.util.*;

public abstract class CachedVisitor {
    private final Map<Object, Object> cache = new HashMap<>();

    protected void cache(Object key, Object value) {
        cache.put(key, value);
    }

    protected boolean visited(Object key) {
        return cache.containsKey(key);
    }

    protected Object cached(Object key) {
        return cache.getOrDefault(key, null);
    }
}
