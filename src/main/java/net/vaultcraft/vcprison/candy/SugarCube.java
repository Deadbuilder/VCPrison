package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class SugarCube implements Candy {
    @Override
    public ShapedRecipe getRecipe() {
        ShapedRecipe rc = new ShapedRecipe(getCandyItem());
        rc.shape("XXX", "XYX", "XXX");
        rc.setIngredient('X', Material.SUGAR);
        rc.setIngredient('Y', Material.MILK_BUCKET);
        return rc;
    }

    private static ItemStack cube = ItemUtils.build(Material.SNOW_BLOCK, ChatColor.translateAlternateColorCodes('&', "&lSugar Cube"),
            "Core item used to create new candies.",
            "Sugar cubes may also be consumed to receive ", "speed and jump boosts.",
            ChatColor.translateAlternateColorCodes('&', "&c&lWARNING&f: &5&oEating too many sugar cubes in such"), "short time can have negative effects!");

    public ItemStack getCandyItem() {
        return cube;
    }

    private static HashMap<Player, Integer> rush = new HashMap<>();

    public ItemStack onCandyConsume(Player player, boolean harmful) {
        int amount = 1;
        if (rush.containsKey(player)) {
            amount = rush.remove(player);
            amount++;

            if (amount >= 4) {
                player.removePotionEffect(PotionEffectType.BLINDNESS);
                player.removePotionEffect(PotionEffectType.CONFUSION);

                player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20 * 25, 0));
                player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(20 * 25, 0));
            }
        }

        rush.put(player, amount);
        if (amount < 4) {
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.SPEED);

            player.addPotionEffect(PotionEffectType.JUMP.createEffect(20 * 5, 0));
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 5, 0));
        }

        int origin = amount;
        Runnable remove = () -> {
            if (!rush.containsKey(player) || rush.get(player) != origin)
                return;

            rush.remove(player);
        };
        Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), remove, 20 * 10);

        return null;
    }
}
