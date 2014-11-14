package net.vaultcraft.vcprison.candy;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;

/**
 * @author Nicholas Peterson
 */
public interface CandyRecipe {

    public ItemStack getResult();

    public Recipe getRecipe();

    public boolean isRecipe(ItemStack[] recipe);
}
