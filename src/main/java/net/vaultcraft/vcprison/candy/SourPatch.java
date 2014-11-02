package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class SourPatch implements Candy {


    private static ItemStack stack = ItemUtils.build(Material.MELON,
            ChatColor.GREEN.toString() + ChatColor.BOLD + "Sour" + ChatColor.GOLD.toString() + ChatColor.BOLD + "Patch",
            "Con");

    @Override
    public ShapedRecipe getRecipe() {
        return null;
    }

    @Override
    public ItemStack getCandyItem() {
        return null;
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public int getHarmfulAfter() {
        return 0;
    }

    @Override
    public ItemStack onCandyConsume(Player player, boolean harmful) {
        return null;
    }
}
