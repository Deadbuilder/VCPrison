package net.vaultcraft.vcprison.event;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.events.ServerEvent;
import net.vaultcraft.vcutils.innerplugin.InnerPlugin;
import org.bukkit.plugin.Plugin;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class DropParty extends InnerPlugin {

    private ServerEvent dropEvent;
    private static DropParty instance;

    @Override
    public void onEnable() {
        instance = this;
        dropEvent = new DropEvent();
    }

    @Override
    public void onDisable() {

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
}
