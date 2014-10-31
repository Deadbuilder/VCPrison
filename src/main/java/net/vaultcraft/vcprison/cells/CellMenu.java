package net.vaultcraft.vcprison.cells;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * Created by tacticalsk8er on 10/29/2014.
 */
public class CellMenu implements InventoryHolder {

    private Inventory inv;

    public CellMenu(UUID plotOwner, Player opener) {
        List<Cell> cells = VCPrison.getInstance().getCellManager().getCellsFromUUID(plotOwner);
        int rows = (int) Math.ceil(((cells.size() + 1.0) / 9.0));
        inv = Bukkit.createInventory(this, rows * 9, ChatColor.GREEN + Bukkit.getOfflinePlayer(plotOwner).getName() + "'s Cells");
        for(int i = 0; i < cells.size(); i++) {
            inv.setItem(i, getCellItem(cells.get(i)));
        }
        if(plotOwner.equals(opener.getUniqueId()))
            inv.setItem(cells.size(), getNewCellItem());
        else if(cells.size() == 0) {
            Form.at(opener, Prefix.ERROR, Bukkit.getOfflinePlayer(plotOwner).getName() + " doesn't have any cells.");
            return;
        }
        opener.openInventory(inv);
    }

    @Override
    public Inventory getInventory() {
        return inv;
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
        int cellIndex = VCPrison.getInstance().getCellManager().getLoadedCells().indexOf(cell);
        itemMeta.setLore(Arrays.asList("Cell", "CellID #" + cellIndex));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
