package net.vaultcraft.vcprison.ffa.event;

import net.vaultcraft.vcprison.ffa.FFAPlayer;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Created by Connor Hollasch on 10/5/14.
 */

public class FFAJoinEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    private FFAPlayer player;

    public FFAJoinEvent(FFAPlayer ffaPlayer) {
        this.player = ffaPlayer;
    }

    public FFAPlayer getPlayer() {
        return player;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
