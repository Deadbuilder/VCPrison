package net.vaultcraft.vcprison.event;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.events.ServerEvent;
import net.vaultcraft.vcutils.events.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class DropEvent extends ServerEvent implements Listener {

    private boolean running = false;
    private Location particleLoc;
    private double rads;

    public DropEvent() {
        super("Drop Party", 30, TimeUnit.MINUTES, 100);
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    public void onEvent(Plugin plugin) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lDROP-PARTY&7: &fThe drop party is now starting!"));

        running = true;

        int delay = 0;

        for (Location loc : Locations.pillars) {
            delay+=5;
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
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), run, delay);
        }

        delay+=20;

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                Locations.center.clone().add(0, -2, 0).getBlock().setType(Material.DIAMOND_BLOCK);
            }
        }, delay);

        BukkitRunnable br = new BukkitRunnable() {
            public void run() {
                if (!running) {
                    cancel();
                    return;
                }

                rads++;
                particleLoc = Locations.center.clone().add(Math.cos(rads)*9, 1, Math.sin(rads)*9);
            }
        };
        br.runTaskTimer(VCPrison.getInstance(), 2, 2);
    }

    public void onTick(Plugin plugin, int i) {
    }

    @EventHandler
    public void onBlockForm(EntityChangeBlockEvent event) {

    }
}
