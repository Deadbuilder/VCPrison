package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.mine.warp.WarpGUI;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 8/4/14. Designed for the VCPrison project.
 */

public class VCWarp extends ICommand {

    public VCWarp(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        PrisonUser user = PrisonUser.fromPlayer(player);

        player.openInventory(WarpGUI.create(user));
        Form.at(player, Prefix.SUCCESS, "Warp GUI opened!");
    }
}
