package net.vaultcraft.vcprison.user;

import net.vaultcraft.shade.mongodb.BasicDBObject;
import net.vaultcraft.shade.mongodb.DBObject;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcprison.pickaxe.Pickaxe;
import net.vaultcraft.vcprison.pickaxe.PickaxePerk;
import net.vaultcraft.vcprison.sword.Sword;
import net.vaultcraft.vcprison.sword.SwordPerk;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.user.Group;
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
    private int prestige;
    private Pickaxe pickaxe = null;
    private Sword sword = null;

    public PrisonUser(final Player player) {
        this.user = User.fromPlayer(player);
        async_player_map.put(player, this);
        Bukkit.getScheduler().runTaskAsynchronously(VCPrison.getInstance(), new Runnable() {
            public void run() {
                DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", player.getUniqueId().toString());
                if (dbObject != null) {
                    rank = dbObject.get("Rank") == null ? Rank.A : Rank.fromName((String) dbObject.get("Rank"));
                    prestige = dbObject.get("Prestige") == null ? 0 : (int) dbObject.get("Prestige");
                }
            }
        });
        new FFAPlayer(player);
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

    public int getPrestige() {
        return prestige;
    }

    //① ② ③ ④ ⑤ ⑥ ⑦ ⑧ ⑨ ⑩
    public String getPrestigePrefix() {
        switch (prestige) {
            case 1:
                return "&f/&6①&f/ ";
            case 2:
                return "&f/&6②&f/ ";
            case 3:
                return "&f/&6③&f/ ";
            case 4:
                return "&f/&6④&f/ ";
            case 5:
                return "&f/&6⑤&f/ ";
            case 6:
                return "&f/&6⑥&f/ ";
            case 7:
                return "&f/&6⑦&f/ ";
            case 8:
                return "&f/&6⑧&f/ ";
            case 9:
                return "&f/&6⑨&f/ ";
            case 10:
                return "&f/&6⑩&f/ ";
            default:
                return "";
        }
    }

    public int getPlotLimit() {
        Group.GroupHandler group = user.getGroup();
        if(group.hasPermission(Group.ENDERMAN))
            return 15;
        if(group.hasPermission(Group.WITHER))
            return 10;
        if(group.hasPermission(Group.ENDERMAN))
            return 5;
        if(group.hasPermission(Group.SKELETON))
            return 4;
        if(group.hasPermission(Group.SLIME))
            return 3;
        if(group.hasPermission(Group.WOLF))
            return 2;
        return 1;
    }

    public void setPrestige(int prestige) {
        this.prestige = prestige;
    }

    public Pickaxe getPickaxe() {
        return pickaxe;
    }

    public void setPickaxe(Pickaxe pickaxe) {
        this.pickaxe = pickaxe;
    }

    public Sword getSword() {
        return sword;
    }

    public void setSword(Sword sword) {
        this.sword = sword;
    }

    public static void remove(final Player player) {
        final PrisonUser user = PrisonUser.fromPlayer(player);
        for(PickaxePerk perk : PickaxePerk.getPerks()) {
            perk.onEnd(player);
        }
        for(SwordPerk perk : SwordPerk.getPerks()) {
            perk.onEnd(player);
        }
        user.getUser().addUserdata("Pickaxe", user.getPickaxe().toString());
        Bukkit.getScheduler().runTaskAsynchronously(VCPrison.getInstance(), new Runnable() {
            public void run() {
                DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString()) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString());
                dbObject.put("UUID", user.getPlayer().getUniqueId().toString());
                dbObject.put("Rank", user.getRank().toString());
                dbObject.put("Prestige", user.getPrestige());
                DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString());
                if (dbObject1 == null)
                    VCUtils.getInstance().getMongoDB().insert("VaultCraft", "PrisonUsers", dbObject);
                else
                    VCUtils.getInstance().getMongoDB().update("VaultCraft", "PrisonUsers", dbObject1, dbObject);
            }
        });
        player.getInventory().setItem(0, null);
        async_player_map.remove(player);
        FFAPlayer.removePlayer(player);
    }

    public static void disable() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            final PrisonUser user = PrisonUser.fromPlayer(player);
            for(PickaxePerk perk : PickaxePerk.getPerks()) {
                perk.onEnd(player);
            }
            for(SwordPerk perk : SwordPerk.getPerks()) {
                perk.onEnd(player);
            }
            user.getUser().addUserdata("Pickaxe", user.getPickaxe().toString());
            DBObject dbObject = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString()) == null ? new BasicDBObject() : VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString());
            dbObject.put("UUID", user.getPlayer().getUniqueId().toString());
            dbObject.put("Rank", user.getRank().toString());
            dbObject.put("Prestige", user.getPrestige());
            DBObject dbObject1 = VCUtils.getInstance().getMongoDB().query("VaultCraft", "PrisonUsers", "UUID", user.getPlayer().getUniqueId().toString());
            if (dbObject1 == null)
                VCUtils.getInstance().getMongoDB().insert("VaultCraft", "PrisonUsers", dbObject);
            else
                VCUtils.getInstance().getMongoDB().update("VaultCraft", "PrisonUsers", dbObject1, dbObject);
            player.getInventory().setItem(0, null);
            async_player_map.remove(player);
        }
    }
}
