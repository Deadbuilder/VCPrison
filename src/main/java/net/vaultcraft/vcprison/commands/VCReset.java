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

        if (args[0].equalsIgnoreCase("setpercent")) {
            if (args.length < 3) {
                Form.at(player, Prefix.ERROR, "Format: /reset setpercent [rank] [percent]");
                return;
            }

            Rank reset = Rank.fromName(args[1]);
            if (reset == null) {
                Form.at(player, Prefix.ERROR, "Cannot set percent of null mine!");
                return;
            }

            Mine from = MineLoader.fromRank(reset);
            if (from == null) {
                Form.at(player, Prefix.ERROR, "The given rank does not have a mine associated with it!");
                return;
            }

            double set = 0.0;
            try { set = Double.parseDouble(args[2]); } catch (NumberFormatException ex) {
                Form.at(player, Prefix.ERROR, args[2] + " is not a valid number!");
                return;
            }

            from.setPercent(set);
            Form.at(player, Prefix.SUCCESS, "Percent set to " + set + "!");
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
