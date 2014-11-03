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
public class Warhead implements Candy {

    @Override
    public Recipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(CandyItems.WARHEAD);
        shapedRecipe.shape("xyx", "yzy", "xyx");
        shapedRecipe.setIngredient('x', Material.QUARTZ);
        shapedRecipe.setIngredient('y', Material.MELON);
        shapedRecipe.setIngredient('z', Material.SNOW_BLOCK);
        return shapedRecipe;
    }

    @Override
    public ItemStack getCandyItem() {
        return CandyItems.WARHEAD;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public int getHarmfulAfter() {
        return 3;
    }

    @Override
    public ItemStack onCandyConsume(Player player, boolean harmful) {
        if(harmful) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20 * 15, 0));
        } else {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(20 * 10, 0));
        }
        return CandyItems.USEDWRAPPER;
    }
}
