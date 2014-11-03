package net.vaultcraft.vcprison.candy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class ChocolateBar implements Candy {

    @Override
    public ShapedRecipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(CandyItems.CHOCOLATEBAR);
        shapedRecipe.shape("xyx", "aza", "xyx");
        shapedRecipe.setIngredient('x', Material.QUARTZ);
        shapedRecipe.setIngredient('y', Material.INK_SACK.getNewData((byte)3));
        shapedRecipe.setIngredient('a', Material.CLAY_BRICK);
        return shapedRecipe;
    }

    @Override
    public ItemStack getCandyItem() {
        return CandyItems.CHOCOLATEBAR;
    }

    @Override
    public int getCooldown() {
        return 15;
    }

    @Override
    public int getHarmfulAfter() {
        return 3;
    }

    @Override
    public ItemStack onCandyConsume(Player player, boolean harmful) {
        if(harmful) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.removePotionEffect(PotionEffectType.CONFUSION);
            player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(10 * 20, 0));
            player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(10 * 20, 0));
        } else {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.addPotionEffect(PotionEffectType.INVISIBILITY.createEffect(20 * 20, 0));
            player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(10 * 20, 0));
        }

        return CandyItems.USEDWRAPPER;
    }
}
