package net.vaultcraft.vcprison.candy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class Butterscotch implements Candy {

    public ShapedRecipe getRecipe() {
        ShapedRecipe rc = new ShapedRecipe(getCandyItem());
        rc.shape("XYX", "YZY", "XYX");
        rc.setIngredient('X', Material.QUARTZ);
        rc.setIngredient('Y', Material.INK_SACK.getNewData((byte)11));
        rc.setIngredient('Z', Material.SNOW_BLOCK);
        return rc;
    }

    public ItemStack getCandyItem() {
        return CandyItems.BUTTERSCOTCH;
    }

    @Override
    public int getCooldown() {
        return 25;
    }

    @Override
    public int getHarmfulAfter() {
        return 4;
    }

    public ItemStack onCandyConsume(Player player, boolean harmful) {
        if(harmful) {
            player.setFireTicks(20 * 30);
        } else {
            player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
            player.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(20 * 30, 1));
        }

        return CandyItems.USEDWRAPPER;
    }
}
