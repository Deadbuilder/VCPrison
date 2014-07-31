package net.vaultcraft.vcprison;

import net.vaultcraft.vcprison.listener.AsyncChatListener;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by tacticalsk8er on 7/30/2014.
 */
public class VCPrison extends JavaPlugin {

    private static VCPrison instance;

    public void onEnable() {
        instance = this;

        new AsyncChatListener();
    }

    public static VCPrison getInstance() {
        return instance;
    }
}
