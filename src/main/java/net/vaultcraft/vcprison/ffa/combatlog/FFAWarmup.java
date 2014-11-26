package net.vaultcraft.vcprison.ffa.combatlog;

import org.bukkit.entity.Player;

import java.util.HashSet;

/**
 * @author Connor Hollasch
 * @since 11/24/2014
 */
public class FFAWarmup {

    private static HashSet<String> in_warmup = new HashSet<>();

    public static void addToWarmup(Player player) {
        in_warmup.add(player.getName());
    }

    public static void removeFromWarmup(Player player) {
        in_warmup.remove(player.getName());
    }

    public static boolean isInWarmup(Player player) {
        return in_warmup.contains(player.getName());
    }
}
