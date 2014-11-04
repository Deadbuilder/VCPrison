package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.*;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
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
        cocoa.setIngredient('x', Material.INK_SACK.getNewData((byte)3));
        cocoa.setIngredient('y', Material.SUGAR);
        Bukkit.addRecipe(cocoa);

        //Recipe for Rubber from Used Wrapper
        ItemStack rubberItem = CandyItems.RUBBER.clone();
        rubberItem.setAmount(4);
        FurnaceRecipe rubber = new FurnaceRecipe(rubberItem, Material.INK_SACK, 7);
        Bukkit.addRecipe(rubber);
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
                if(holding.getItemMeta().getLore().contains("Harmful"))
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

        if(!from.getCandyItem().getType().equals(Material.SNOW_BALL))
            event.setCancelled(true);

        if (player.getItemInHand().getAmount() <= 1) {
            player.getInventory().removeItem(player.getItemInHand());
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
        Logger.debug(VCPrison.getInstance(), "PICE Called: " + inventory.getContents().length);
        for(int i = 1; i < inventory.getContents().length; i++) {
            if(i <= 4 || i >= 6) {
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

        ItemStack itemStack = candy.getCandyItem().clone();
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> lore = itemMeta.getLore();
        lore.add("Harmful");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        inventory.setResult(itemStack);
    }
}
