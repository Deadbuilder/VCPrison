package net.vaultcraft.vcprison.plots;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.logging.Logger;
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
        Logger.debug(VCPrison.getInstance(), "Created World");
    }

    public static World getPlotWorld() {
        return plotWorld;
    }
}
