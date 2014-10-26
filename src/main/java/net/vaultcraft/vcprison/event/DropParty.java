package net.vaultcraft.vcprison.event;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.events.ServerEvent;
import net.vaultcraft.vcutils.innerplugin.InnerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class DropParty extends InnerPlugin {

    private ServerEvent dropEvent;
    private static DropParty instance;
    private int timeLeft = 30*60;

    public void onEnable() {
        instance = this;
        dropEvent = new DropEvent();

        Runnable task = new Runnable() {
            public void run() {
                timeLeft--;
                if (timeLeft <= 0) {
                    dropEvent.onEvent(VCPrison.getInstance());
                    timeLeft = (30*60);
                    return;
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VCPrison.getInstance(), task, 20, 20);
    }

    public void onDisable() {

    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public DropEvent getDropEvent() {
        return (DropEvent) dropEvent;
    }

    public static DropParty getInstance() {
        return instance;
    }

    @Override
    public Plugin getWrapperPlugin() {
        return VCPrison.getInstance();
    }

    public int getTimeLeft() {
        return timeLeft;
    }
}
