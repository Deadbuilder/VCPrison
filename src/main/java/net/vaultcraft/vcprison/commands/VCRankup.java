package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/31/14. Designed for the VCPrison project.
 */

public class VCRankup extends ICommand {

    public VCRankup(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] strings) {
        PrisonUser user = PrisonUser.fromPlayer(player);

        if (user.getRank() == Rank.FREE) {
            Form.at(player, Prefix.WARNING, "You cannot rankup anymore!");
            return;
        }

        double balance = user.getUser().getMoney();
        Rank next = Rank.next(user.getRank());
    }
}
