package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class CandyItems {

    public static final ItemStack WRAPPER = ItemUtils.build(Material.QUARTZ, ChatColor.translateAlternateColorCodes('&', "&7&lCandy Wrapper"), "You'll need something to contain sticky candies!");
    public static final ItemStack BUTTER = ItemUtils.build(Material.INK_SACK, (byte) 11, ChatColor.translateAlternateColorCodes('&', "&e&lButter"), "Used to create some types of candies");
    public static final ItemStack COCOA = ItemUtils.build(Material.BRICK, ChatColor.GOLD.toString() + ChatColor.BOLD + "Co-Coa", "Used to make some types of ");
    public static final ItemStack JAWBREAKER = ItemUtils.build(Material.SNOW_BALL, ChatColor.translateAlternateColorCodes('&', "&lJawbreaker"), "Ow! Jawbreakers can do some serious damage to your teeth!");

}
