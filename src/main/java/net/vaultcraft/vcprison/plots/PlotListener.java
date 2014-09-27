package net.vaultcraft.vcprison.plots;

import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class PlotListener implements Listener {

    public PlotListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if(!e.getPlayer().getWorld().getName().equalsIgnoreCase(PlotInfo.worldName))
            return;
        Chunk chunk = e.getTo().getChunk();
        if(chunk.getX() > PlotInfo.worldBoarderRadius || chunk.getX() < -PlotInfo.worldBoarderRadius
                || chunk.getZ() > PlotInfo.worldBoarderRadius || chunk.getZ() < -PlotInfo.worldBoarderRadius)
            e.getPlayer().teleport(e.getFrom());
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(!e.getPlayer().getWorld().getName().equalsIgnoreCase(PlotInfo.worldName))
            return;
        Block block = e.getClickedBlock();
        if(block == null)
            return;
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(block.getLocation());
        if(plot == null)
            return;
        if(plot.canBuild(e.getPlayer()))
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if(!e.getPlayer().getWorld().getName().equalsIgnoreCase(PlotInfo.worldName))
            return;
        Block block = e.getBlock();
        if(block == null)
            return;
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(block.getLocation());
        if(plot == null)
            return;
        if(plot.canBuild(e.getPlayer()))
            return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if(!e.getPlayer().getWorld().getName().equalsIgnoreCase(PlotInfo.worldName))
            return;
        Block block = e.getBlock();
        if(block == null)
            return;
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(block.getLocation());
        if(plot == null)
            return;
        if(plot.canBuild(e.getPlayer()))
            return;
        e.setCancelled(true);
    }
}
