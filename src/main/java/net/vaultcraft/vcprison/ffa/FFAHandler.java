package net.vaultcraft.vcprison.ffa;

import org.bukkit.World;

/**
 * Created by Connor Hollasch on 9/28/14.
 */

public class FFAHandler {

    private static World ffaWorld;

    public static void setFFAWorld(World world) {
        ffaWorld = world;
    }

    public static World getFFAWorld() {
        return ffaWorld;
    }
}
