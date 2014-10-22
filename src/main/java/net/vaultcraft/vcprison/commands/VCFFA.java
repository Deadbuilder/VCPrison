package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/21/14
 */
public class VCFFA extends ICommand {

    public VCFFA(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] strings) {
        FFAPlayer ffa = FFAPlayer.getFFAPlayerFromPlayer(player);
    }
}
