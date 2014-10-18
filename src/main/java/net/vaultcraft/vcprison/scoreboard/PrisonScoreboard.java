package net.vaultcraft.vcprison.scoreboard;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class PrisonScoreboard {

    private static HashMap<Player, ScoreboardHandle> boards = new HashMap<>();

    public static void init() {
        Runnable ticker = () -> {
            List<Player> remove = Lists.newArrayList();
            for (Player key : boards.keySet()) {
                if (!(key.isOnline()) || User.fromPlayer(key) == null)
                    remove.add(key);
                else {
                    ScoreboardHandle value = boards.get(key);
                    value.run();
                }
            }

            for (Player player : remove)
                boards.remove(player);
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VCPrison.getInstance(), ticker, 5*20, 5*20);
    }

    public static void addPlayer(Player player) {
        boards.put(player, new ScoreboardHandle(player));
    }

    public static void removePlayer(Player player) {
        if (boards.containsKey(player))
            boards.remove(player);
    }
}
