package net.vaultcraft.vcprison.cells;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tacticalsk8er on 10/29/2014.
 */
public class VCCell extends ICommand {

    public VCCell(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {

        if(args.length == 0) {
            openMenu(player, player);
        }

        switch (args[0].toLowerCase()) {
            case "setspawn":
                executeSetSpawn(player);
                break;
            case "addbuilder":
                executeAddBuilder(player, args);
                break;
            case "removebuilder":
                executeRemoveBuilder(player, args);
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
            default:
                OfflinePlayer player1 = Bukkit.getPlayer(args[0]);
                if(player1 == null) {
                    player1 = Bukkit.getOfflinePlayer(args[0]);
                    if(player1 == null) {
                        Form.at(player, Prefix.ERROR, "No such player! Format: /plot <player>.");
                        return;
                    }
                }

                openMenu(player1.getPlayer(), player);
        }

    }

    public void openMenu(Player plotOwner, Player opener) {
        List<Cell> cells = VCPrison.getInstance().getCellManager().getCellsFromPlayer(plotOwner);
        int rows = (int) Math.ceil(((cells.size() + 1.0) / 9.0));
        Inventory menu = Bukkit.createInventory(null, rows, ChatColor.GREEN + plotOwner.getName() + "'s Cells");
        for(int i = 0; i < cells.size(); i++) {
            menu.setItem(i, getCellItem(cells.get(i)));
        }
        if(plotOwner.equals(opener))
            menu.setItem(cells.size(), getNewCellItem());
        opener.openInventory(menu);
    }

    private ItemStack getNewCellItem() {
        ItemStack itemStack = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Create Cell");
        itemMeta.setLore(Arrays.asList(ChatColor.WHITE + "Creates a new cell, and teleports you to it."));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    private ItemStack getCellItem(Cell cell) {
        ItemStack itemStack = new ItemStack(Material.IRON_FENCE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(cell.name);
        itemMeta.setLore(Arrays.asList(VCPrison.getInstance().getCellManager().locationToString(cell.cellSpawn)));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void executeSetSpawn(Player player) {
        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation());

        if(cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if(!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of the cell!");
            return;
        }

        cell.cellSpawn = player.getLocation();

        Form.at(player, Prefix.SUCCESS, "You have set " + cell.name + " cell spawn to your location.");
    }

    public void executeAddBuilder(Player player, String[] args) {

        if(args.length < 2) {
            Form.at(player, Prefix.ERROR, "Missing arguments! Format: /plot addbuilder <player>");
            return;
        }

        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation());

        if(cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if(!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of the cell!");
            return;
        }

        OfflinePlayer player1 = Bukkit.getPlayer(args[0]);
        if(player1 == null) {
            player1 = Bukkit.getOfflinePlayer(args[0]);
            if(player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player! Format: /plot addbuilder <player>.");
                return;
            }
        }

        if(player.equals(player1.getPlayer())) {
            Form.at(player, Prefix.ERROR, "You can't add yourself as a builder.");
            return;
        }

        if(cell.additionalUUIDs.contains(player1.getUniqueId())) {
            Form.at(player, Prefix.ERROR, player1.getName() + " is already a build in " + cell.name + " cell.");
        }

        cell.additionalUUIDs.add(player1.getUniqueId());
        Form.at(player, Prefix.SUCCESS, player1.getName() + " has been added as a builder to " + cell.name + " cell.");
    }

    public void executeRemoveBuilder(Player player, String[] args) {
        if(args.length < 2) {
            Form.at(player, Prefix.ERROR, "Missing arguments! Format: /plot removebuilder <player>");
            return;
        }

        Cell cell = VCPrison.getInstance().getCellManager().getCellFromLocation(player.getLocation());

        if(cell == null) {
            Form.at(player, Prefix.ERROR, "You need to stand inside a cell you own to use this command.");
            return;
        }

        if(!cell.ownerUUID.equals(player.getUniqueId())) {
            Form.at(player, Prefix.ERROR, "You are not the owner of the cell!");
            return;
        }

        OfflinePlayer player1 = Bukkit.getPlayer(args[0]);
        if(player1 == null) {
            player1 = Bukkit.getOfflinePlayer(args[0]);
            if(player1 == null) {
                Form.at(player, Prefix.ERROR, "No such player! Format: /plot removebuilder <player>.");
                return;
            }
        }

        if(!cell.additionalUUIDs.contains(player1.getUniqueId())) {
            Form.at(player, Prefix.ERROR, player1.getName() + " is not a builder in " + cell.name + " cell.");
            return;
        }

        cell.additionalUUIDs.remove(player1.getUniqueId());
        Form.at(player, Prefix.SUCCESS, player1.getName() + " is no longer a builder in " + cell.name + " cell.");
    }

    public void executeDelete(Player player) {

    }

    public void executeClaim(Player player) {

    }

    public void executeInfo(Player player) {

    }
}
