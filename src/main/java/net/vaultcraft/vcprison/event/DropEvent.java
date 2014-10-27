package net.vaultcraft.vcprison.event;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.crate.CrateFile;
import net.vaultcraft.vcprison.crate.CrateItem;
import net.vaultcraft.vcutils.protection.Area;
import net.vaultcraft.vcutils.uncommon.FireworkEffectPlayer;
import net.vaultcraft.vcutils.uncommon.Particles;
import org.bukkit.*;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Item;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;


/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class DropEvent implements Listener {

    private boolean running = false;
    private Location particleLoc;
    private double rads;

    public DropEvent() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    public void onEvent(Plugin plugin) {
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lDROP-PARTY&7: &fThe drop party is now starting!"));
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lDROP-PARTY&7: &fType /spawn to get to the dp!"));

        running = true;
        int delay = 0;

        for (Location loc : Locations.pillars) {
            delay += 5;
            Runnable run = () -> {
                FallingBlock f1 = Locations.world.spawnFallingBlock(loc.clone().add(0, 140, 0), Material.COBBLE_WALL, (byte) 0);
                FallingBlock f2 = Locations.world.spawnFallingBlock(loc.clone().add(0, 142, 0), Material.BEACON, (byte) 0);
                FallingBlock f3 = Locations.world.spawnFallingBlock(loc.clone().add(0, 144, 0), Material.COBBLE_WALL, (byte) 0);

                f1.setDropItem(false);
                f2.setDropItem(false);
                f3.setDropItem(false);
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), run, delay);
        }

        delay += 20;

        Bukkit.getScheduler().runTaskLater(plugin, () -> Locations.center.clone().add(0, -2, 0).getBlock().setType(Material.DIAMOND_BLOCK), delay);

        BukkitRunnable br = new BukkitRunnable() {
            public void run() {
                if (!running) {
                    cancel();
                    return;
                }

                rads++;
                double realRads = Math.toRadians(rads);

                double x = Math.cos(realRads);
                double y = (Math.sin(4 * realRads) * 2) + 4;
                double z = Math.sin(realRads);
                particleLoc = Locations.center.clone().add(x*9, y, z*9);

                Particles.FIREWORKS_SPARK.sendToLocation(particleLoc, 0, 0, 0, 0, 1);
                particleLoc.getWorld().playSound(particleLoc, Sound.AMBIENCE_CAVE, 1, 1);

                for (int i = 0; i < 5; i++) { //Comment for broken commit
                    Particles.FIREWORKS_SPARK.sendToLocation(Locations.center.clone().add(x*(8-i), y, z*(8-i)), 0, 0, 0, 0, 1);
                }

                if (Math.random() > 0.8) {
                    ItemStack[] boom = random();
                    for (ItemStack i : boom) {
                        Item drop = particleLoc.getWorld().dropItem(randomInside(Locations.spawnTopArea), i.clone());
                        drop.setTicksLived(5800);
                    }
                }
            }
        };
        br.runTaskTimer(VCPrison.getInstance(), 1, 1);

        Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), () -> {
            running = false;
            int tDelay = 0;
            for (Location loc : Locations.pillars) {
                Location clone = loc.clone();
                Runnable rd = () -> {
                    for (int x = 1; x <= 3; x++) {
                        clone.add(0, 1, 0);

                        FallingBlock fb = loc.getWorld().spawnFallingBlock(clone, clone.getBlock().getTypeId(), clone.getBlock().getData());
                        fb.setVelocity(new Vector(0, 3, 0));

                        Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), () -> {
                            fb.remove();
                            FireworkEffectPlayer.playFirework(fb.getWorld(), fb.getLocation(), randomEffect());
                        }, 20);

                        clone.getBlock().setType(Material.AIR);
                        Particles.SPELL.sendToLocation(loc.clone().add(0.5, 0, 0.5), 0, 0, 0, 1, 30);
                    }
                };
                Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), rd, tDelay+=5);
            }

            Locations.center.clone().add(0, -2, 0).getBlock().setType(Material.STONE);
        }, delay += (20 * 20));
    }

    private static FireworkEffect randomEffect() {
        Color c1 = Color.fromRGB((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));
        Color c2 = Color.fromRGB((int)(Math.random()*255), (int)(Math.random()*255), (int)(Math.random()*255));

        FireworkEffect.Builder b = FireworkEffect.builder().withColor(c1).withFade(c2).with(FireworkEffect.Type.values()[(int)(Math.random()*FireworkEffect.Type.values().length)]);

        if (Math.random() > .5)
            b = b.withFlicker();
        if (Math.random() > .5)
            b = b.withTrail();

        return b.build();
    }

    private static ItemStack[] random() {
        int amount = (int)(Math.random()*10)+1;
        ItemStack[] random = new ItemStack[amount];

        int pos = 0;
        while (random[random.length-1] == null) {
            random[pos] = rand();
            pos++;
        }

        return random;
    }

    private static ItemStack rand() {
        double r = Math.random();
        if (r < 0.33)
            return new ItemStack(Material.EMERALD_BLOCK, 64);
        if (r >= 0.33 && r < 0.66)
            return new ItemStack(Material.DIAMOND_BLOCK, 64);
        return new ItemStack(Material.GOLD_BLOCK, 64);
    }

    private static Location randomInside(Area area) {
        Location low = area.getMin();
        Location hi = area.getMax();

        int x = randInt(low.getBlockX(), hi.getBlockX());
        int y = randInt(low.getBlockY(), hi.getBlockY());
        int z = randInt(low.getBlockZ(), hi.getBlockZ());

        return new Location(area.getMax().getWorld(), x, y, z);
    }

    private static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}
