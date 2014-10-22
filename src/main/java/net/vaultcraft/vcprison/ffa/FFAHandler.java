package net.vaultcraft.vcprison.ffa;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Connor Hollasch on 9/28/14.
 */

public class FFAHandler {

    private static Location ffaSpawn;

    public static void setFFASpawn(Location loc) {
        ffaSpawn = loc;
    }

    private static FFAPlayer doubleBounty = null;

    public static void handleDeath(Player dead, Player killer) {
        FFAPlayer ffaDead = FFAPlayer.getFFAPlayerFromPlayer(dead);
        FFAPlayer ffaKill = FFAPlayer.getFFAPlayerFromPlayer(killer);

        if (doubleBounty == null || ffaKill.sessionKills >= doubleBounty.sessionKills) {
            doubleBounty = ffaKill;
        }

        double bounty = ffaDead.bounty*=(ffaDead.equals(doubleBounty) ? 2 : 1);

        switch (ffaKill.getUser().getGroup().getHighest()) {
            case ENDERDRAGON:
                bounty*=4.0;
                break;
            case WITHER:
                bounty*=3.0;
                break;
            case ENDERMAN:
                bounty*=2.3;
                break;
            case SKELETON:
                bounty*=1.8;
                break;
            case SLIME:
                bounty*=1.5;
                break;
            case WOLF:
                bounty*=1.2;
        }

        ffaKill.getUser().setMoney(ffaKill.getUser().getMoney() + bounty);

        ffaDead.sessionDeaths++;
        ffaDead.currentKillstreak = 0;
        announceFFA("&5"+dead.getName()+" &7was slain by &5"+killer.getName()+"&7!");

        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&e&m=====================================================");
        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&6&lYou were killed by: &7"+ffaKill.getUser().getPlayer().getName());
        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&7"+ffaKill.getUser().getPlayer().getName()+" &6&lreceived &7$"+bounty+" &6&lfrom your death!");
        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&e&m=====================================================");

        Form.at(ffaKill.getUser().getPlayer(), Prefix.NOTHING, "&6&lYou received &7$"+bounty+" &6&lfrom &7"+ffaDead.getUser().getPlayer().getName()+"'s &6&ldeath!");
    }

    public static Location getFFASpawn() {
        return ffaSpawn;
    }

    public static void announceFFA(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (FFAPlayer.getFFAPlayerFromPlayer(player).isPlaying())
                Form.at(player, Prefix.NOTHING, "&e&lFF&6&lA&f: &7"+message);
        }
    }
}
