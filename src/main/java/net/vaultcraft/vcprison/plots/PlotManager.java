package net.vaultcraft.vcprison.plots;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import net.minecraft.util.com.google.gson.Gson;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.database.sqlite.SQLite;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class PlotManager {

    private List<Plot> plots = new ArrayList<>();
    private SQLite sqLite = VCUtils.getInstance().getSqlite();

    public PlotManager() {
        sqLite.updateThread.add(Statements.TABLE_SQLITE.getSql("Plots", "LONGTEXT JSON"));
        Logger.log(VCPrison.getInstance(), "Loading plots...");
        sqLite.addQuery(Statements.QUERYALL.getSql("Plots"), new MySQL.ISqlCallback() {
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

    public Plot getAvaliblePlot() {
        for (Plot plot : plots) {
            if (!plot.hasOwner())
                if (plot.getChunkX() <= PlotInfo.worldBoarderRaduis || plot.getChunkX() >= -PlotInfo.worldBoarderRaduis
                        || plot.getChunkZ() <= PlotInfo.worldBoarderRaduis
                        || plot.getChunkZ() >= -PlotInfo.worldBoarderRaduis)
                    return plot;
        }
        return null;
    }

    public List<Plot> getPlots() {
        return plots;
    }

    public void savePlots() {
        sqLite.updateThread.add("DELETE FROM Plots");
        Gson gson = new Gson();
        for (Plot plot : plots) {
            String json = gson.toJson(plot);
            sqLite.updateThread.add(Statements.INSERT.getSql("Plots", json));
        }
    }

    public List<Plot> getPlayerPlots(Player player) {
        List<Plot> playerPlots = new ArrayList<>();
        String playerUUID = player.getUniqueId().toString();
        for (Plot plot : plots)
            if (plot.getOwnerUUID().equals(playerUUID))
                playerPlots.add(plot);
        return playerPlots;
    }

    public void addNewPlots(Chunk chunk) {
        for(CuboidSelection cuboidSelection : PlotInfo.getPlotCubiods()) {
            plots.add(new Plot(cuboidSelection, chunk.getX(), chunk.getZ()));
        }
    }

    public Plot getPlotFromLocation(Location location) {
        for(Plot plot : plots) {
            if(plot.getPlotArea().contains(location))
                return plot;
        }
        return null;
    }
}
