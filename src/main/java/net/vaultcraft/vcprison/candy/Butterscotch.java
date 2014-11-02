package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.ChatColor;
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

    private static ItemStack stack = ItemUtils.build(Material.INK_SACK, (byte)14, ChatColor.translateAlternateColorCodes('&', "&6&lButterscotch"), "Receive 8 minutes of fire resistance.");

    public ItemStack getCandyItem() {
        return stack;
    }

    public ItemStack onCandyConsume(Player player) {
        player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
        player.addPotionEffect(PotionEffectType.FIRE_RESISTANCE.createEffect(20 * 30, 1));
        return null;
    }
}
