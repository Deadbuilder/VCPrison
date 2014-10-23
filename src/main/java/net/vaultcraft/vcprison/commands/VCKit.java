package net.vaultcraft.vcprison.commands;

import com.google.common.collect.Lists;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.util.List;

/**
 * Created by CraftFest on 10/20/2014.
 */
public class VCKit extends ICommand {

    public static final ItemStack COMMON_HELMET = build(Material.IRON_HELMET, "&7&lCommon Helmet");
    public static final ItemStack COMMON_CHESTPLATE = build(Material.IRON_CHESTPLATE, "&7&lCommon Chestplate");
    public static final ItemStack COMMON_LEGGINGS = build(Material.IRON_LEGGINGS, "&7&lCommon Leggings");
    public static final ItemStack COMMON_BOOTS = build(Material.IRON_BOOTS, "&7&lCommon Boots");

    public static final ItemStack WOLF_HELMET = build(Material.IRON_HELMET, "&8&lWolf Helmet");
    public static final ItemStack WOLF_CHESTPLATE = build(Material.IRON_CHESTPLATE, "&8&lWolf Chestplate");
    public static final ItemStack WOLF_LEGGINGS = build(Material.IRON_LEGGINGS, "&8&lWolf Leggings");
    public static final ItemStack WOLF_BOOTS = build(Material.IRON_BOOTS, "&8&lWolf Boots");

    public static final ItemStack SLIME_HELMET = build(Material.IRON_HELMET, "&a&lSlime Helmet");
    public static final ItemStack SLIME_CHESTPLATE = build(Material.IRON_CHESTPLATE, "&a&lSlime Chestplate");
    public static final ItemStack SLIME_LEGGINGS = build(Material.IRON_LEGGINGS, "&a&lSlime Leggings");
    public static final ItemStack SLIME_BOOTS = build(Material.IRON_BOOTS, "&a&lSlime Boots");

    public static final ItemStack SKELETON_HELMET = build(Material.DIAMOND_HELMET, "&f&lSkeleton Helmet");
    public static final ItemStack SKELETON_CHESTPLATE = build(Material.DIAMOND_CHESTPLATE, "&f&lSkeleton Chestplate");
    public static final ItemStack SKELETON_LEGGINGS = build(Material.DIAMOND_LEGGINGS, "&f&lSkeleton Leggings");
    public static final ItemStack SKELETON_BOOTS = build(Material.DIAMOND_BOOTS, "&f&lSkeleton Boots");

    public static final ItemStack ENDERMAN_HELMET = build(Material.DIAMOND_HELMET, "&5&lEnderman Helmet");
    public static final ItemStack ENDERMAN_CHESTPLATE = build(Material.DIAMOND_CHESTPLATE, "&5&lEnderman Chestplate");
    public static final ItemStack ENDERMAN_LEGGINGS = build(Material.DIAMOND_LEGGINGS, "&5&lEnderman Leggings");
    public static final ItemStack ENDERMAN_BOOTS = build(Material.DIAMOND_BOOTS, "&5&lEnderman Boots");

    public static final ItemStack WITHER_HELMET = build(Material.DIAMOND_HELMET, "&f&lSkeleton Helmet");
    public static final ItemStack WITHER_CHESTPLATE = build(Material.DIAMOND_CHESTPLATE, "&f&lSkeleton Chestplate");
    public static final ItemStack WITHER_LEGGINGS = build(Material.DIAMOND_LEGGINGS, "&f&lSkeleton Leggings");
    public static final ItemStack WITHER_BOOTS = build(Material.DIAMOND_BOOTS, "&f&lSkeleton Boots");

    public static final ItemStack ENDERDRAGON_HELMET = build(Material.DIAMOND_HELMET, "&5&lEnder&7&lDragon Helmet");
    public static final ItemStack ENDERDRAGON_CHESTPLATE = build(Material.DIAMOND_CHESTPLATE, "&5&lEnder&7&lDragon Chestplate");
    public static final ItemStack ENDERDRAGON_LEGGINGS = build(Material.DIAMOND_LEGGINGS, "&5&lEnder&7&lDragon Leggings");
    public static final ItemStack ENDERDRAGON_BOOTS = build(Material.DIAMOND_BOOTS, "&5&lEnder&7&lDragon Boots");

    public VCKit(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            player.sendMessage(ChatColor.DARK_PURPLE + "Available kits" + ChatColor.GRAY + ":");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + "Common");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + "Wolf");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + "Slime");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + "Skeleton");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + "Enderman");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + "Wither");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.LIGHT_PURPLE + "EnderDragon");
            player.sendMessage(ChatColor.GRAY + "Usage: /kit <kit name>");
        } else if(args.length == 1) {
            if(args[0].equalsIgnoreCase("common")) {
                //player.getInventory().addItem(COMMON_HELMET);
                //player.getInventory().addItem(COMMON_CHESTPLATE);
                //player.getInventory().addItem(COMMON_LEGGINGS);
                //player.getInventory().addItem(COMMON_BOOTS);
                //player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SUCCESS" + ChatColor.WHITE + ":" + ChatColor.GRAY + "You spawned the Common kit.");
                Form.at(player, Prefix.ERROR, "Kits are not yet released.");
            } else if(args[0].equalsIgnoreCase("wolf")) {
                //player.getInventory().addItem(WOLF_HELMET);
                //player.getInventory().addItem(WOLF_CHESTPLATE);
                //player.getInventory().addItem(WOLF_LEGGINGS);
                //player.getInventory().addItem(WOLF_BOOTS);
                //player.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "SUCCESS" + ChatColor.WHITE + ":" + ChatColor.GRAY + "You spawned the " + ChatColor.DARK_GRAY +  "Wolf " + ChatColor.GRAY + "kit.");
                Form.at(player, Prefix.ERROR, "Kits are not yet released.");
            } else if(args[0].equalsIgnoreCase("slime")) {
                Form.at(player, Prefix.ERROR, "Kits are not yet released.");
            } else if(args[0].equalsIgnoreCase("skeleton")) {
                Form.at(player, Prefix.ERROR, "Kits are not yet released.");
            } else if(args[0].equalsIgnoreCase("enderman")) {
                Form.at(player, Prefix.ERROR, "Kits are not yet released.");
            } else if(args[0].equalsIgnoreCase("wither")) {
                Form.at(player, Prefix.ERROR, "Kits are not yet released.");
            } else if(args[0].equalsIgnoreCase("enderdragon")) {
                Form.at(player, Prefix.ERROR, "Kits are not yet released.");
            } else {
                Form.at(player, Prefix.ERROR, "That kit does not exist.");
            }
        } else if(args.length > 1) {
            Form.at(player, Prefix.ERROR, "Too many arguments.");
        }
    }

    public static ItemStack build(Material type, String displayName, String... lore) {
        return build(type, (byte)0, displayName, lore);
    }

    public static ItemStack build(Material type, byte data, String displayName, String... lore) {
        ItemStack stack = new ItemStack(type, 1, (short)1, data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (lore.length > 0) {
            List<String> l = Lists.newArrayList();
            for (String s : lore) {
                l.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            meta.setLore(l);
        }
        stack.setItemMeta(meta);
        return stack;
    }
}
