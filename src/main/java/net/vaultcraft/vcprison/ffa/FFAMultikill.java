package net.vaultcraft.vcprison.ffa;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 10/23/14
 */
public class FFAMultikill {

    private static HashMap<Player, Integer> killstreak = new HashMap<>();
    private static HashMap<Player, Long> multiKillTimer = new HashMap<>();

    private static HashMap<Integer, String> killMessages = new HashMap<>();

    static {
        killMessages.put(2, "&9&lDouble Kill");
        killMessages.put(3, "&a&lTriple Kill");
        killMessages.put(4, "&e&lMega Kill");
        killMessages.put(5, "&6&lSUPER KILL");
        killMessages.put(6, "&c&lW-T-F");
        killMessages.put(7, "&4&lUnbelievable");
    }

    public static void handleDeath(Player dead, Player killer) {
        if (killstreak.containsKey(dead))
            killstreak.remove(dead);

        int kills = 0;
        if (killstreak.containsKey(killer))
            kills = killstreak.remove(killer);

        killstreak.put(killer, kills+1);
        kills++;

        if (multiKillTimer.containsKey(killer)) {
            long at = multiKillTimer.get(killer);
            if (at < System.currentTimeMillis()) {
                multiKillTimer.remove(killer);
                killstreak.remove(killer);
                return;
            }
        }

        if (kills > 1) {
            multiKillTimer.put(killer, System.currentTimeMillis() + (5000));

            if (kills >= 7) {
                FFAHandler.announceFFA(killer.getName()+" &f&l>> &r" + killMessages.get(7) + "&f&l!");
            }
            else
                FFAHandler.announceFFA(killer.getName()+" &f&l>> &r" + killMessages.get(kills) + "&f&l!");

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (FFAPlayer.getFFAPlayerFromPlayer(player).isPlaying())
                    player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 1, 1);
            }
        }
    }
}
