package me.dthbr.utils.utils;

import me.dthbr.utils.DeathUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class Tasks {

    public static Tasks async() {
        return new Tasks(true);
    }

    public static Tasks sync() {
        return new Tasks(false);
    }

    public static boolean isRunning(int taskId) {
        return Bukkit.getScheduler().isCurrentlyRunning(taskId);
    }

    public static boolean isQueued(int taskId) {
        return Bukkit.getScheduler().isQueued(taskId);
    }

    public static void cancel(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    public static void cancelAll() {
        Bukkit.getScheduler().cancelTasks(DeathUtils.instance());
    }

    private final boolean async;
    private long delay = 0L;
    private long period = 0L;

    private Tasks(boolean async) {
        this.async = async;
    }

    public Tasks delay(long delay) {
        this.delay = delay;
        return this;
    }

    public Tasks period(long period) {
        this.period = period;
        return this;
    }

    public Tasks delayed(long delay) {
        this.delay = delay;
        return this;
    }

    public Tasks repeating(long delay, long period) {
        this.delay = delay;
        this.period = period;
        return this;
    }

    public int run(Runnable runnable) {
        BukkitTask task = schedule(runnable);
        return task == null ? -1 : task.getTaskId();
    }

    public BukkitTask schedule(Runnable runnable) {

        boolean repeating = period > 0L && delay > 0L;
        boolean delayed = delay > 0L;
        BukkitScheduler scheduler = Bukkit.getScheduler();
        BukkitTask bukkitTask;

        if (async) {
            if (repeating) {
                bukkitTask = scheduler.runTaskTimerAsynchronously(DeathUtils.instance(), runnable, delay, period);
            } else if (delayed) {
                bukkitTask = scheduler.runTaskLaterAsynchronously(DeathUtils.instance(), runnable, delay);
            } else {
                bukkitTask = scheduler.runTaskAsynchronously(DeathUtils.instance(), runnable);
            }
        } else {
            if (repeating) {
                bukkitTask = scheduler.runTaskTimer(DeathUtils.instance(), runnable, delay, period);
            } else if (delayed) {
                bukkitTask = scheduler.runTaskLater(DeathUtils.instance(), runnable, delay);
            } else {
                bukkitTask = scheduler.runTask(DeathUtils.instance(), runnable);
            }
        }

        return bukkitTask;
    }

}
