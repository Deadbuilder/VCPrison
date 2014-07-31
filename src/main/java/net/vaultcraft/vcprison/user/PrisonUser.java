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

    private Player player;
    private User user;
    private Rank rank;

    public PrisonUser(Player player) {
        this.player = player;
        async_player_map.put(player, this);
        this.user = User.fromPlayer(player);
        Bukkit.getScheduler().runTaskAsynchronously(VCPrison.getInstance(), new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public Player getPlayer() {
        return player;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public User getUser() {
        return user;
    }

    public void remove(Player player) {
        if(async_player_map.contains(player)) {
            async_player_map.remove(player);
            Bukkit.getScheduler().runTaskAsynchronously(VCPrison.getInstance(), new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    public void disable(Player player) {
        if(async_player_map.contains(player)) {
            async_player_map.remove(player);
        }
    }
}
