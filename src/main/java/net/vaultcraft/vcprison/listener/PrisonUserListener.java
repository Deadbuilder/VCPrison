package net.vaultcraft.vcprison.listener;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.cells.CellManager;
import net.vaultcraft.vcprison.event.DropParty;
import net.vaultcraft.vcprison.ffa.FFADamageTracker;
import net.vaultcraft.vcprison.ffa.FFAHandler;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcprison.ffa.combatlog.CombatLog;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.scoreboard.PrisonScoreboard;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.user.UserLoadedEvent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

/**
 * Created by tacticalsk8er on 7/31/2014.
 */
public class PrisonUserListener implements Listener {

    public PrisonUserListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onUserLoaded(UserLoadedEvent event) {
        new PrisonUser(event.getUser().getPlayer());
        PrisonScoreboard.addPlayer(event.getUser().getPlayer());

        event.getUser().getPlayer().teleport(VCPrison.spawn);
        event.getUser().getPlayer().setGameMode(GameMode.SURVIVAL);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CombatLog.handleLeave(event.getPlayer());
        PrisonUser.remove(event.getPlayer());
        PrisonScoreboard.removePlayer(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        PrisonUser.remove(event.getPlayer());
    }

    @EventHandler
    public void blockBreak(final BlockBreakEvent event) {
        Runnable async = () -> {
            Location loc = event.getBlock().getLocation();
            final Mine mine = MineLoader.fromLocation(loc);
            if (mine == null)
                return;

            if (mine.isResetting())
                return;

            mine.tickBlocks();

            if (mine.getPercent() > 0.3) {
                Runnable sync = () -> {
                    if (mine.isResetting())
                        return;

                    MineLoader.resetMine(mine);
                    MineLoader.initCannotReset(mine);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Form.at(player, "Mine: &e"+mine.getRank().toString()+ Prefix.VAULT_CRAFT.getChatColor()+" reset!");
                    }
                };
                Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), sync);
            }
        };
        Bukkit.getScheduler().scheduleAsyncDelayedTask(VCPrison.getInstance(), async);
    }

    @EventHandler
    public static void onFallDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        if(event.getCause() != EntityDamageEvent.DamageCause.FALL)
            return;
        Player player = (Player) event.getEntity();
        if(!FFAPlayer.getFFAPlayerFromPlayer(player).isPlaying())
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        Entity hurt = event.getEntity();
        Entity damager = event.getDamager();

        if (!(hurt instanceof Player) || !(damager instanceof Player))
            return;

        if (ProtectionManager.getInstance().getState(FlagType.PVP, hurt.getLocation()).isCancelled())
            return;

        if(hurt.getLocation().getWorld().getName().equalsIgnoreCase("ffa")) {
            FFAPlayer ffaPlayer = FFAPlayer.getFFAPlayerFromPlayer((Player) hurt);
            FFAPlayer ffaPlayer1 = FFAPlayer.getFFAPlayerFromPlayer((Player) damager);
            if(ffaPlayer.getLastJoin() + 5000 > System.currentTimeMillis()) {
                event.setCancelled(true);
                Form.at((Player) damager, Prefix.ERROR, "You can't hurt a player that just spawned.");
                return;
            }
            if(ffaPlayer1.getLastJoin() + 5000 > System.currentTimeMillis()) {
                event.setCancelled(true);
                Form.at((Player) damager, Prefix.ERROR, "You can't hurt players since you just spawned.");
                return;
            }
            FFADamageTracker.setLastDamager((Player) hurt, (Player) damager);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (FFAPlayer.getFFAPlayerFromPlayer(event.getPlayer()).isPlaying())
            FFAPlayer.getFFAPlayerFromPlayer(event.getPlayer()).endFFA();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);

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

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            //Don't remove the entity!
            return;
        }

        Player player = (Player) event.getEntity();
        if (event.getCause().equals(EntityDamageEvent.DamageCause.VOID)) {
            if(FFAPlayer.getFFAPlayerFromPlayer(player).isPlaying()) {
                event.setCancelled(false);
                return;
            }
            Location location = new Location(Bukkit.getServer().getWorld("world"), -3839.5, 86, 0.5);
            player.teleport(location);
        }
    }

    private static List<Player> dp = Lists.newArrayList();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock().getType().equals(Material.ANVIL) && !event.getPlayer().getWorld().equals(VCPrison.getInstance().getCellManager().getPlotWorld())) {
            event.setCancelled(true);
        }

        Player player = event.getPlayer();

        /*ItemStack hand = player.getItemInHand();
        /hand.setAmount(1);

        if (DropParty.getDpToken().clone().equals(hand)) {
            player.getInventory().removeItem(player.getItemInHand());

            final Item drop = player.getWorld().dropItem(player.getLocation(), DropParty.getDpToken().clone());
            drop.setVelocity(player.getEyeLocation().getDirection().normalize().multiply(2));
            drop.setPickupDelay(20 * 60 * 60);

            Runnable delay = () -> {
                drop.remove();
                dp.add(player);
                BukkitRunnable br = new BukkitRunnable() {
                    public void run() {
                        if (!dp.contains(player)) {
                            cancel();
                            return;
                        }
                    }
                };
                br.runTaskTimer(VCPrison.getInstance(), 5, 5);
                Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), () -> dp.remove(player), 20 * 10);
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), delay, 20 * 3);
        }*/
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (FFAPlayer.getFFAPlayerFromPlayer(event.getPlayer()).isPlaying())
            event.setCancelled(true);
    }
}
