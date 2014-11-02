package net.vaultcraft.vcprison.candy;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public interface Candy extends Listener {

    public ShapedRecipe getRecipe();

    public ItemStack getCandyItem();

    public ItemStack onCandyConsume(Player player);
}
