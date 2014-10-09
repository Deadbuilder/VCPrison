package net.vaultcraft.vcprison.sword;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.user.UserLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

/**
 * Created by tacticalsk8er on 10/5/2014.
 */
public class SwordListener implements Listener {

    public SwordListener () {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onUserLoad(UserLoadedEvent event) {
        PrisonUser user = PrisonUser.fromPlayer(event.getUser().getPlayer());
        if (event.getUser().getUserdata("Pickaxe") != null) {
            //user.setSword(new Sword(user.getPlayer(), event.getUser().getUserdata("Sword")));
        } else {
            user.setSword(new Sword(user.getPlayer()));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Sword sword = PrisonUser.fromPlayer(event.getEntity()).getSword();
        event.getDrops().remove(sword.getSword());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Sword sword = PrisonUser.fromPlayer(event.getPlayer()).getSword();
        if(sword.isInUse())
            event.getPlayer().getInventory().setItem(0, sword.getSword());
    }
}
