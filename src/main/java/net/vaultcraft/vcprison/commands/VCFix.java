package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;
import net.vaultcraft.vcutils.user.Group;

/**
 * Created by CraftFest on 10/17/2014.
 */
public class VCFix extends ICommand {

    public VCFix(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        repair(player);
    }

    public void repair(Player player) {
        for(int i = 0; i <= 36; i++)
        {
            try
            {
                    player.getInventory().getItem(i).setDurability((short) 0);
                Form.at(player, Prefix.SUCCESS, "Fixed all items!");
            }
            catch(Exception e)
            {

            }
        }
    }
}
