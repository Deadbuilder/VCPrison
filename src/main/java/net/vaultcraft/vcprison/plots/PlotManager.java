package net.vaultcraft.vcprison.plots;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import net.minecraft.util.com.google.gson.Gson;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.config.ClassConfig;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.database.sqlite.SQLite;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class PlotManager {

    private List<Plot> plots = new ArrayList<>();
    private List<Chunk> newPlots = new ArrayList<>();
    private SQLite sqLite = VCUtils.getInstance().getSqlite();

    public PlotManager() {
        sqLite.doUpdate(Statements.TABLE_SQLITE.getSql("Plots", "JSON TEXT"));
        Logger.log(VCPrison.getInstance(), "Loading plots...");
        sqLite.doQuery(Statements.QUERYALL.getSql("Plots"), new MySQL.ISqlCallback() {
            @Override
            public void onSuccess(ResultSet resultSet) {
                Gson gson = new Gson();
                try {
                    while (resultSet.next()) {
                        String json = resultSet.getString("JSON");
                        plots.add(gson.fromJson(json, Plot.class));
                    }
                    Logger.log(VCPrison.getInstance(), "Plots loaded.");
                } catch (SQLException e) {
                    Logger.error(VCPrison.getInstance(), e);
                }
            }

            @Override
            public void onFailure(SQLException e) {
                Logger.error(VCPrison.getInstance(), e);
            }
        });
    }

    public Plot getAvailablePlot() {
        if (plots.size() <= 0) {
            return null;
        }
        for (Plot plot : plots) {
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

    public List<Plot> getPlots() {
        return plots;
    }

    public void savePlots() {
        sqLite.doUpdate("DELETE FROM Plots");
        Gson gson = new Gson();
        for (Plot plot : plots) {
            String json = gson.toJson(plot);
            sqLite.doUpdate(Statements.INSERT_SQLITE.getSql("Plots", "JSON", '"' + Statements.makeSqlSafe(json) + '"'));
        }
    }

    public List<Plot> getPlayerPlots(OfflinePlayer player) {
        List<Plot> playerPlots = new ArrayList<>();
        String playerUUID = player.getUniqueId().toString();
        for (Plot plot : plots)
            if (plot.getOwnerUUID().equals(playerUUID))
                playerPlots.add(plot);
        return playerPlots;
    }

    public void generatePlots() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(VCPrison.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (newPlots.size() > 0) {
                    Chunk chunk = newPlots.get(0);
                    for (CuboidSelection cuboidSelection : PlotInfo.getPlotCubiods())
                        plots.add(new Plot(cuboidSelection, chunk.getX(), chunk.getZ()));
                    newPlots.remove(0);
                }
            }
        }, 0, 5);
    }

    public void addNewPlots(Chunk chunk) {
        newPlots.add(chunk);
    }

    public Plot getPlotFromLocation(Location location) {
        for (Plot plot : plots) {
            if (plot.getPlotArea().contains(location))
                return plot;
        }
        return null;
    }
}
