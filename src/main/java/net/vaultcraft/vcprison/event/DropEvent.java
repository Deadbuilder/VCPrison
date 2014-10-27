package net.vaultcraft.vcprison.event;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.events.ServerEvent;
import net.vaultcraft.vcutils.events.TimeUnit;
import net.vaultcraft.vcutils.uncommon.Particles;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class DropEvent extends ServerEvent implements Listener {

    private boolean running = false;
    private Location particleLoc;
    private double rads;

    private List<FallingBlock> fallingEntities = Lists.newArrayList();

    public DropEvent() {
        super("Drop Party", 30, TimeUnit.MINUTES, 100);
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    public void onEvent(Plugin plugin) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lDROP-PARTY&7: &fThe drop party is now starting!"));

        running = true;
        int delay = 0;

        for (Location loc : Locations.pillars) {
            delay += 5;
            Runnable run = new Runnable() {
                public void run() {
                    FallingBlock f1 = Locations.world.spawnFallingBlock(loc.clone().add(0, 100, 0), Material.COBBLE_WALL, (byte) 0);
                    FallingBlock f2 = Locations.world.spawnFallingBlock(loc.clone().add(0, 102, 0), Material.BEACON, (byte) 0);
                    FallingBlock f3 = Locations.world.spawnFallingBlock(loc.clone().add(0, 104, 0), Material.COBBLE_WALL, (byte) 0);

                    f1.setDropItem(false);
                    f2.setDropItem(false);
                    f3.setDropItem(false);
                }
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), run, delay);
        }

        delay += 20;

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
                double realRads = Math.toRadians(rads);

                particleLoc = Locations.center.clone().add(Math.cos(realRads) * 9, (Math.sin(4 * realRads) * 2) + 4, Math.sin(realRads) * 9);

                Particles.FIREWORKS_SPARK.sendToLocation(particleLoc, 0, 0, 0, 0, 1);

                if (Math.random() > 0.98) {
                    FallingBlock chest = particleLoc.getWorld().spawnFallingBlock(particleLoc, Material.CHEST.getId(), (byte) 0);
                    chest.setVelocity(new Vector((Math.random() * 3) - 1.5, Math.random() * 2, (Math.random() * 3) - 1.5));
                    fallingEntities.add(chest);
                }
            }

            private Material randMat() {
                double rand = Math.random();
                if (rand < 0.33)
                    return Material.DIAMOND;
                if (rand >= 0.33 && rand < 0.66)
                    return Material.GOLD_INGOT;
                if (rand >= 0.66)
                    return Material.EMERALD;

                return Material.AIR;
            }
        };
        br.runTaskTimer(VCPrison.getInstance(), 1, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), () -> {
            running = false;
            for (Location loc : Locations.pillars.clone()) {
                for (int x = 1; x <= 3; x++) {
                    loc.add(0, 1, 0);
                    loc.getBlock().setType(Material.AIR);
                    Particles.CRIT.sendToLocation(loc.clone().add(0.5, 0, 0.5), 0, 0, 0, 1, 30);
                }
            }
            Locations.center.clone().add(0, -2, 0).getBlock().setType(Material.STONE);
        }, delay += (20 * 10));
    }

    public void onTick(Plugin plugin, int i) {
    }

    @EventHandler
    public void onBlockForm(EntityChangeBlockEvent event) {
        if (fallingEntities.contains(event.getEntity())) {
            event.setCancelled(true);
        }
    }
}
