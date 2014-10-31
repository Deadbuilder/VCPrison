package net.vaultcraft.vcprison.cells;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.material.Openable;

/**
 * Created by tacticalsk8er on 10/26/2014.
 */
public class CellsListener implements Listener {

    public CellsListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onMove(PlayerMoveEvent event) {
        if(!event.getTo().getWorld().equals(VCPrison.getInstance().getCellManager().getPlotWorld()))
            return;
        if(event.isCancelled())
            event.setCancelled(false);
        if(event.getTo().getBlockZ() > (16 * 27) || event.getTo().getBlockZ() < -(16 * 26)
                || event.getTo().getBlockX() > (16 * (CellManager.xRadius + 2)) || event.getTo().getBlockX() < -(16 * CellManager.xRadius + 1)) {
            event.getPlayer().teleport(event.getFrom());
            Form.at(event.getPlayer(), Prefix.ERROR, "You hit the world border!");
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlace(BlockPlaceEvent event) {
        if(!event.getBlock().getLocation().getWorld().equals(VCPrison.getInstance().getCellManager().getPlotWorld()))
            return;
        if(event.getPlayer().isOp())
            return;
        if(event.isCancelled())
            event.setCancelled(false);
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

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreak(BlockBreakEvent event) {
        if(!event.getBlock().getLocation().getWorld().equals(VCPrison.getInstance().getCellManager().getPlotWorld()))
            return;
        if(event.getPlayer().isOp())
            return;
        if(event.isCancelled())
            event.setCancelled(false);
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

    @EventHandler(priority = EventPriority.LOW)
    public void onInteract(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null)
            return;
        if(!event.getClickedBlock().getLocation().getWorld().equals(VCPrison.getInstance().getCellManager().getPlotWorld()))
            return;
        Material type = event.getClickedBlock().getType();
        if(type != Material.IRON_DOOR_BLOCK && type != Material.CHEST && type != Material.ENDER_CHEST && type != Material.BREWING_STAND
                && type != Material.FURNACE && type != Material.ANVIL && type != Material.TRAPPED_CHEST && type != Material.BEACON
                && type != Material.BED_BLOCK && type != Material.BURNING_FURNACE && type != Material.ENCHANTMENT_TABLE
                && type != Material.DISPENSER && type != Material.HOPPER && type != Material.DROPPER && type != Material.STONE_BUTTON
                && type != Material.WOOD_BUTTON && type != Material.LEVER)
            return;
        Cell possibleCell = VCPrison.getInstance().getCellManager().getCellFromLocation(event.getClickedBlock().getLocation());
        if(possibleCell == null) {
            if(event.getClickedBlock().getType() == Material.IRON_DOOR_BLOCK && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                BlockState state = event.getClickedBlock().getState();
                if(event.getClickedBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_DOOR_BLOCK)
                    state = event.getClickedBlock().getRelative(BlockFace.DOWN).getState();
                Openable openable = (Openable) state.getData();
                if(openable.isOpen()) {
                    openable.setOpen(false);
                    event.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.DOOR_OPEN, 1, 1);
                } else {
                    openable.setOpen(true);
                    event.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.DOOR_CLOSE, 1, 1);
                }
                state.setData((org.bukkit.material.MaterialData) openable);
                state.update();
            }
            return;
        }
        if(!event.getPlayer().getUniqueId().equals(possibleCell.ownerUUID) && !possibleCell.additionalUUIDs.contains(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.WARNING, "You can't interact with blocks in a cell or area you do not have access to.");
            return;
        }

        if(event.getClickedBlock().getType() == Material.IRON_DOOR_BLOCK && event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            BlockState state = event.getClickedBlock().getState();
            if(event.getClickedBlock().getRelative(BlockFace.DOWN).getType() == Material.IRON_DOOR_BLOCK)
                state = event.getClickedBlock().getRelative(BlockFace.DOWN).getState();
            Openable openable = (Openable) state.getData();
            if(openable.isOpen()) {
                openable.setOpen(false);
                event.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.DOOR_OPEN, 1, 1);
            } else {
                openable.setOpen(true);
                event.getPlayer().playSound(event.getClickedBlock().getLocation(), Sound.DOOR_CLOSE, 1, 1);
            }
            state.setData((org.bukkit.material.MaterialData) openable);
            state.update();
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
            newCell.cellSpawn = new Location(nextFree.getWorld(), (nextFree.getX()*16) + 13, 88, (nextFree.getZ()*16) + 12, 135f, 0f);
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
        Cell targetCell = VCPrison.getInstance().getCellManager().getLoadedCells().get(Integer.valueOf(event.getCurrentItem().getItemMeta().getLore().get(1).split("#")[1]));
        if(targetCell == null) {
            return;
        }

        if(targetCell.block && !targetCell.ownerUUID.equals(event.getWhoClicked().getUniqueId()) && !targetCell.additionalUUIDs.contains(event.getWhoClicked().getUniqueId())) {
            Form.at((org.bukkit.entity.Player) event.getWhoClicked(), Prefix.ERROR, "That cell is private. You can't teleport to it.");
            return;
        }

        if(targetCell.cellSpawn.getBlock().getRelative(BlockFace.UP).getType() == Material.AIR) {
            event.getWhoClicked().teleport(targetCell.cellSpawn);
            Form.at((org.bukkit.entity.Player) event.getWhoClicked(), Prefix.SUCCESS, "Teleported to "
                    + Bukkit.getOfflinePlayer(targetCell.ownerUUID).getName() + "'s " + targetCell.name + " cell.");
        } else {
            event.getWhoClicked().teleport(new Location(targetCell.cellSpawn.getWorld(), ((targetCell.chunkX - 1) * 16) + 14, 88, (targetCell.chunkZ * 16) + 4, -90f, 0f));
            Form.at((org.bukkit.entity.Player) event.getWhoClicked(), Prefix.ERROR, targetCell.name
                    + "'s spawn location is obstructed. Teleporting you outside of the cell.");
        }
    }

    @EventHandler
    public void onTrample(PlayerInteractEvent event) {
        if(event.getAction() == Action.PHYSICAL && event.getClickedBlock().getType() == Material.SOIL)
            event.setCancelled(true);
    }
}
