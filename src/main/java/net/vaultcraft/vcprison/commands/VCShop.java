package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/21/14
 */
public class VCShop extends ICommand {
    public VCShop(String name, Group permission, String... aliases) {
        super(name, permission, aliases);

        loc =  new Location(Bukkit.getServer().getWorld("world"), -3890.5, 81, 51.5, -314.7f, 2.5f);
    }

    public static Location loc;

    public void processCommand(Player player, String[] strings) {
        if (FFAPlayer.getFFAPlayerFromPlayer(player).isPlaying()) {
            Form.at(player, Prefix.ERROR, "You cannot go to spawn during battle!");
            return;
        }

        player.teleport(loc);
        Form.at(player, Prefix.SUCCESS, "You teleported to the shop!");
    }
}
