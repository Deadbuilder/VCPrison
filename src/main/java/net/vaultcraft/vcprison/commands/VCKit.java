package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Available kits" + ChatColor.GRAY + ":");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GRAY + "Common");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.DARK_GRAY + "Wolf");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.GREEN + "Slime");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.WHITE + "Skeleton");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.DARK_PURPLE + "Enderman");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.YELLOW + "Wither");
            player.sendMessage(ChatColor.GRAY + "- " + ChatColor.DARK_PURPLE + "Ender" + ChatColor.GRAY + "Dragon");
            player.sendMessage(ChatColor.GRAY + "/kit <name>");
        } else if(args.length == 1) {
            Form.at(player, Prefix.ERROR, "Kits are not yet released. Coming soon!");
        } else if(args.length > 1) {
            Form.at(player, Prefix.ERROR, "Too many arguments.");
        }
    }
}
