package net.vaultcraft.vcprison.pickaxe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by tacticalsk8er on 7/31/2014.
 */
public class Pickaxe {

    ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

    public Pickaxe() {
        pickaxe.addEnchantment(Enchantment.DIG_SPEED, 10);
        pickaxe.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, 10);
        ItemMeta itemMeta = pickaxe.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "V" + ChatColor.DARK_GRAY + "C " + ChatColor.WHITE + "Pickaxe");
    }
}
