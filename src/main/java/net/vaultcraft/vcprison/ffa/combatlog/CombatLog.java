package net.vaultcraft.vcprison.ffa.combatlog;

import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 10/21/14
 */
public class CombatLog {

    public static HashMap<Player, Long> inPvP = new HashMap<>();
    public static HashMap<Player, Villager> logged = new HashMap<>();

    public static void wasTagged(Player player) {
        if (inPvP.containsKey(player)) {
            long x = inPvP.get(player);
            x+=(1000 * 10);
            inPvP.put(player, x);
        }
        else
            inPvP.put(player, System.currentTimeMillis() + (1000 * 10));
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
