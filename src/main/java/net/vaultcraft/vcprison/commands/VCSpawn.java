package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by CraftFest on 10/13/2014.
 */
public class VCSpawn extends ICommand {

    public VCSpawn(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        Location location = new Location(Bukkit.getServer().getWorld("world"), -3839.5, 86, 0.5);
        player.teleport(location);
    }
}
