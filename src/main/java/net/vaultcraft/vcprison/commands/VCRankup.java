package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.string.StringUtils;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by Connor on 7/31/14. Designed for the VCPrison project.
 */

public class VCRankup extends ICommand {

    public VCRankup(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] argz) {
        PrisonUser user = PrisonUser.fromPlayer(player);
        if (argz.length > 1 && user.getUser().getGroup().hasPermission(Group.ADMIN) && argz[0].equalsIgnoreCase("set")) {
            String[] args = StringUtils.buildFromArray(argz, 1).split(" ");
            Player usr = Bukkit.getPlayer(args[0]);
            if (usr == null) {
                Form.at(player, Prefix.ERROR, "No such player!");
                return;
            }

            Rank back = Rank.A;
            if (args.length >= 2) {
                back = Rank.fromName(args[1]);
            }

            PrisonUser.fromPlayer(usr).setRank(back);
            Form.at(player, Prefix.SUCCESS, "You set &e"+usr.getName()+Prefix.SUCCESS.getChatColor()+"'s rank back to &e"+back.toString()+Prefix.SUCCESS.getChatColor()+"!");
            Form.at(usr, Prefix.WARNING, "Your rank was set to &e"+back.toString()+Prefix.SUCCESS.getChatColor()+"!");
            return;
        }

        if (user.getRank() == Rank.FREE) {
            Form.at(player, Prefix.WARNING, "You cannot rankup anymore!");
            return;
        }

        double balance = user.getUser().getMoney();
        Rank next = Rank.next(user.getRank());

        if (balance >= next.getCost()) {
            //rankup
            user.setRank(next);
            user.getUser().setMoney(balance-next.getCost());

            Form.at(player, Prefix.SUCCESS, "You ranked up to &e"+next.toString()+Prefix.SUCCESS.getChatColor()+"!");
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl.equals(player))
                    continue;

                Form.at(pl, "&e"+player.getName()+" "+Prefix.VAULT_CRAFT.getChatColor()+"ranked up to &e"+next.toString()+Prefix.VAULT_CRAFT.getChatColor()+"!");
            }
        } else {
            double rankup = next.getCost() - balance;
            Form.at(player, Prefix.ERROR, "You do not have enough to rankup! &e$"+Form.at(rankup, true)+Prefix.ERROR.getChatColor()+" left!");
        }
    }
}
