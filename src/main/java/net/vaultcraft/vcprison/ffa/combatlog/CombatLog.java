package net.vaultcraft.vcprison.ffa.combatlog;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 10/21/14
 */
public class CombatLog {

    public static HashMap<Player, Long> inPvP = new HashMap<>();

    public static void wasTagged(Player player) {
        if (inPvP.containsKey(player)) {
            inPvP.remove(player);
        }
        else {
            Form.at(player, Prefix.WARNING, "You were tagged, you may not logout for another 10 seconds!");
        }

        inPvP.put(player, System.currentTimeMillis() + (1000 * 10));
    }

    public static void untag(Player player) {
        if(inPvP.containsKey(player))
            inPvP.remove(player);
    }

    public static boolean isSafeLogout(Player player) {
        if (inPvP.containsKey(player)) {
            long at = inPvP.get(player);
            if (at <= System.currentTimeMillis())
                inPvP.remove(player);
        }

        return !inPvP.containsKey(player);
    }

    public static void handleLeave(Player player) {
        if (isSafeLogout(player))
            return;

        player.getInventory().setItem(0, null);
        player.damage(player.getMaxHealth()*100);
    }
}
