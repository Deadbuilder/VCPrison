package net.vaultcraft.vcprison.plots;

import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class PlotCommands extends ICommand {


    public PlotCommands(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] strings) {

    }
}
