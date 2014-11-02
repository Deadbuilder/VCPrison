package net.vaultcraft.vcprison.candy;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public abstract class Candy {

    public abstract ShapedRecipe getRecipe();

    public abstract ItemStack getCandyItem();

    public ItemStack onCandyConsume(Player player) {
        return null;
    }
}
