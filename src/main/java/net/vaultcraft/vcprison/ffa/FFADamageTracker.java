package net.vaultcraft.vcprison.ffa;

import net.vaultcraft.vcprison.ffa.combatlog.CombatLog;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Connor Hollasch on 9/29/14.
 */

public class FFADamageTracker {

    //Maybe handle kill steals?
    //<Player damaged, Player damager>
    private static HashMap<Player, Player> lastDamage = new HashMap<>();

    public static void setLastDamager(Player hurt, Player damager) {
        if (lastDamage.containsKey(hurt))
            lastDamage.remove(hurt);

        lastDamage.put(hurt, damager);
        CombatLog.wasTagged(hurt);
    }

    public static Player getLastDamager(Player player) {
        return lastDamage.get(player);
    }

    public static void reset(Player player) {
        lastDamage.remove(player);
    }
}
