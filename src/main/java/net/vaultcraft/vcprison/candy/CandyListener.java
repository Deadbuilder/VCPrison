package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public class CandyListener implements Listener {

    public CandyListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());

        //Recipe for Wrapper
        ItemStack itemStack = new ItemStack(Material.QUARTZ);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BOLD + "Candy Wrapper");
        itemMeta.setLore(Arrays.asList("Used to make candy."));
        itemStack.setItemMeta(itemMeta);
        ShapedRecipe shapedRecipe = new ShapedRecipe(itemStack);
        shapedRecipe.shape("xxx", "xyx", "xxx");
        shapedRecipe.setIngredient('x', Material.INK_SACK, 8);
        shapedRecipe.setIngredient('y', Material.SNOW_BLOCK);
        Bukkit.getServer().addRecipe(shapedRecipe);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack holding = event.getItem();
        Player player = event.getPlayer();
        Candy from = CandyManager.getCandy(holding);
        if (from == null)
            return;

        ItemStack drop = from.onCandyConsume(player);
        player.playSound(player.getLocation(), Sound.EAT, 1, 1);
        player.playEffect(player.getLocation(), Effect.STEP_SOUND, holding.getTypeId());

        if (player.getItemInHand().getAmount() == 1) {
            player.getInventory().remove(player.getItemInHand());
        } else {
            holding.setAmount(holding.getAmount()-1);
            player.setItemInHand(holding);
        }

        if (drop != null) {
            if (player.getInventory().firstEmpty() == -1) {
                Item iDrop = player.getWorld().dropItem(player.getLocation(), drop.clone());
                iDrop.setPickupDelay(20);
            } else {
                player.getInventory().addItem(drop.clone());
            }
        }

        player.updateInventory();
    }
}
