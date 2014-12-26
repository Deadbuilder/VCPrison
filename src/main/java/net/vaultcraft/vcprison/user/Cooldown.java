package net.vaultcraft.vcprison.user;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.concurrent.TimeUnit;

/**
 * @author Connor Hollasch
 * @since 12/7/2014
 */
public class Cooldown {

    private Plugin plugin;
    private static final String PREF = "user_cooldown_";

    public Cooldown(Plugin plugin) {
        this.plugin = plugin;
    }

    public boolean hasCooldown(Player player, String task) {
        if (!player.hasMetadata(PREF+task))
            return false;

        MetadataValue fmv = player.getMetadata(PREF+task).get(0);
        long time = fmv.asLong();
        long at = System.currentTimeMillis();

        if (time >= at) {
            //remove and return
            player.removeMetadata(PREF+task, plugin);
            return false;
        }

        return true;
    }

    public int getSecondsLeft(Player player, String task) {
        if (!hasCooldown(player, task))
            return 0;

        long time = player.getMetadata(PREF+task).get(0).asLong();
        long at = System.currentTimeMillis();

        return (int) ((time-at)/1000);
    }

    public void addCooldown(Player player, String task, long time, TimeUnit unit) {
        long millis = unit.convert(time, TimeUnit.MILLISECONDS);
        long at = System.currentTimeMillis();

        FixedMetadataValue fmv = new FixedMetadataValue(plugin, at+millis);
        player.setMetadata(PREF+task, fmv);
    }
}
