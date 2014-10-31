package net.vaultcraft.vcprison.cells;

import com.mongodb.DBObject;
import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitUtil;
import com.sk89q.worldedit.data.DataException;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by tacticalsk8er on 10/29/2014.
 */
public class VCCell extends ICommand {

    public VCCell(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {

        if (args.length == 0) {
            new CellMenu(player.getUniqueId(), player);
            return;
        }

        switch (args[0].toLowerCase()) {
            case "help":
                executeHelp(player);
                break;
            case "new":
                executeNew(player);
                break;
            case "rename":
                executeRename(player, args);
                break;
            case "setspawn":
                executeSetSpawn(player);
                break;
            case "add":
                executeAdd(player, args);
                break;
            case "remove":
                executeRemove(player, args);
                break;
            case "delete":
                executeDelete(player);
                break;
            case "claim":
                executeClaim(player);
                break;
            case "info":
                executeInfo(player);
                break;
            case "block":
                executeBlock(player);
                break;
            default:
                OfflinePlayer player1 = Bukkit.getPlayer(args[0]);
                if (player1 == null) {
                    player1 = Bukkit.getOfflinePlayer(args[0]);
                    if (player1 == null || !player1.hasPlayedBefore()) {
                        Form.at(player, Prefix.ERROR, "No such player! Format: /plot <player>.");
                        return;
                    }
                }

                new CellMenu(player1.getUniqueId(), player);
        }

    }

    public void executeHelp(Player player) {
        Form.at(player, Prefix.VAULT_CRAFT, "Commands: /cell new, /cell rename, /cell setspawn, /cell add" +
                ", /cell remove, /cell delete, /cell claim, /cell info");
    }

    public void executeNew(Player player) {

        Chunk chunk = VCPrison.getInstance().getCellManager().getNextOpenCell();

        int ownedCells = VCPrison.getInstance().getCellManager().getCellsFromPlayer(player).size();

        if (ownedCells >= PrisonUser.fromPlayer(player).getPlotLimit()) {
            Form.at(player, Prefix.ERROR, "You have hit the limit on the amount of cells you can have.");
            return;
        }

        Cell cell = new Cell();
        cell.chunkX = chunk.getX();
        cell.chunkZ = chunk.getZ();
        cell.ownerUUID = player.getUniqueId();
        cell.name = "Cell #" + (ownedCells + 1);
        cell.cellSpawn = new Location(chunk.getWorld(), (chunk.getX() * 16) + 13, 88,
                (chunk.getZ() * 16) + 12, 135f, 0f);
        player.teleport(cell.cellSpawn);
        VCPrison.getInstance().getCellManager().addCell(cell);
        Form.at(player, Prefix.SUCCESS, "Teleporting you to your new cell!");
    }

    public void executeRename(Player player, String[] args) {

        if (args.length < 2) {
            Form.at(player, Prefix.ERROR, "Missing Arguments! Format: /cell rename <name>");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }

        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation().getBlock().getLocation());

        if (cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if (!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of this cell!");
            return;
        }

        cell.name = sb.toString();

        Form.at(player, Prefix.SUCCESS, "You have renamed your cell to " + cell.name + ".");
    }

    public void executeSetSpawn(Player player) {
        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation().getBlock().getLocation());

        if (cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if (!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of this cell!");
            return;
        }

        cell.cellSpawn = player.getLocation();

        Form.at(player, Prefix.SUCCESS, "You have set " + cell.name + " cell spawn to your location.");
    }

    public void executeAdd(Player player, String[] args) {

        if (args.length < 2) {
            Form.at(player, Prefix.ERROR, "Missing arguments! Format: /plot add <player>");
            return;
        }

        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation().getBlock().getLocation());

        if (cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if (!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of this cell!");
            return;
        }

        OfflinePlayer player1 = Bukkit.getPlayer(args[1]);
        if (player1 == null) {
            player1 = Bukkit.getOfflinePlayer(args[1]);
            if (player1 == null || !player1.hasPlayedBefore()) {
                Form.at(player, Prefix.ERROR, "No such player! Format: /plot add <player>.");
                return;
            }
        }

        if (player.getUniqueId().equals(player1.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You can't add yourself as a builder.");
            return;
        }

        if (cell.additionalUUIDs.contains(player1.getUniqueId())) {
            Form.at(player, Prefix.ERROR, player1.getName() + " is already a build in " + cell.name + " cell.");
        }

        cell.additionalUUIDs.add(player1.getUniqueId());
        Form.at(player, Prefix.SUCCESS, player1.getName() + " has been added as a builder to " + cell.name + " cell.");
    }

    public void executeRemove(Player player, String[] args) {
        if (args.length < 2) {
            Form.at(player, Prefix.ERROR, "Missing arguments! Format: /plot remove <player>");
            return;
        }

        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation().getBlock().getLocation());

        if (cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if (!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of this cell!");
            return;
        }

        OfflinePlayer player1 = Bukkit.getPlayer(args[1]);
        if (player1 == null) {
            player1 = Bukkit.getOfflinePlayer(args[1]);
            if (player1 == null || !player1.hasPlayedBefore()) {
                Form.at(player, Prefix.ERROR, "No such player! Format: /plot remove <player>.");
                return;
            }
        }

        if (!cell.additionalUUIDs.contains(player1.getUniqueId())) {
            Form.at(player, Prefix.ERROR, player1.getName() + " is not a builder in " + cell.name + " cell.");
            return;
        }

        cell.additionalUUIDs.remove(player1.getUniqueId());
        Form.at(player, Prefix.SUCCESS, player1.getName() + " is no longer a builder in " + cell.name + " cell.");
    }

    public void executeDelete(Player player) {

        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation().getBlock().getLocation());

        if (cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if (!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of this cell!");
            return;
        }

        player.teleport(new Location(player.getWorld(), ((cell.chunkX - 1) * 16) + 14, 88, (cell.chunkZ * 16) + 4, -90f, 0f));

        CuboidClipboard cells;

        DBObject dbObject = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Cells", "Chunk", cell.chunkX + "," + cell.chunkZ);
        if(dbObject != null)
            VCUtils.getInstance().getMongoDB().getClient().getDB(VCUtils.mongoDBName).getCollection("Cells").remove(dbObject);

        try {
            cells = CuboidClipboard.loadSchematic(new File(VCPrison.getInstance().getDataFolder(), "cells.schematic"));
        } catch (DataException | IOException e) {
            e.printStackTrace();
            Form.at(player, Prefix.ERROR, "Something happened when trying to delete your cell. Please notify a staff member.");
            return;
        }


        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(BukkitUtil.getLocalWorld(VCPrison.getInstance().getCellManager().getPlotWorld()), -1);
        try {
            cells.rotate2D(90);
            cells.paste(editSession, new Vector((cell.chunkX * 16) + 15, 84, (cell.chunkZ * 16) + 15), false);
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
            Form.at(player, Prefix.ERROR, "Something happened when trying to delete your cell. Please notify a staff member.");
            return;
        }

        VCPrison.getInstance().getCellManager().removeCell(cell);
        Form.at(player, Prefix.SUCCESS, "Your cell have been removed.");
    }

    public void executeClaim(Player player) {

        if (player.getLocation().getChunk().getX() % 2 != 0) {
            Form.at(player, Prefix.ERROR, "Please stand inside the cell you want to claim.");
            return;
        }

        if (VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation().getBlock().getLocation()) != null) {
            Form.at(player, Prefix.ERROR, "This cell has already been claimed!");
            return;
        }

        int ownedCells = VCPrison.getInstance().getCellManager().getCellsFromPlayer(player).size();

        if (ownedCells >= PrisonUser.fromPlayer(player).getPlotLimit()) {
            Form.at(player, Prefix.ERROR, "You have hit the limit on the amount of cells you can have.");
            return;
        }

        Cell cell = new Cell();
        cell.chunkX = player.getLocation().getChunk().getX();
        cell.chunkZ = player.getLocation().getChunk().getZ();
        cell.ownerUUID = player.getUniqueId();
        cell.name = "Cell #" + (ownedCells + 1);
        cell.cellSpawn = new Location(player.getWorld(), (player.getLocation().getChunk().getX() * 16) + 13, 88,
                (player.getLocation().getChunk().getZ() * 16) + 12, 135f, 0f);
        VCPrison.getInstance().getCellManager().addCell(cell);
        Form.at(player, Prefix.SUCCESS, "You have claimed this cell.");
    }

    public void executeInfo(Player player) {

        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation().getBlock().getLocation());

        if (cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a claimed cell to use this command.");
            return;
        }

        String cellOwnerName = Bukkit.getOfflinePlayer(cell.ownerUUID).getName();
        player.sendMessage(ChatColor.GREEN + "===" + ChatColor.WHITE.toString() +
                ChatColor.BOLD + cellOwnerName + "'s Cell" + ChatColor.GREEN + "===");
        player.sendMessage(ChatColor.GOLD + "Name: " + cell.name);
        player.sendMessage(ChatColor.RED + "Spawn Location: " + VCPrison.getInstance().getCellManager().locationToString(cell.cellSpawn));
        StringBuilder sb = new StringBuilder();
        for (UUID uuid : cell.additionalUUIDs) {
            OfflinePlayer player1 = Bukkit.getOfflinePlayer(uuid);
            if (player1.isOnline())
                sb.append(ChatColor.GREEN).append(player1.getName()).append(", ");
            else
                sb.append(ChatColor.RED).append(player1.getName()).append(", ");
        }
        player.sendMessage(ChatColor.BLUE + "Builders: " + sb.toString());
        player.sendMessage("");
    }

    public void executeBlock(Player player) {
        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation().getBlock().getLocation());

        if (cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if (!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of this cell!");
            return;
        }

        if(cell.block) {
            cell.block = false;
            Form.at(player, Prefix.SUCCESS, "You have opened your cell to the public. Anyone can teleport to it.");
        } else {
            cell.block = true;
            Form.at(player, Prefix.SUCCESS, "You have closed your cell. Only you and your builders can teleport to it.");
        }
    }
}
