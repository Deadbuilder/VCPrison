package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.events.TimeUnit;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import net.vaultcraft.vcutils.util.DateUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

/**
 * Created by CraftFest on 10/17/2014.
 */
public class VCFix extends ICommand {

    public VCFix(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] args) {
        if (User.fromPlayer(player).hasUserdata("fixCooldown")) {
            long x = Long.parseLong(User.fromPlayer(player).getUserdata("fixCooldown"));
            if (x < System.currentTimeMillis())
                User.fromPlayer(player).removeUserdata("fixCooldown");
            else {
                Form.at(player, Prefix.ERROR, "You cannot use this command for another " + DateUtil.fromTime(TimeUnit.SECONDS, (double)((x-System.currentTimeMillis())/1000)));
                return;
            }
        }

        User.fromPlayer(player).addUserdata("fixCooldown", String.valueOf(System.currentTimeMillis() + (1000 * 60 * getMultiplier(User.fromPlayer(player).getGroup().getHighest()))));

        repairItems(player.getInventory().getContents());

        repairItems(player.getInventory().getArmorContents());

        Form.at(player, Prefix.SUCCESS, "You repaired all of your items!");
    }

    private int getMultiplier(Group group) {
        if (Group.hasPermission(group, Group.ENDERDRAGON))
            return (60);
        if (Group.hasPermission(group, Group.WITHER))
            return (120);
        if (Group.hasPermission(group, Group.ENDERMAN))
            return (180);
        if (Group.hasPermission(group, Group.SKELETON))
            return (240);
        if (Group.hasPermission(group, Group.SLIME))
            return (300);
        if (group.hasPermission(group, Group.WOLF))
            return (360);

        return 600;
    }

    private void repairItems(ItemStack[] stacks) {
        for (ItemStack i : stacks) {
            if (i == null)
                continue;

            if (i.getType().isBlock() || i.getType().getMaxDurability() < 1)
                continue;

            i.setDurability((short)0);
        }
    }

    private void repairItems(PlayerInventory inventory) {
        for (ItemStack i : inventory) {
            if (i == null)
                continue;

            if (i.getType().isBlock() || i.getType().getMaxDurability() < 1)
                continue;

            i.setDurability((short)0);
        }
    }
}
