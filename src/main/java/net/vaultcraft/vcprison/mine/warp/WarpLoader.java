package net.vaultcraft.vcprison.mine.warp;

import net.minecraft.util.org.apache.commons.io.FileUtils;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Connor on 8/4/14. Designed for the VCPrison project.
 */

public class WarpLoader {

    private static HashMap<Rank, Location> warps = new HashMap<>();

    public static void loadMines() {
        try {
            //load
            File folder = VCPrison.getInstance().getDataFolder();
            if (!(folder.exists()))
                folder.mkdirs();

            File file = new File(folder, "mine.warps");
            if (!(file.exists()))
                file.createNewFile();

            for (String line : FileUtils.readLines(file)) {
                line = line.replace(" ", "").toLowerCase();
                String in = findInsideMap(line, "rank");
                String[] loc = findInsideMap(line, "location").split(",");

                World world = Bukkit.getWorld(loc[0]);
                double x = Double.parseDouble(loc[1]);
                double y = Double.parseDouble(loc[2]);
                double z = Double.parseDouble(loc[3]);
                float yaw = Float.parseFloat(loc[4]);
                float pitch = Float.parseFloat(loc[5]);

                warps.put(Rank.fromName(in), new Location(world, x, y, z, yaw, pitch));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String findInsideMap(String total, String key) {
        int after = total.indexOf(key)+key.length()+1;
        String modify = total.substring(after);
        String make = "";
        int deep = 0;
        for (char c : modify.toCharArray()) {
            if (c == '(')
                deep++;
            if (c == ')')
                deep--;

            if (deep >= 0)
                make+=c;
            else
                break;
        }
        return make;
    }

    public static Location getWarpLocation(Rank rank) {
        return warps.get(rank);
    }
}
