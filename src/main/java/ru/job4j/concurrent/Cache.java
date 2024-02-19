package ru.job4j.concurrent;

public final class Cache {
    private static volatile Cache cache;

    private Cache() {
    }

    public static Cache getInstance() {
        if (cache == null) {
            synchronized (Cache.class) {
                if (cache == null) {
                    cache = new Cache();
                }
            }
        }
        return cache;
    }
}