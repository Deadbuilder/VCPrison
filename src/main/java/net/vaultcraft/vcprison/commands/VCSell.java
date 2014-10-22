package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.worth.Warden;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

public class VCSell extends ICommand {
    public VCSell(String name, Group permission) {
        super(name, permission);
    }

    @Override
    public void processCommand(Player player, String[] strings) {
        Warden.sell(player);
    }
}
