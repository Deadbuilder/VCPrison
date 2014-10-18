package net.vaultcraft.vcprison.plots;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import net.minecraft.util.com.google.gson.Gson;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.config.ClassConfig;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class PlotManager {

    private volatile ConcurrentHashMap<String, Plot> plots = new ConcurrentHashMap<>();
    private LinkedList<Chunk> newPlots = new LinkedList<>();
    private BukkitTask task = null;
    private BukkitTask task2 = null;

    public PlotManager() {
        ConfigurationSection section = VCPrison.getInstance().getConfig().getConfigurationSection("Plots.GenPlots");
        Gson gson = new Gson();
        if(section != null) {
            for (String key : section.getKeys(false)) {
                plots.put(key, gson.fromJson(section.getString(key), Plot.class));
            }
        }
        task2 = Bukkit.getScheduler().runTaskTimerAsynchronously(VCPrison.getInstance(),
                () -> VCPrison.getInstance().saveConfig(),  18000l, 18000l);
    }

    public Plot getAvailablePlot() {
        if (plots.size() <= 0) {
            return null;
        }
        for (Plot plot : plots.values()) {
            if (!plot.hasOwner())
                if (plot.getChunkX() <= PlotInfo.worldBoarderRadius || plot.getChunkX() >= -PlotInfo.worldBoarderRadius
                        || plot.getChunkZ() <= PlotInfo.worldBoarderRadius
                        || plot.getChunkZ() >= -PlotInfo.worldBoarderRadius)
                    return plot;
        }
        PlotInfo.worldBoarderRadius += 1;
        ClassConfig.updateConfig(PlotInfo.class, VCPrison.getInstance().getConfig());
        VCPrison.getInstance().saveConfig();
        return this.getAvailablePlot();
    }

    public Collection<Plot> getPlots() {
        return plots.values();
    }

    public List<Plot> getPlayerPlots(OfflinePlayer player) {
        List<Plot> playerPlots = new ArrayList<>();
        String playerUUID = player.getUniqueId().toString();
        for (Plot plot : plots.values())
            if (plot.getOwnerUUID().equals(playerUUID))
                playerPlots.add(plot);
        return playerPlots;
    }

    public void generatePlots() {
        task = Bukkit.getScheduler().runTaskTimer(VCPrison.getInstance(), () -> {
            if (newPlots.size() > 0) {
                Chunk chunk = newPlots.pop();
                Gson gson = new Gson();
                for (CuboidSelection cuboidSelection : PlotInfo.getPlotCubiods()) {
                    Plot plot = new Plot(cuboidSelection, chunk.getX(), chunk.getZ());
                    plots.put(plot.getPlotUUID(), plot);
                    VCPrison.getInstance().getConfig().set("Plots.GenPlots." + plot.getPlotUUID(), gson.toJson(plot));
                }
            }
        }, 0, 5);
    }

    public void addNewPlots(Chunk chunk) {
        newPlots.add(chunk);
    }

    public Plot getPlotFromLocation(Location location) {
        for (Plot plot : plots.values()) {
            if (plot.getPlotArea().contains(location))
                return plot;
        }
        return null;
    }

    public void disable() {
        if(task != null)
            task.cancel();
        if(task2 != null)
            task2.cancel();
        VCPrison.getInstance().saveConfig();
    }
}
