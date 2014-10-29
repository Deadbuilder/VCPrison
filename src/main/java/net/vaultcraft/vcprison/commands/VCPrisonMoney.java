package net.vaultcraft.vcprison.commands;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by tacticalsk8er on 7/22/2014.
 */
public class VCPrisonMoney extends ICommand {

    public VCPrisonMoney(String name, Group permission, String... aliases) {
        super(name, permission, aliases);

        this.display_group = true;
        subCmds.put("pay <user> <amount>", "Pay a user the given amount"); groupPerms.put("pay <user> <amount", Group.COMMON);
        subCmds.put("add <user> <amount>", "Add the given amount to a users balance"); groupPerms.put("add <user> <amount>", Group.ADMIN);
        subCmds.put("set <user> <amount>", "Set a users balance to the given amount"); groupPerms.put("set <user> <amount>", Group.ADMIN);
        subCmds.put("<user>", "Check the given users balance"); groupPerms.put("<user>", Group.COMMON);
    }

    private static List<Player> cooldown = Lists.newArrayList();

    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.NOTHING, "&a&lMoney&f: $" + Form.at(User.fromPlayer(player).getMoney(), true));
            return;
        }

        if(args.length >= 1) {
            switch (args[0]) {
                case "help":
                    Form.atHelp(player, this);
                    break;
                case "pay":
                    if (cooldown.contains(player)) {
                        Form.at(player, Prefix.ERROR, "Your last payment is still being processed!");
                        return;
                    }

                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /money pay <player> <amount>");
                        return;
                    }

                    Player player1 = Bukkit.getPlayer(args[1]);
                    double amount;
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    Rank yours = PrisonUser.fromPlayer(player).getRank();
                    Rank his = PrisonUser.fromPlayer(player1).getRank();

                    if (yours.higherThan(his)) {
                        if (amount > (his.getCost()/1000)) {
                            Form.at(player, Prefix.ERROR, "You cannot send more than &e" + (his.getCost()/1000) + Prefix.ERROR.getChatColor() + " to this player!");
                            return;
                        }
                    }

                    if(amount <= 0) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a positive integer.");
                        return;
                    }

                    if (player1 == null) {
                        Form.at(player, Prefix.ERROR, "No such player");
                        return;
                    }

                    if(User.fromPlayer(player).getMoney() < amount) {
                        Form.at(player, Prefix.ERROR, "You don't have enough money.");
                        return;
                    }

                    User.fromPlayer(player).addMoney(-amount);
                    User.fromPlayer(player1).addMoney(amount);
                    Form.at(player, Prefix.SUCCESS, "You sent " + player1.getName() + " $" + amount + ".");
                    Form.at(player1, Prefix.VAULT_CRAFT, player.getName() + " sent you $" + amount + ".");
                    cooldown.add(player);

                    Runnable run = () -> cooldown.remove(player);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), run, 20*10);

                    break;
                case "add":
                    if(!User.fromPlayer(player).getGroup().hasPermission(Group.ADMIN)) {
                        Form.at(player, Prefix.ERROR, "You do not have permission to use this command!");
                        return;
                    }

                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /money add <player> <amount>");
                        return;
                    }

                    player1 = Bukkit.getPlayer(args[1]);
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    if (player1 == null) {
                        Form.at(player, Prefix.ERROR, "No such player");
                        return;
                    }

                    if(User.fromPlayer(player1).getMoney() < -amount) {
                        amount = -User.fromPlayer(player1).getMoney();
                    }

                    User.fromPlayer(player1).addMoney(amount);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " now has $" + User.fromPlayer(player1).getMoney() + ".");
                    break;
                case "set":
                    if(!User.fromPlayer(player).getGroup().hasPermission(Group.ADMIN)) {
                        Form.at(player, Prefix.ERROR, "You do not have permission to use this command!");
                        return;
                    }

                    if(args.length < 3) {
                        Form.at(player, Prefix.ERROR, "Format: /money set <player> <amount>");
                        return;
                    }

                    player1 = Bukkit.getPlayer(args[1]);
                    try {
                        amount = Double.parseDouble(args[2]);
                    } catch (NumberFormatException e) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a integer.");
                        return;
                    }

                    if (player1 == null) {
                        Form.at(player, Prefix.ERROR, "No such player");
                        return;
                    }

                    if(amount <= 0) {
                        Form.at(player, Prefix.ERROR, "Argument 3 needs to be a positive integer.");
                        return;
                    }

                    User.fromPlayer(player1).setMoney(amount);
                    Form.at(player, Prefix.SUCCESS, player1.getName() + " now has $" + User.fromPlayer(player1).getMoney() + ".");
                    break;
                default:
                    player1 = Bukkit.getPlayer(args[0]);
                    if (player1 == null) {
                        Form.at(player, Prefix.ERROR, "No such player");
                        return;
                    }
                    Form.at(player, Prefix.VAULT_CRAFT,  player1.getName() + " has &a$" + User.fromPlayer(player1).getMoney() + ".");
                    break;
            }
        }
    }
}
