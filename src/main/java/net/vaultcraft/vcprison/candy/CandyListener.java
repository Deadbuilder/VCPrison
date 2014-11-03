package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public class CandyListener implements Listener {

    private static HashMap<String, HashMap<Candy, Integer>> playerCandyMap = new HashMap<>();

    public CandyListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());

        //Recipe for wrappers
        ShapedRecipe wrapper = new ShapedRecipe(CandyItems.WRAPPER);
        wrapper.shape("XXX", "XYX", "XXX");
        wrapper.setIngredient('X', Material.INK_SACK, 8);
        wrapper.setIngredient('Y', Material.SNOW_BLOCK);
        Bukkit.addRecipe(wrapper);

        //Recipe for butter
        ShapedRecipe butter = new ShapedRecipe(CandyItems.BUTTER);
        butter.shape("XYX", "YXY", "XYX");
        butter.setIngredient('X', Material.WHEAT);
        butter.setIngredient('Y', Material.GLOWSTONE_DUST);
        Bukkit.addRecipe(butter);

        //Recipe for CoCoa
        ShapedRecipe cocoa = new ShapedRecipe(CandyItems.COCOA);
        cocoa.shape("xyx", "yxy", "xyx");
        cocoa.setIngredient('x', Material.COCOA);
        cocoa.setIngredient('y', Material.SUGAR);
        Bukkit.addRecipe(cocoa);

        //Recipe for jawbreaker
        ShapelessRecipe snowball = new ShapelessRecipe(CandyItems.JAWBREAKER);
        snowball.addIngredient(4, Material.SUGAR);
        Bukkit.addRecipe(snowball);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        ItemStack holding = event.getItem();
        Player player = event.getPlayer();
        String name = player.getName();
        Candy from = CandyManager.getCandy(holding);
        if (from == null)
            return;

        boolean harmful = false;
        if(holding.getItemMeta() != null)
            if(holding.getItemMeta().getLore() != null)
                if(holding.getItemMeta().getLore().contains("harmful"))
                    harmful = true;

        int amount = 1;
        if(playerCandyMap.containsKey(name)) {
            HashMap<Candy, Integer> candyMap = playerCandyMap.remove(name);
            if(candyMap.containsKey(from)) {
                amount = candyMap.remove(from);
                if(amount >= from.getHarmfulAfter())
                    harmful = true;
                amount++;
                candyMap.put(from, amount);
                playerCandyMap.put(name, candyMap);
            } else {
                candyMap.put(from, amount);
                playerCandyMap.put(name, candyMap);
            }
        } else {
            HashMap<Candy, Integer> candyMap = new HashMap<>();
            candyMap.put(from, amount);
            playerCandyMap.put(name, candyMap);
        }

        ItemStack drop = from.onCandyConsume(player, harmful);
        player.playSound(player.getLocation(), Sound.EAT, 1, 1);
        player.playEffect(player.getLocation(), Effect.STEP_SOUND, holding.getTypeId());

        final int origin = amount;
        Runnable runnable = () -> {
            if(!playerCandyMap.containsKey(name) || playerCandyMap.get(name) == null)
                return;
            HashMap<Candy, Integer> candyMap = playerCandyMap.remove(name);
            if(!candyMap.containsKey(from) || candyMap.get(from) == null) {
                playerCandyMap.put(name, candyMap);
                return;
            }

            int amount1 = candyMap.remove(from);
            if(amount1 != origin) {
                candyMap.put(from, amount1);
                playerCandyMap.put(name, candyMap);
                return;
            }

            playerCandyMap.put(name, candyMap);
        };

        Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), runnable, from.getCooldown() * 20);

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

    @EventHandler
    public void onCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        Candy candy = null;
        for(int i = 0; i < inventory.getContents().length; i++) {
            if(i <= 3 || i >= 8) {
                if(inventory.getContents()[i].getType() != Material.SNOW_BALL)
                    return;
                continue;
            }
            if(CandyManager.getCandy(inventory.getContents()[i]) != null) {
                candy = CandyManager.getCandy(inventory.getContents()[i]);
                continue;
            }
            return;
        }

        if(candy == null)
            return;

        ItemStack itemStack = candy.getCandyItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.add("Harmful");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        inventory.setResult(itemStack);
    }
}
