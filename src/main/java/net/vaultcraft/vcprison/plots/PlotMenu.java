package net.vaultcraft.vcprison.plots;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 9/17/2014.
 */
public class PlotMenu implements Listener {

    public PlotMenu() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    public void open(OfflinePlayer player, Player openPlayer) {
        List<Plot> plots = PlotWorld.getPlotManager().getPlayerPlots(player);
        Inventory inventory = null;

        if (player.isOnline()) {
            if (player.getPlayer().equals(openPlayer)) {
                int rows = (int) Math.ceil(((plots.size() + 1.0) / 9.0));
                inventory = Bukkit.createInventory(null, rows, ChatColor.GREEN + "Your Cells");
                inventory.setItem(plots.size(), addPlotItem());
            }
        }

        if (inventory == null) {
            if (plots.size() == 0) {
                Form.at(openPlayer, Prefix.ERROR, player.getName() + " does not have any cells.");
                return;
            }
            int rows = (int) Math.ceil(plots.size() / 9.0);
            inventory = Bukkit.createInventory(null, rows, ChatColor.GOLD + player.getName() + "'s Cells");
        }

        for(int i = 1; i <= plots.size(); i++) {
            Plot plot = plots.get(i - 1);
            inventory.setItem(i - 1, plotItem(plot, i));
        }

        openPlayer.openInventory(inventory);
    }

    private ItemStack plotItem(Plot plot, int i) {
        ItemStack itemStack = new ItemStack(Material.IRON_FENCE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Cell " + i);
        List<String> lore = new ArrayList<>();
        lore.add("Location: " + plot.getPlotSpawn().getBlockX() + "," + plot.getPlotSpawn().getBlockY() + "," + plot.getPlotSpawn().getBlockZ());
        itemMeta.setLore(lore);
        return itemStack;
    }

    private ItemStack addPlotItem() {
        ItemStack itemStack = new ItemStack(Material.WOOL, 1, (short)5);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.GREEN + "Get a new cell.");
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inventory = e.getInventory();
        ItemStack itemStack = e.getCurrentItem();

        if(!inventory.getName().contains("Cell"))
            return;

        e.setCancelled(true);

        if(itemStack == null)
            return;

        if(itemStack.getItemMeta() == null)
            return;

        if(itemStack.getItemMeta().getDisplayName() == null)
            return;

        String name  = itemStack.getItemMeta().getDisplayName();

        if(name.contains("Cell")) {
            teleportToPlot(itemStack, (Player) e.getWhoClicked());
            return;
        }

        if(name.equalsIgnoreCase(ChatColor.GREEN + "Get a new cell")) {
            addNewPlot((Player) e.getWhoClicked());
        }
    }

    public void teleportToPlot(ItemStack itemStack, Player player) {
        String location = itemStack.getItemMeta().getLore().get(0).replace("Location: ", "");
        String[] parts = location.split(",");
        player.teleport(new Location(PlotWorld.getPlotWorld(), Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2])));
    }

    public void addNewPlot(Player player) {
        List<Plot> plots = PlotWorld.getPlotManager().getPlayerPlots(player);
        if(plots.size() >= PrisonUser.fromPlayer(player).getPlotLimit()) {
            Form.at(player, Prefix.ERROR, "You have hit the max cells you can have.");
            return;
        }
        Plot plot = PlotWorld.getPlotManager().getAvailablePlot();
        plot.setOwnerUUID(player.getUniqueId().toString());
        player.teleport(plot.getPlotSpawn());
        Form.at(player, Prefix.SUCCESS, "You have been teleported to your new cell.");
    }

}
