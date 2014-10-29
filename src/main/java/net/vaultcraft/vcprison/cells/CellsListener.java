package net.vaultcraft.vcprison.cells;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 * Created by tacticalsk8er on 10/26/2014.
 */
public class CellsListener implements Listener {

    public CellsListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        //TODO World Border
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        //TODO Handle block placing in plots.
        Cell possibleCell = VCPrison.getInstance().getCellManager().getCellFromLocation(event.getBlockPlaced().getLocation());
        if(possibleCell == null) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "You can't place blocks in a cell or area you do not have access to.");
            return;
        }
        if(event.getPlayer().getUniqueId() != possibleCell.ownerUUID || !possibleCell.additionalUUIDs.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "You can't place blocks in a cell or area you do not have access to.");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        //TODO Handle block breaking in plots.
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        //TODO Handle interact events in plots.
    }
}
