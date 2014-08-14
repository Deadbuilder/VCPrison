package net.vaultcraft.vcprison.plots;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 * Created by tacticalsk8er on 8/13/2014.
 */
public class PlotWorld {

    private static World plotWorld;

    public PlotWorld(String name) {
        WorldCreator wc = new WorldCreator(name);
        wc.generator(new PlotGen());
        wc.generateStructures(false);
        plotWorld = Bukkit.getServer().createWorld(wc);
    }

    public static World getPlotWorld() {
        return plotWorld;
    }
}
