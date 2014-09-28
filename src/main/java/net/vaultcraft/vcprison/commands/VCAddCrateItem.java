package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.crate.CrateFile;
import net.vaultcraft.vcprison.crate.CrateItem;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Connor on 9/17/14. Designed for the VCPrison project.
 */

public class VCAddCrateItem extends ICommand {

    public VCAddCrateItem(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (player.getItemInHand() == null || player.getItemInHand().getType().equals(Material.AIR)) {
            Form.at(player, Prefix.ERROR, "You must be holding a valid item!");
            return;
        }

        if (args.length == 0) {
            Form.at(player, Prefix.ERROR, "Please specify a percentage");
            return;
        }

        double percent = 0.0;
        try {
            percent = Double.parseDouble(args[0]);
        } catch (NumberFormatException ex) {
            Form.at(player, Prefix.ERROR, args[0]+" is not a valid number!");
            return;
        }

        ItemStack hand = player.getItemInHand();
        CrateItem ci = new CrateItem(hand, percent);

        CrateFile.crateItems.add(ci);
        Form.at(player, Prefix.SUCCESS, "Crate item added with a &e"+percent+"%"+Prefix.SUCCESS.getChatColor()+" chance!");
    }
}
