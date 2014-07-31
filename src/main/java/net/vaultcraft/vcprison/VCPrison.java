package net.vaultcraft.vcprison;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by tacticalsk8er on 7/30/2014.
 */
public class VCPrison extends JavaPlugin { //1.08

    private static VCPrison instance;

    public void onEnable() {
        instance = this;
    }

    public static VCPrison getInstance() {
        return instance;
    }
}
