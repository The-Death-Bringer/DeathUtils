package me.dthbr.utils.utils;

import me.dthbr.utils.DeathUtils;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class Keys {

    public static Keys newKey(String key) {
        return new Keys(new NamespacedKey(DeathUtils.instance(), key));
    }

    public static Keys from(String namespace, String key) {
        return new Keys(new NamespacedKey(namespace, key));
    }

    public static Keys from(NamespacedKey key) {
        return new Keys(key);
    }

    public static Keys from(JavaPlugin plugin, String key) {
        return new Keys(new NamespacedKey(plugin, key));
    }

    private final NamespacedKey key;

    private Keys(NamespacedKey key) {
        this.key = key;
    }

    public <T> void set(PersistentDataContainer container, PersistentDataType<T, T> type, T value) {
        container.set(key, type, value);
    }

}
