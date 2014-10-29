package net.vaultcraft.vcprison.cells;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
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
        if(!event.getTo().getWorld().equals(VCPrison.getInstance().getCellManager().getPlotWorld()))
            return;
        if(event.getTo().getBlockZ() > (16 * 27) || event.getTo().getBlockZ() < -(16 * 26)
                || event.getTo().getBlockX() > (16 * (CellManager.xRadius + 2)) || event.getTo().getBlockX() < -(16 * CellManager.xRadius + 1)) {
            event.getPlayer().teleport(event.getFrom());
            Form.at(event.getPlayer(), Prefix.ERROR, "You hit the world boarder!");
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Cell possibleCell = VCPrison.getInstance().getCellManager().getCellFromLocation(event.getBlockPlaced().getLocation());
        if(possibleCell == null) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "You can't place blocks in a cell or area you do not have access to.");
            return;
        }
        if(!event.getPlayer().getUniqueId().equals(possibleCell.ownerUUID) && !possibleCell.additionalUUIDs.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "You can't place blocks in a cell or area you do not have access to.");
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Cell possibleCell = VCPrison.getInstance().getCellManager().getCellFromLocation(event.getBlock().getLocation());
        if(possibleCell == null) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "You can't break blocks in a cell or area you do not have access to.");
            return;
        }
        if(!event.getPlayer().getUniqueId().equals(possibleCell.ownerUUID) && !possibleCell.additionalUUIDs.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "You can't break blocks in a cell or area you do not have access to.");
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Cell possibleCell = VCPrison.getInstance().getCellManager().getCellFromLocation(event.getClickedBlock().getLocation());
        if(possibleCell == null) {
            return;
        }
        if(!event.getPlayer().getUniqueId().equals(possibleCell.ownerUUID) && !possibleCell.additionalUUIDs.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "You can't interact with blocks in a cell or area you do not have access to.");
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if(!event.getInventory().getTitle().contains("'s Cells"))
            return;
        event.setCancelled(true);
        if(event.getCurrentItem() == null)
            return;
        if(event.getCurrentItem().getItemMeta() == null)
            return;
        if(event.getCurrentItem().getItemMeta().getDisplayName() == null)
            return;
        if(event.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.DARK_GREEN + "Create Cell")) {
            Chunk nextFree = VCPrison.getInstance().getCellManager().getNextOpenCell();
            if(nextFree == null) {
                Form.at((org.bukkit.entity.Player) event.getWhoClicked(), Prefix.ERROR, "Ask VC to smack CyberKisune pls");
                return;
            }

            int ownedCells = VCPrison.getInstance().getCellManager().getCellsFromPlayer((org.bukkit.entity.Player) event.getWhoClicked()).size();

            if(ownedCells >= PrisonUser.fromPlayer((org.bukkit.entity.Player) event.getWhoClicked()).getPlotLimit()) {
                Form.at((org.bukkit.entity.Player) event.getWhoClicked(), Prefix.ERROR, "You have hit the limit on the amount of cells you can have.");
                return;
            }

            Cell newCell = new Cell();
            newCell.ownerUUID = event.getWhoClicked().getUniqueId();
            newCell.chunkX = nextFree.getX();
            newCell.chunkZ = nextFree.getZ();
            newCell.cellSpawn = new Location(nextFree.getWorld(), (nextFree.getX()*16) + 13, 88, (nextFree.getZ()*16) + 12, 90f, 135f);
            newCell.name = "Cell #" + (ownedCells + 1);
            VCPrison.getInstance().getCellManager().addCell(newCell);
            event.getWhoClicked().teleport(newCell.cellSpawn);
            Form.at((org.bukkit.entity.Player) event.getWhoClicked(), Prefix.VAULT_CRAFT, "Teleporting you to your new cell!");
            return;
        }
        if(event.getCurrentItem().getItemMeta().getLore() == null)
            return;
        if(event.getCurrentItem().getItemMeta().getLore().size() != 2)
            return;
        if(!event.getCurrentItem().getItemMeta().getLore().get(0).equals("Cell"))
            return;
        Location location = VCPrison.getInstance().getCellManager().stringToLocation(event.getCurrentItem().getItemMeta().getLore().get(1));
        event.getWhoClicked().teleport(location);
        Form.at((org.bukkit.entity.Player) event.getWhoClicked(), Prefix.SUCCESS, "Teleported to " + event.getCurrentItem().getItemMeta().getDisplayName() + " cell.");
    }
}
