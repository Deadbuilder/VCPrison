package net.vaultcraft.vcprison.plots;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import net.vaultcraft.vcutils.config.ClassConfig;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class PlotInfo {
    @ClassConfig.Config(path = "Plots.PlotWorld")
    public static String worldName = "PlotWorld";
    @ClassConfig.Config(path = "Plots.PlotRegions")
    public static List<String> plotRegions = new ArrayList<>(Arrays.asList("2 84 3,13 85 12", "2 88 3,13 89 12"));
    @ClassConfig.Config(path = "Plots.WorldBoarderRadius")
    public static int worldBoarderRadius = 50;

    public static List<CuboidSelection> getPlotCubiods() {
        List<CuboidSelection> cuboidSelections = new ArrayList<>();
        for(String s: plotRegions) {
            String[] locations = s.split(",");
            String[] location1String = locations[0].split(" ");
            String[] location2String = locations[1].split(" ");
            Location location1 = new Location(PlotWorld.getPlotWorld(), Double.parseDouble(location1String[0]),
                    Double.parseDouble(location1String[1]), Double.parseDouble(location1String[2]));
            Location location2 = new Location(PlotWorld.getPlotWorld(), Double.parseDouble(location2String[0]),
                    Double.parseDouble(location2String[1]), Double.parseDouble(location1String[2]));
            cuboidSelections.add(new CuboidSelection(PlotWorld.getPlotWorld(), location1, location2));
        }
        return cuboidSelections;
    }
}
