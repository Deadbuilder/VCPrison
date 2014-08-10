package net.vaultcraft.vcprison.listener;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
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

                if (mine.getPercent() > 0.5) {
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
