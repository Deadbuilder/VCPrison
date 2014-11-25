package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcprison.ffa.combatlog.FFAWarmup;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/21/14
 */
public class VCSpawn extends ICommand {

    public VCSpawn(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] strings) {
        if (FFAPlayer.getFFAPlayerFromPlayer(player).isPlaying()) {
            player.performCommand("redir ffa");
            return;
        }

        FFAWarmup.addToWarmup(player);
        player.teleport(VCPrison.spawn);
        Form.at(player, Prefix.SUCCESS, "You teleported to spawn!");
    }
}
