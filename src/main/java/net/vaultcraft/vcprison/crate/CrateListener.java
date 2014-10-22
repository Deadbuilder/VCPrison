package net.vaultcraft.vcprison.crate;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Connor Hollasch on 9/26/14.
 */

public class CrateListener implements Listener {

    public CrateListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    public static List<Location> crates = Lists.newArrayList();

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        if (MineCrateInjector.getInjected().contains(block.getLocation())) {
            MineCrateInjector.getInjected().remove(block.getLocation());
            Player player = event.getPlayer();

            if (!(block.getState() instanceof Chest))
                return;

            player.playSound(block.getLocation(), Sound.ITEM_BREAK, 1, 0);
            Chest chest = (Chest)event.getBlock().getState();
            for (ItemStack stack : chest.getInventory().getContents()) {
                if (stack == null)
                    continue;

                if (player.getInventory().firstEmpty() == -1) {
                    player.getWorld().dropItem(chest.getLocation(), stack);
                    Form.at(player, Prefix.WARNING, "Your inventory was full so you didn't get crate items!");
                } else {
                    player.getInventory().addItem(stack);
                    Form.at(player, Prefix.SUCCESS, "You found a loot crate!");
                }
            }
            player.updateInventory();
            chest.getInventory().clear();
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Chest) {
            Chest chest = (Chest)event.getInventory().getHolder();
            if (crates.contains(chest.getLocation())) {
                crates.remove(chest.getLocation());
                onBlockBreak(new BlockBreakEvent(chest.getBlock(), (Player) event.getPlayer()));
                ((Player) event.getPlayer()).playEffect(chest.getLocation(), Effect.STEP_SOUND, Material.CHEST.getId());
                chest.getBlock().setType(Material.AIR);
            }
        }
    }
}
