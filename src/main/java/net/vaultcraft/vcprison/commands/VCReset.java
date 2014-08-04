package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 8/3/14. Designed for the VCPrison project.
 */

public class VCReset extends ICommand {

    public VCReset(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Please specify a mine to reset");
            return;
        }

        Rank reset = Rank.fromName(args[0]);
        if (reset == null) {
            Form.at(player, Prefix.ERROR, "No such mine to reset!");
            return;
        }

        Mine from = MineLoader.fromRank(reset);
        if (from == null) {
            Form.at(player, Prefix.ERROR, "The given rank does not have a mine associated with it!");
            return;
        }

        MineLoader.resetMine(from);
        Form.at(player, Prefix.SUCCESS, "Mine: &e"+reset.toString()+Prefix.SUCCESS.getChatColor()+" reset!");
    }
}
