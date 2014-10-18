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

import java.util.HashMap;

/**
 * Created by CraftFest on 10/17/2014.
 */
public class VCFix extends ICommand {

    public VCFix(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    private HashMap<Player, Long> cooldown = new HashMap<>();

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
        switch (group) {
            case WOLF:
                return (6 * 60);
            case SLIME:
                return (5 * 60);
            case SKELETON:
                return (4 * 60);
            case ENDERMAN:
                return (3 * 60);
            case WITHER:
                return (2 * 60);
            case ENDERDRAGON:
                return (60);
            default:
                return 1;
        }
    }

    private void repairItems(ItemStack[] stacks) {
        for (ItemStack i : stacks) {
            if (i == null)
                return;

            if (i.getType().isBlock() || i.getType().getMaxDurability() < 1)
                return;

            i.setDurability((short)0);
        }
    }
}
