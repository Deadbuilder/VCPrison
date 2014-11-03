package net.vaultcraft.vcprison.candy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class SwedishFish implements Candy {

    @Override
    public Recipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(CandyItems.SWEDISHFISH);
        shapedRecipe.shape("xyx", "zaz", "xyx");
        shapedRecipe.setIngredient('x', Material.QUARTZ);
        shapedRecipe.setIngredient('y', Material.NETHER_WARTS);
        shapedRecipe.setIngredient('z', Material.INK_SACK, 1);
        shapedRecipe.setIngredient('a', Material.SNOW_BLOCK);
        return shapedRecipe;
    }

    @Override
    public ItemStack getCandyItem() {
        return CandyItems.SWEDISHFISH;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public int getHarmfulAfter() {
        return 4;
    }

    @Override
    public ItemStack onCandyConsume(Player player, boolean harmful) {
        if(harmful) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(PotionEffectType.SLOW.createEffect(20 * 10, 0));
            player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20 * 10, 0));
        } else {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 10, 0));
            player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20 * 5, 0));
        }

        return CandyItems.USEDWRAPPER;
    }
}
