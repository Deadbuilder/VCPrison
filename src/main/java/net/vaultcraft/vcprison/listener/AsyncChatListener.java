package net.vaultcraft.vcprison.listener;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * Created by Connor on 7/31/14. Designed for the VCPrison project.
 */

public class AsyncChatListener implements Listener {

    public AsyncChatListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onAsyncChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        PrisonUser user = PrisonUser.fromPlayer(player);

        String format = event.getFormat();
        event.setFormat(ChatColor.translateAlternateColorCodes('&',user.getPrestigePrefix() + user.getRank().getPrefix()+"&r "+format));
    }
}
