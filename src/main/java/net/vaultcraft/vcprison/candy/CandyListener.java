package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public class CandyListener implements Listener {

    public CandyListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
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
        player.playEffect(player.getLocation(), Effect.POTION_BREAK, holding.getTypeId());

        if (drop != null) {
            if (player.getInventory().firstEmpty() == -1) {
                Item iDrop = player.getWorld().dropItem(player.getLocation(), drop.clone());
                iDrop.setPickupDelay(20);
            } else {
                player.getInventory().addItem(drop.clone());
            }
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        ItemStack stack = event.getItem().getItemStack();
        stack.setAmount(1);

        if (Gum.chewed.equals(stack)) {
            event.setCancelled(true);
            player.addPotionEffect(PotionEffectType.SLOW.createEffect(20 * 10, 1));
            player.playSound(player.getLocation(), Sound.DIG_WOOL, 1, 0);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&l"));
        }
    }
}
