package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by tacticalsk8er on 8/2/2014.
 */
public class VCPrestige extends ICommand {
    public VCPrestige(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        PrisonUser user = PrisonUser.fromPlayer(player);
        if(user.getPrestige() == 10) {
            Form.at(player, Prefix.ERROR, "You are at the max prestige level!");
            return;
        }
        if(user.getRank() != Rank.FREE) {
            Form.at(player, Prefix.ERROR, "You need to be rank Free in order to use this command!");
            return;
        }
        if(args.length == 0) {
            Form.at(player, Prefix.WARNING, "Prestiging will bring you down to rank A, wipe your money, " + ChatColor.RED.toString() + ChatColor.BOLD + "wipe your pickaxe," + ChatColor.RESET + " and teleport you to mine A. If you are sure you want to prestige type /prestige confirm.");
            return;
        }

        if(args.length > 0) {
            if(args[0].equalsIgnoreCase("confirm")) {
                user.setRank(Rank.A);
                user.setPrestige(user.getPrestige() + 1);
                user.getUser().setMoney(0);
                user.getPickaxe().reset();
                Form.at(player, Prefix.SUCCESS, "You have Prestiged!");
            }
        }
    }
}
