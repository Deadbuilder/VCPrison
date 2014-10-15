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

    @Override
    public void onEnable() {
        dropEvent = new DropEvent();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public Plugin getWrapperPlugin() {
        return VCPrison.getInstance();
    }
}
