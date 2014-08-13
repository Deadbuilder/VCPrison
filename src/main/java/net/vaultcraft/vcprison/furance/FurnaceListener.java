package net.vaultcraft.vcprison.furance;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;

/**
 * Created by tacticalsk8er on 8/2/2014.
 */
public class FurnaceListener implements Listener {

    public HashMap<String, BukkitTask> smelting = new HashMap<>();

    public FurnaceListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onBlockClick(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.BURNING_FURNACE) {
                event.setCancelled(true);
                if(smelting.containsKey(event.getPlayer().getName())) {
                    Form.at(event.getPlayer(), Prefix.ERROR, "You are currently smelting items right now.");
                    return;
                }
                int blocks = 0;
                for(ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
                    if(itemStack == null)
                        continue;
                    if(isSmeltable(itemStack.getType()))
                        blocks += itemStack.getAmount();
                }
                if(blocks == 0) {
                    Form.at(event.getPlayer(), Prefix.ERROR, "You have no blocks that need to be smelted!");
                    return;
                }
                int ticks = blocks/4;
                Form.at(event.getPlayer(), Prefix.VAULT_CRAFT, "Smelting your items please wait... (" + ticks/20.0 + " seconds)");
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.FIRE, 1, 1);
                BukkitTask task;
                task = Bukkit.getScheduler().runTaskLater(VCPrison.getInstance(), new Runnable() {
                    @Override
                    public void run() {
                        for(ItemStack itemStack : event.getPlayer().getInventory().getContents()) {
                            if(itemStack == null)
                                continue;
                            if(isSmeltable(itemStack.getType())) {
                                event.getPlayer().getInventory().remove(itemStack);
                                event.getPlayer().getInventory().addItem(new ItemStack(smelt(itemStack.getType()), itemStack.getAmount()));
                                event.getPlayer().updateInventory();
                            }
                        }
                        Form.at(event.getPlayer(), Prefix.SUCCESS, "All smeltable items have been smelted.");
                        smelting.remove(event.getPlayer().getName());
                    }
                }, ticks);
                smelting.put(event.getPlayer().getName(), task);
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onInvClick(InventoryClickEvent event) {
        if(smelting.containsKey(event.getWhoClicked().getName())) {
            event.setCancelled(true);
            Form.at((org.bukkit.entity.Player) event.getWhoClicked(), Prefix.ERROR, "Please wait you are smelting items.");
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onItemDrop(PlayerDropItemEvent event) {
        if(smelting.containsKey(event.getPlayer().getName())) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.ERROR, "Please wait you are smelting items.");
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onItemDrop(BlockBreakEvent event) {
        if(smelting.containsKey(event.getPlayer().getName())) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.ERROR, "Please wait you are smelting items.");
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onItemDrop(BlockPlaceEvent event) {
        if(smelting.containsKey(event.getPlayer().getName())) {
            event.setCancelled(true);
            Form.at(event.getPlayer(), Prefix.ERROR, "Please wait you are smelting items.");
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(smelting.containsKey(event.getPlayer().getName())) {
            smelting.get(event.getPlayer().getName()).cancel();
            smelting.remove(event.getPlayer().getName());
        }
    }

    private boolean isSmeltable(Material type) {
        switch (type) {
            case GOLD_ORE:
                return true;
            case IRON_ORE:
                return true;
            case COBBLESTONE:
                return true;
            default:
                return false;
        }
    }

    private Material smelt(Material type) {
        switch (type) {
            case GOLD_ORE:
                return Material.GOLD_INGOT;
            case IRON_ORE:
                return Material.IRON_INGOT;
            case COBBLESTONE:
                return Material.STONE;
            default:
                return type;
        }
    }
}
