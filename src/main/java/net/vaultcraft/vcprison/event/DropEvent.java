package net.vaultcraft.vcprison.event;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.events.ServerEvent;
import net.vaultcraft.vcutils.events.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class DropEvent extends ServerEvent implements Listener {

    private boolean running = false;

    public DropEvent() {
        super("Drop Party", 30, TimeUnit.MINUTES, 100);
    }

    public void onEvent(Plugin plugin) {
        Locations.world.setStorm(true);
        Locations.world.setWeatherDuration(20 * 60 * 5); // 5 minutes
        running = true;

        int p1IndexDelay = 0;
        for (Location loc : Locations.pillars) {
            p1IndexDelay+=10;
            Runnable run = new Runnable() {
                public void run() {
                    FallingBlock f1 = Locations.world.spawnFallingBlock(loc.clone().add(0, 100, 0), Material.COBBLE_WALL, (byte)0);
                    FallingBlock f2 = Locations.world.spawnFallingBlock(loc.clone().add(0, 102, 0), Material.BEACON, (byte)0);
                    FallingBlock f3 = Locations.world.spawnFallingBlock(loc.clone().add(0, 104, 0), Material.COBBLE_WALL, (byte)0);

                    f1.setDropItem(false);
                    f2.setDropItem(false);
                    f3.setDropItem(false);
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), run, p1IndexDelay);
        }
    }

    public void onTick(Plugin plugin, int i) {
    }

    public void onBlockForm(EntityChangeBlockEvent event) {

    }
}
