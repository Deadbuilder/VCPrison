package net.vaultcraft.vcprison.user;

import net.vaultcraft.shade.mongodb.BasicDBObject;
import net.vaultcraft.shade.mongodb.DBObject;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
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

    public PrisonUser(final Player player) {
        this.user = User.fromPlayer(player);
        async_player_map.put(player, this);
        Bukkit.getScheduler().runTaskAsynchronously(VCPrison.getInstance(), new Runnable() {
            @Override
            public void run() {
                DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", player.getUniqueId().toString());
                if(dbObject != null) {
                    rank = dbObject.get("Rank") == 0.0 ? Rank.A : Rank.fromCost((Double) dbObject.get("Rank"));
                }
            }
        });
    }

    public Player getPlayer() {
        return user.getPlayer();
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
            final PrisonUser user = PrisonUser.fromPlayer(player);
            Bukkit.getScheduler().runTaskAsynchronously(VCPrison.getInstance(), new Runnable() {
                public void run() {
                    DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString()) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString());
                    dbObject.put("Rank", user.getRank().getCost());
                    DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString());
                    if(dbObject1 == null)
                        VCUtils.getInstance().getMongoDB().insert("VaultCraft", "PrisonUsers", dbObject);
                    else
                        VCUtils.getInstance().getMongoDB().update("VaultCraft", "PrisonUsers", dbObject1, dbObject);
                }
            });
            async_player_map.remove(player);
        }
    }

    public static void disable() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if (async_player_map.contains(player)) {
                final PrisonUser user = PrisonUser.fromPlayer(player);
                DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString()) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString());
                dbObject.put("Rank", user.getRank().getCost());
                VCUtils.getInstance().getMongoDB().insert("VaultCraft", "PrisonUsers", dbObject);
                DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString());
                if(dbObject1 == null)
                    VCUtils.getInstance().getMongoDB().insert("VaultCraft", "PrisonUsers", dbObject);
                else
                    VCUtils.getInstance().getMongoDB().update("VaultCraft", "PrisonUsers", dbObject1, dbObject);
                async_player_map.remove(player);
            }
        }
    }
}
