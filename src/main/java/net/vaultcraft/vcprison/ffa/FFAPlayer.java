package net.vaultcraft.vcprison.ffa;

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

    private Player player;
    private boolean playing = false;

    public FFAPlayer(Player player) {
        this.player = player;
        ffaPlayers.put(player, this);
    }

    public boolean isPlaying() {
        return playing;
    }

    public void beginFFA() {
        //Teleport player
    }
}
