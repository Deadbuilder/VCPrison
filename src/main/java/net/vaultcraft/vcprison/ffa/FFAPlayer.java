package net.vaultcraft.vcprison.ffa;

import net.vaultcraft.vcprison.ffa.event.FFAJoinEvent;
import net.vaultcraft.vcprison.ffa.event.FFALeaveEvent;
import net.vaultcraft.vcprison.sword.Sword;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Connor Hollasch on 9/28/14.
 */

public class FFAPlayer {

    private static HashMap<Player, FFAPlayer> ffaPlayers = new HashMap<>();

    private Sword sword;

    public static FFAPlayer getFFAPlayerFromPlayer(Player player) {
        return ffaPlayers.get(player);
    }

    public static void removePlayer(Player player) {
        ffaPlayers.remove(player);
    }

    //============== Game data ==============

    private Player player;
    private boolean playing = false;
    private ItemStack[] saved;

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

    public int alltimeKills = 0;
    public int alltimeDeaths = 0;

    public int currentKillstreak = 0;
    public int highestKillstreak = 0;

    public double bounty = 0.0;
    //===================================

    public void beginFFA() {
        player.teleport(FFAHandler.getFFASpawn());

        PrisonUser.fromPlayer(player).getPickaxe().setInUse(false);
        saved = player.getInventory().getContents();

        player.getInventory().clear();

        player.getEquipment().setHelmet(FFAItems.startingHelmet);
        player.getEquipment().setChestplate(FFAItems.startingChestplate);
        player.getEquipment().setLeggings(FFAItems.startingLeggings);
        player.getEquipment().setBoots(FFAItems.startingBoots);

        player.getInventory().setItem(0, FFAItems.startingSword);

        player.updateInventory();

        Bukkit.getPluginManager().callEvent(new FFAJoinEvent(this));
    }

    public void endFFA() {
        //TODO - Sell items that players have from FFA
        player.getInventory().clear();
        player.getInventory().setContents(saved);

        Bukkit.getPluginManager().callEvent(new FFALeaveEvent(this));
    }
}
