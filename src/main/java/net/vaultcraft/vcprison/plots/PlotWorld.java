package net.vaultcraft.vcprison.plots;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 * Created by tacticalsk8er on 8/13/2014.
 */
public class PlotWorld {

    private static World plotWorld;
    private static PlotManager plotManager;

    public PlotWorld() {
        WorldCreator wc = new WorldCreator(PlotInfo.worldName);
        wc.generator(new PlotGen());
        wc.generateStructures(false);
        plotManager = new PlotManager();
        plotWorld = Bukkit.getServer().createWorld(wc);
    }

    public static World getPlotWorld() {
        return plotWorld;
    }

    public static PlotManager getPlotManager() {
        return plotManager;
    }
}
