package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.ffa.FFAHandler;
import net.vaultcraft.vcutils.protection.Area;
import net.vaultcraft.vcutils.uncommon.Particles;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class Cookie implements Candy {

    @Override
    public Recipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(CandyItems.COOKIE);
        shapedRecipe.shape("xyz", "yay", "zyx");
        shapedRecipe.setIngredient('x', Material.INK_SACK.getNewData((byte)11));
        shapedRecipe.setIngredient('y', Material.CLAY_BRICK);
        shapedRecipe.setIngredient('z', Material.NETHER_STAR);
        shapedRecipe.setIngredient('a', Material.SNOW_BLOCK);
        return shapedRecipe;
    }

    @Override
    public ItemStack getCandyItem() {
        return CandyItems.COOKIE;
    }

    @Override
    public int getCooldown() {
        return 1;
    }

    @Override
    public int getHarmfulAfter() {
        return 20;
    }

    @Override
    public ItemStack onCandyConsume(Player player, boolean harmful) {

        Location min = new Location(player.getLocation().getWorld(), player.getLocation().getX() - 10, player.getLocation().getY() + 10, player.getLocation().getZ() - 10);
        Location max = new Location(player.getLocation().getWorld(), player.getLocation().getX() + 10, player.getLocation().getY() + 10, player.getLocation().getZ() + 10);
        Area area = new Area(min , max);

        for(int i = 0; i < 25; i++) {
            Item drop = player.getWorld().dropItem(randomInside(area), new ItemStack(Material.INK_SACK, 1, (short)3));
            drop.setPickupDelay(6000);
            Runnable runnable = () -> {
                Location boom = drop.getLocation();
                drop.remove();

                Particles.HUGE_EXPLOSION.sendToLocation(boom, 0, 0, 0, 1, 1);
                boom.getWorld().playSound(boom, Sound.EXPLODE, 1, 0.7f);

                boom.getWorld().getPlayers().stream().filter(world -> world.getLocation().distance(boom) <= 4).forEach(world -> {
                    Location move = world.getLocation();

                    Vector set = move.toVector().subtract(boom.toVector()).normalize().multiply(1.2).setY(1.2);
                    world.setVelocity(set);
                    world.setFireTicks(20 * 3);

                    if (!world.getGameMode().equals(GameMode.CREATIVE) && !world.equals(FFAHandler.getRandomSpawnLocation().getWorld()))
                        world.damage(world.getMaxHealth()/5);
                });
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), runnable, (int)(Math.random()*60)+40);
        }

        return null;
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
