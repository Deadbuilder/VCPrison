package net.vaultcraft.vcprison.listener;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.ffa.FFADamageTracker;
import net.vaultcraft.vcprison.ffa.FFAHandler;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.protection.ProtectedArea;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

/**
 * Created by tacticalsk8er on 7/31/2014.
 */
public class PrisonUserListener implements Listener {

    public PrisonUserListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        new PrisonUser(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PrisonUser.remove(event.getPlayer());
    }

    @EventHandler
    public void blockBreak(final BlockBreakEvent event) {
        Runnable async = new Runnable() {
            public void run() {
                Location loc = event.getBlock().getLocation();
                final Mine mine = MineLoader.fromLocation(loc);
                if (mine == null)
                    return;

                mine.tickBlocks();

                if (mine.getPercent() > 0.3) {
                    mine.reset();
                    Runnable sync = new Runnable() {
                        public void run() {
                            MineLoader.resetMine(mine);
                            for (Player player : Bukkit.getOnlinePlayers()) {
                                Form.at(player, "Mine: &e"+mine.getRank().toString()+ Prefix.VAULT_CRAFT.getChatColor()+" reset!");
                            }
                        }
                    };
                    Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), sync);
                }
            }
        };
        Bukkit.getScheduler().scheduleAsyncDelayedTask(VCPrison.getInstance(), async);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity hurt = event.getEntity();
        Entity damager = event.getDamager();

        if (!(hurt instanceof Player) || !(damager instanceof Player))
            return;

        if (ProtectionManager.getInstance().getState(FlagType.PVP, hurt.getLocation()).isCancelled())
            return;

        for (ProtectedArea area : ProtectionManager.getInstance().fromLocation(hurt.getLocation())) {
            if (ProtectionManager.getInstance().getArea("ffa").equals(area)) {
                //is ffa area
                FFADamageTracker.setLastDamager((Player)hurt, (Player)damager);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (FFAPlayer.getFFAPlayerFromPlayer(event.getPlayer()).isPlaying())
            event.setRespawnLocation(FFAHandler.getFFASpawn());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (!FFAPlayer.getFFAPlayerFromPlayer(event.getEntity()).isPlaying())
            return;

        Player killer = FFADamageTracker.getLastDamager(event.getEntity());
        FFAHandler.handleDeath(event.getEntity(), killer);
    }

    @EventHandler
    public void onSignUpdate(SignChangeEvent event) {
        if (event.getPlayer().isOp()) {
            int x = -1;
            for (String line : event.getLines()) {
                event.setLine(++x, ChatColor.translateAlternateColorCodes('&', line));
            }
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (!event.getWorld().hasStorm())
            event.setCancelled(true);
    }
}
