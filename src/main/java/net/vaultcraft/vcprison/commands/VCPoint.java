package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.event.DropParty;
import net.vaultcraft.vcprison.pickaxe.Pickaxe;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by tacticalsk8er on 10/19/2014.
 */
public class VCPoint extends ICommand {

    public VCPoint(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            Form.at(player, Prefix.ERROR, "Format /point <amount> <player>");
            return;
        }

        if(args.length > 0) {
            if (args[0].equalsIgnoreCase("dp")) {
                if (args.length > 1) {
                    Player find = Bukkit.getPlayer(args[1]);
                    if (find == null) {
                        Form.at(player, Prefix.ERROR, "Cannot find player &e" + find.getName() + Prefix.ERROR.getChatColor() + "!");
                        return;
                    }

                    find.getInventory().addItem(DropParty.getDpToken().clone());
                    Form.at(player, Prefix.SUCCESS, "You gave &e" + find.getName() + Prefix.SUCCESS.getChatColor() + " a drop party token!");
                    return;
                }

                player.getInventory().addItem(DropParty.getDpToken().clone());
                Form.at(player, Prefix.SUCCESS, "You gave yourself a drop party token!");
                return;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                Form.at(player, Prefix.ERROR, "The first argument needs to be a integer.");
                return;
            }
            ItemStack perkPoint = Pickaxe.getAddPointItem();
            perkPoint.setAmount(amount);
            if(args.length > 1) {
                if(args[1].equals("'ALL'")) {
                    for(Player player1 : Bukkit.getOnlinePlayers()) {
                        player1.getInventory().addItem(perkPoint);
                    }
                    return;
                }
                Player player1 = Bukkit.getPlayer(args[1]);
                if(player1 == null) {
                    Form.at(player, Prefix.ERROR, "That player is not online.");
                    return;
                }
                player1.getInventory().addItem(perkPoint);
            } else {
                player.getInventory().addItem(perkPoint);
            }
        }
    }
}
