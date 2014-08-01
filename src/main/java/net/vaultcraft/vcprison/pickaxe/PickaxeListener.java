package net.vaultcraft.vcprison.pickaxe;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by tacticalsk8er on 8/1/2014.
 */
public class PickaxeListener implements Listener {

    public PickaxeListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null)
            if (event.getCurrentItem().getType() == Material.DIAMOND_PICKAXE)
                event.setCancelled(true);
        if(event.getAction() == InventoryAction.HOTBAR_SWAP) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onHotbarHover(PlayerItemHeldEvent event) {

    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.DIAMOND_PICKAXE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {

        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.DIAMOND_PICKAXE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);
        ItemStack item = new ItemStack(changeType(event.getBlock().getType()));
        PrisonUser user = PrisonUser.fromPlayer(event.getPlayer());
        int amount = 1;
        if (user.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
            user.getPickaxe().mineBlock();
            if (user.getPickaxe().isAUTO_SMELT())
                item.setType(canSmelt(item.getType()));
            if (isFortuneBlock(item.getType()))
                amount = user.getPickaxe().getFortuneItems();
        }
        item.setAmount(amount);
        user.getPlayer().getInventory().addItem(item);
        event.getBlock().setType(Material.AIR);
    }

    public boolean isFortuneBlock(Material type) {
        switch (type) {
            case COAL:
                return true;
            case DIAMOND:
                return true;
            case IRON_ORE:
                return true;
            case IRON_INGOT:
                return true;
            case GOLD_INGOT:
                return true;
            case GOLD_ORE:
                return true;
            case EMERALD:
                return true;
            default:
                return false;
        }
    }

    public Material changeType(Material type) {
        switch (type) {
            case COAL_ORE:
                return Material.COAL;
            case DIAMOND_ORE:
                return Material.DIAMOND;
            case EMERALD_ORE:
                return Material.EMERALD;
            case STONE:
                return Material.COBBLESTONE;
            default:
                return type;
        }
    }

    public Material canSmelt(Material type) {
        switch (type) {
            case IRON_ORE:
                return Material.IRON_INGOT;
            case GOLD_ORE:
                return Material.GOLD_INGOT;
            case COBBLESTONE:
                return Material.STONE;
            default:
                return type;
        }
    }
}
