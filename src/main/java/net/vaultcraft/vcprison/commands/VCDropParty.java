package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.event.DropParty;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/14/14
 */
public class VCDropParty extends ICommand {

    public VCDropParty(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        DropParty dp = DropParty.getInstance();

        if (args.length == 0) {
            int left = DropParty.getInstance().getTimeLeft();

            Form.at(player, Prefix.VAULT_CRAFT, "The drop party will begin in " + MMSS(left) + "!");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "force":
            case "forcestart":
            case "begin": {
                if(!User.fromPlayer(player).getGroup().hasPermission(Group.DEVELOPER))
                    return;
                DropParty.getInstance().setTimeLeft(5);
                Form.at(player, Prefix.WARNING, "Drop party will begin in 5 seconds!");
                break;
            }
        }
    }

    public static String MMSS(int in) {
        if (in >= 60) {
            int mins = (int)(in/60);
            int left = in % 60;

            return mins + " min, " + left + " sec";
        }

        return in + " seconds";
    }
}
