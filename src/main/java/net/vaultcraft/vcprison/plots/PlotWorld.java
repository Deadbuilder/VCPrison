package net.vaultcraft.vcprison.plots;

import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;

/**
 * Created by tacticalsk8er on 8/13/2014.
 */
public class PlotWorld {

    private static World plotWorld;
    private static PlotManager plotManager;
    private static PlotMenu plotMenu;
    private static PlotGen plotGenerator;

    public PlotWorld() {
        WorldCreator wc = new WorldCreator(PlotInfo.worldName);

        wc.generator(plotGenerator);
        wc.generateStructures(false);
        plotManager = new PlotManager();
        plotMenu = new PlotMenu();
        plotWorld = Bukkit.getServer().createWorld(wc);
        plotGenerator.addGeneratedPlots();
        plotManager.generatePlots();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VCPrison.getInstance(), plotGenerator::addGeneratedPlots, 0, 20);
    }

    public static World getPlotWorld() {
        return plotWorld;
    }

    public static PlotManager getPlotManager() {
        return plotManager;
    }

    public static PlotMenu getPlotMenu() {
        return plotMenu;
    }
}
