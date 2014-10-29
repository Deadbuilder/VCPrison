package net.vaultcraft.vcprison.ffa;

import net.vaultcraft.vcprison.ffa.combatlog.CombatLog;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.worth.ItemWorthLoader;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.Random;

/**
 * Created by Connor Hollasch on 9/28/14.
 */

public class FFAHandler {

    //TODO Un hard code
    private static final Point min = new Point(-45, -77);
    private static final Point max = new Point(69, 56);

    private static World world = Bukkit.getWorld("ffa");
    private static FFAPlayer doubleBounty = null;

    public static void handleDeath(Player dead, Player killer) {
        FFAPlayer ffaDead = FFAPlayer.getFFAPlayerFromPlayer(dead);
        ffaDead.sessionDeaths++;
        ffaDead.bounty = 1.0;
        if (killer == null) {
            announceFFA("&5"+dead.getName()+" &7died!");
            return;
        }

        FFAPlayer ffaKill = FFAPlayer.getFFAPlayerFromPlayer(killer);

        if (doubleBounty == null || ffaKill.sessionKills >= doubleBounty.sessionKills) {
            doubleBounty = ffaKill;
        }

        double bounty = ffaDead.bounty*=(ffaDead.equals(doubleBounty) ? 1.5 : 1.2);

        if (ffaKill.getUser().getGroup().hasPermission(Group.ENDERDRAGON))
            bounty*=4.0;
        else if (ffaKill.getUser().getGroup().hasPermission(Group.WITHER))
            bounty*=3.0;
        else if (ffaKill.getUser().getGroup().hasPermission(Group.ENDERMAN))
            bounty*=2.3;
        else if (ffaKill.getUser().getGroup().hasPermission(Group.SKELETON))
            bounty*=1.8;
        else if (ffaKill.getUser().getGroup().hasPermission(Group.SLIME))
            bounty*=1.5;
        else if (ffaKill.getUser().getGroup().hasPermission(Group.WOLF))
            bounty*=1.2;

        bounty*=(5 * ItemWorthLoader.getWorth(PrisonUser.fromPlayer(killer).getRank(), Material.DIAMOND_BLOCK));

        ffaKill.getUser().setMoney(ffaKill.getUser().getMoney() + bounty);

        ffaKill.sessionKills++;

        announceFFA("&5"+dead.getName()+" &7was slain by &5"+killer.getName()+"&7!");
        FFAMultikill.handleDeath(dead, killer);

        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&e&m=====================================================");
        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&6&lYou were killed by: &7"+ffaKill.getUser().getPlayer().getName());
        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&7"+ffaKill.getUser().getPlayer().getName()+" &6&lreceived &7$"+Form.at(bounty, false)+" &6&lfrom your death!");
        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&6&lTo join to back type /ffa.");
        Form.at(ffaDead.getUser().getPlayer(), Prefix.NOTHING, "&e&m=====================================================");

        Form.at(ffaKill.getUser().getPlayer(), Prefix.NOTHING, "&6&lYou received &7$"+Form.at(bounty, false)+" &6&lfrom &7"+ffaDead.getUser().getPlayer().getName()+"'s &6&ldeath!");
        CombatLog.untag(ffaDead.getUser().getPlayer());
    }

    public static Location getRandomSpawnLocation() {
        if (world == null)
            world = Bukkit.createWorld(WorldCreator.name("ffa"));

        Location find = null;
        while (find == null) {
            Point p = new Point(randInt(min.getX(), max.getX()), randInt(min.getY(), max.getY()));
            Block at = world.getHighestBlockAt((int)p.getX(), (int)p.getY());
            if (at.getType().equals(Material.AIR))
                continue;

            find = at.getLocation();
        }

        find.setYaw((float)(Math.random()*360));
        find.setPitch((float)(Math.random())+1);

        return find;
    }

    public static void announceFFA(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (FFAPlayer.getFFAPlayerFromPlayer(player).isPlaying())
                Form.at(player, Prefix.NOTHING, "&e&lFF&6&lA&f: &7"+message);
        }
    }

    /**
     * Returns a pseudo-random number between min and max, inclusive.
     * The difference between min and max can be at most
     * <code>Integer.MAX_VALUE - 1</code>.
     *
     * @param min Minimum value
     * @param max Maximum value.  Must be greater than min.
     * @return Integer between min and max, inclusive.
     * @see java.util.Random#nextInt(int)
     */
    public static int randInt(double min, double max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        double randomNum = rand.nextInt((int)(max - min) + 1) + min;

        return (int)randomNum;
    }
}
