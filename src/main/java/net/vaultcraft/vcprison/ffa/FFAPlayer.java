package net.vaultcraft.vcprison.ffa;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.ffa.event.FFAJoinEvent;
import net.vaultcraft.vcprison.ffa.event.FFALeaveEvent;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by Connor Hollasch on 9/28/14.
 */

public class FFAPlayer {

    private static HashMap<Player, FFAPlayer> ffaPlayers = new HashMap<>();

    public static FFAPlayer getFFAPlayerFromPlayer(Player player) {
        return ffaPlayers.get(player);
    }

    public static void removePlayer(Player player) {
        ffaPlayers.remove(player);
    }

    //============== Game data ==============

    private Player player;
    private boolean playing = false;

    private User user;

    //=======================================

    public FFAPlayer(Player player) {
        this.player = player;
        ffaPlayers.put(player, this);
        this.user = User.fromPlayer(player);
    }

    public User getUser() {
        return user;
    }

    public boolean isPlaying() {
        return playing;
    }

    //============== Stats ==============

    public int sessionKills = 0;
    public int sessionDeaths = 0;

    public double bounty = 1.0;

    //===================================

    public void beginFFA() {
        PrisonUser user = PrisonUser.fromPlayer(player);
        player.teleport(FFAHandler.getRandomSpawnLocation());

        user.getPickaxe().setInUse(false);

        Bukkit.getPluginManager().callEvent(new FFAJoinEvent(this));

        user.getPickaxe().setInUse(false);
        user.getSword().setInUse(true);
        player.updateInventory();

        player.setGameMode(GameMode.ADVENTURE);

        playing = true;
    }

    public void endFFA() {
        PrisonUser user = PrisonUser.fromPlayer(player);
        //TODO - Sell items that players have from FFA

        Bukkit.getPluginManager().callEvent(new FFALeaveEvent(this));

        if(user == null)
            return;
        user.getSword().setInUse(false);
        user.getPickaxe().setInUse(true);
        player.updateInventory();

        player.setGameMode(GameMode.SURVIVAL);

        playing = false;
        player.teleport(VCPrison.spawn);
    }
}
