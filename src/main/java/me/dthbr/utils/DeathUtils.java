package me.dthbr.utils;

import org.bukkit.plugin.java.JavaPlugin;

public final class DeathUtils {

    private static JavaPlugin instance;

    public static void instance(JavaPlugin instance) {
        DeathUtils.instance = instance;
    }

    public static void init(JavaPlugin instance) {
        instance(instance);
    }

    public static JavaPlugin instance() {
        return instance;
    }

}
