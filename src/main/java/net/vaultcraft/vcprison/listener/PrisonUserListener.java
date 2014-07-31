package net.vaultcraft.vcprison.listener;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * Created by tacticalsk8er on 7/31/2014.
 */
public class PrisonUserListener implements Listener {

    public PrisonUserListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onJoin(PlayerJoinEvent event) {
        new PrisonUser(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerJoinEvent event) {
        PrisonUser.remove(event.getPlayer());
    }
}
