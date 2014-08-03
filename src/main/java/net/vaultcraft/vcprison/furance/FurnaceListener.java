package net.vaultcraft.vcprison.furance;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by tacticalsk8er on 8/2/2014.
 */
public class FurnaceListener implements Listener {

    public FurnaceListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onBlockClick(final PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.BURNING_FURNACE) {
                event.setCancelled(true);
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
            }
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
