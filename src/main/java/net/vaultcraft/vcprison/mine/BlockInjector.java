package net.vaultcraft.vcprison.mine;

import org.bukkit.Location;

/**
 * Created by Connor on 9/15/14. Designed for the VCPrison project.
 */

public interface BlockInjector {

    public boolean doGenerate();

    public void setBlock(Location loc);
}
