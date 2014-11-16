package net.vaultcraft.vcprison.candy;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class Jawbreaker implements Candy {

    public Recipe getRecipe() {
        ShapelessRecipe snowball = new ShapelessRecipe(getCandyItem());
        snowball.addIngredient(4, Material.SNOW_BLOCK);
        return snowball;
//        CandyShapelessRecipe rc = new CandyShapelessRecipe(this.getCandyItem());
//        rc.addIngredient(4, CandyItems.SUGARCUBE);
//        return rc;
    }

    public ItemStack getCandyItem() {
        return CandyItems.JAWBREAKER;
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public int getHarmfulAfter() {
        return 0;
    }

    private static HashMap<Projectile, Player> jawbreakers = new HashMap<>();

    public ItemStack onCandyConsume(Player player, boolean harmful) {
        return null;
    }

    @EventHandler
    public void onProjectileHit(EntityDamageByEntityEvent event) {
        if (jawbreakers.containsKey(event.getDamager())) {
            jawbreakers.remove(event.getDamager());

            if (event.getEntity() instanceof Player) {
                Player hit = (Player)event.getEntity();
                hit.removePotionEffect(PotionEffectType.WEAKNESS);
                hit.removePotionEffect(PotionEffectType.BLINDNESS);
                hit.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20 * 6, 0));
                hit.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20 * 6, 0));

                hit.getWorld().playSound(hit.getLocation(), Sound.ITEM_BREAK, 1, 1);
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        if (event.getEntity() instanceof Snowball) {
            jawbreakers.put(event.getEntity(), (Player)event.getEntity().getShooter());
        }
    }
}
