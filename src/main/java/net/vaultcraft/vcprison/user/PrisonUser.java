package net.vaultcraft.vcprison.user;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tacticalsk8er on 7/31/2014.
 */
public class PrisonUser {

    private static volatile ConcurrentHashMap<Player, PrisonUser> async_player_map = new ConcurrentHashMap<>();

    public static PrisonUser fromPlayer(Player player) {
        return async_player_map.get(player);
    }

    private User user;
    private Rank rank = Rank.A;

    public PrisonUser(Player player) {
        this.user = User.fromPlayer(player);
        async_player_map.put(player, this);
        Bukkit.getScheduler().runTaskAsynchronously(VCPrison.getInstance(), new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public User getUser() {
        return user;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public static void remove(Player player) {
        if(async_player_map.contains(player)) {
            async_player_map.remove(player);
            Bukkit.getScheduler().runTaskAsynchronously(VCPrison.getInstance(), new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    public static void disable(Player player) {
        if(async_player_map.contains(player)) {
            async_player_map.remove(player);
        }
    }
}
