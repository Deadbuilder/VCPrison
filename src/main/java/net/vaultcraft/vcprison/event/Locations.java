package net.vaultcraft.vcprison.event;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Connor Hollasch
 * @since 10/14/14
 */
public class Locations {

    //HARDCODED LOCATIONS
    public static final World world = Bukkit.getWorld("world");
    public static final Location[] pillars = {
            build("-3843.5,89.0,11.5"),
            build("-3847.5,89.0,8.5"),
            build("-3850.5,89.0,4.5"),
            build("-3850.5,89.0,-3.5"),
            build("-3847.5,89.0,-7.5"),
            build("-3843.5,89.0,-10.5"),
            build("-3835.5,89.0,-10.5"),
            build("-3831.5,89.0,-7.5"),
            build("-3828.5,89.0,-3.5"),
            build("-3828.5,89.0,4.5"),
            build("-3831.5,89.0,8.5"),
            build("-3835.5,89.0,11.5")
    };

    public static final Location center = build("-3839.5,86.0,0.5");

    private static Location build(String in) {
        return new Location(world, Double.parseDouble(in.split(",")[0]), Double.parseDouble(in.split(",")[1]), Double.parseDouble(in.split(",")[2]));
    }
}
