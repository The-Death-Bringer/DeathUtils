package me.dthbr.utils.utils;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

// Cooldown Util
public class CooldownUtil {

    private final Map<UUID, Long> cooldowns;
    private final long offset;

    public CooldownUtil(final int cooldown, final TimeUnit unit) {
        this.cooldowns = Maps.newHashMap();
        this.offset = unit.toMillis(cooldown);
    }

    public void putOnCd(final UUID uuid) {
        long time = System.currentTimeMillis();
        this.cooldowns.put(uuid, time + offset);
    }

    public boolean isCdOver(final UUID uuid) {
        final long current = System.currentTimeMillis();
        final long timestamp = this.cooldowns.getOrDefault(uuid, 0L);
        return timestamp == 0L || current >= timestamp;
    }

}
