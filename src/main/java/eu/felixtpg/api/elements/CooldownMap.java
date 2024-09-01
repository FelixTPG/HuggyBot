package eu.felixtpg.api.elements;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class CooldownMap {

    private final HashMap<String, Long> cooldowns;
    private final long defaultCooldown;

    public CooldownMap(long defaultCooldown) {
        this.cooldowns = new HashMap<>();

        if (defaultCooldown <= 0) throw new IllegalArgumentException("Default cooldown cannot be negative or zero!");
        this.defaultCooldown = defaultCooldown;
    }

    /**
     * Set a specific cooldown for a key. If you want to remove the cooldown, set the seconds to 0. If you want to set the default cooldown, set the seconds to -1.
     * @param key
     * @param seconds
     */
    public void setCooldown(String key, long seconds) {
        if (seconds == 0) {
            this.cooldowns.remove(key);
            return;
        }

        if (seconds == -1) seconds = this.defaultCooldown;
        this.cooldowns.put(key, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds));
    }

    public boolean isOnCooldown(String key) {
        return this.cooldowns.containsKey(key) && this.cooldowns.get(key) > System.currentTimeMillis();
    }

    public long getCooldownSeconds(String key) {
        long cooldownLeft = TimeUnit.MILLISECONDS.toSeconds(this.cooldowns.get(key) - System.currentTimeMillis());
        return cooldownLeft < 0 ? 0 : cooldownLeft;
    }

}
