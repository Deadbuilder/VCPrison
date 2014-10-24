package net.vaultcraft.vcprison.ffa.combatlog;

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
