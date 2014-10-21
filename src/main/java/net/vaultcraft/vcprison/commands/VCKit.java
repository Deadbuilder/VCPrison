package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by CraftFest on 10/20/2014.
 */
public class VCKit extends ICommand {

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
            if(args[1].equalsIgnoreCase("common")) {
                Form.at(player, Prefix.SUCCESS, "You spawned the kit: " + ChatColor.LIGHT_PURPLE + "Common");
            } else if(args[1].equalsIgnoreCase("wolf")) {
                Form.at(player, Prefix.SUCCESS, "You spawned the kit: " + ChatColor.LIGHT_PURPLE + "Wolf");
            } else if(args[1].equalsIgnoreCase("slime")) {
                Form.at(player, Prefix.SUCCESS, "You spawned the kit: " + ChatColor.LIGHT_PURPLE + "Slime");
            } else if(args[1].equalsIgnoreCase("skeleton")) {
                Form.at(player, Prefix.SUCCESS, "You spawned the kit: " + ChatColor.LIGHT_PURPLE + "Skeleton");
            } else if(args[1].equalsIgnoreCase("enderman")) {
                Form.at(player, Prefix.SUCCESS, "You spawned the kit: " + ChatColor.LIGHT_PURPLE + "Enderman");
            } else if(args[1].equalsIgnoreCase("wither")) {
                Form.at(player, Prefix.SUCCESS, "You spawned the kit: " + ChatColor.LIGHT_PURPLE + "Wither");
            } else if(args[1].equalsIgnoreCase("enderdragon")) {
                Form.at(player, Prefix.SUCCESS, "You spawned the kit: " + ChatColor.LIGHT_PURPLE + "EnderDragon");
            } else {
                Form.at(player, Prefix.ERROR, "That kit does not exist.");
            }
        } else if(args.length > 1) {
            Form.at(player, Prefix.ERROR, "Too many arguments.");
        }
    }
}
