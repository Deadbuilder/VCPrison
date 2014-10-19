package net.vaultcraft.vcprison.shop;

import net.citizensnpcs.api.event.NPCLeftClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.lang.model.element.Name;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 10/18/2014.
 */
public class PrisonShopListener implements Listener {

    private static List<Player> inShop = new ArrayList<>();
    private final ArrayList<ShopItem> items = new ArrayList<ShopItem>() {{
        add(new ShopItem(Material.LEATHER_HELMET, 1000, "", "Leather Helmet", 1));
        add(new ShopItem(Material.LEATHER_CHESTPLATE, 1500, "", "Leather Chestplate", 1));
        add(new ShopItem(Material.LEATHER_LEGGINGS, 1500, "", "Leather Leggings", 1));
        add(new ShopItem(Material.LEATHER_BOOTS, 1000, "", "Leather Boots", 1));
        add(new ShopItem(Material.IRON_HELMET, 3000, "", "Iron Helmet", 1));
        add(new ShopItem(Material.IRON_CHESTPLATE, 4500, "", "Iron Chestplate", 1));
        add(new ShopItem(Material.IRON_LEGGINGS, 4500, "", "Iron Leggings", 1));
        add(new ShopItem(Material.IRON_BOOTS, 3000, "", "Iron Boots", 1));
        add(new ShopItem(Material.DIAMOND_HELMET, 6000, "", "Diamond Helmet", 1));
        add(new ShopItem(Material.DIAMOND_CHESTPLATE, 8000, "", "Diamond Chestplate", 1));
        add(new ShopItem(Material.DIAMOND_LEGGINGS, 8000, "", "Diamond Leggings", 1));
        add(new ShopItem(Material.DIAMOND_BOOTS, 6000, "", "Diamond Boots", 1));
        add(new ShopItem(Material.BED, 3000, "", "Bed", 1));
        add(new ShopItem(Material.ENDER_CHEST, 50000, "", "Ender Chest", 1));
        add(new ShopItem(Material.CHEST, 5000, "", "Chest", 1));
        add(new ShopItem(Material.SMOOTH_BRICK, 4000, "", "Stone Bricks", 64));
    }};


    public PrisonShopListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    private class ShopItem {
        private Material itemType;
        private double price;
        private String description;
        private String name;
        private int quantity;

        public ShopItem(Material itemType, double price, String description, String name, int quantity) {
            this.itemType = itemType;
            this.price = price;
            this.description = description;
            this.name = name;
            this.quantity = quantity;
        }

        public ItemStack getItemStack() {
            ItemStack itemStack = new ItemStack(itemType);
            ItemMeta itemMeta = itemStack.getItemMeta();
            if(!name.equals(""))
                itemMeta.setDisplayName(name);
            ArrayList<String> itemLore = new ArrayList<>();
            itemLore.add("Price: $" + price);
            if(!description.equals("")) {
                itemLore.add(description);
            }
            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }

    @EventHandler
    public void onEntInteract(PlayerInteractEntityEvent event) {
        if(event.getRightClicked().getType() == EntityType.WITCH && !inShop.contains(event.getPlayer())) {
            Inventory inv = Bukkit.createInventory(null, 9*(items.size()/9), "Prison Shop");
            for(ShopItem si : items) {
                inv.addItem(si.getItemStack());
            }
            inShop.add(event.getPlayer());
            event.getPlayer().openInventory(inv);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if(inShop.contains(event.getPlayer())) {
            inShop.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player && inShop.contains(event.getWhoClicked())) {
            int clickedSlot = event.getSlot();
            if(clickedSlot > items.size()) {
                return;
            }
            ShopItem item = items.get(clickedSlot);
            User user = User.fromPlayer((Player) event.getWhoClicked());
            if(user.getMoney() < item.price) {
                Form.at((Player) event.getWhoClicked(), Prefix.ERROR, "You do not have enough money to purchase this item!");
                event.getWhoClicked().closeInventory();
                return;
            }

            event.getWhoClicked().getInventory().addItem(new ItemStack(item.itemType, item.quantity));
            user.setMoney(user.getMoney() - item.price);
            Form.at((Player) event.getWhoClicked(), Prefix.SUCCESS, "You bought a "+item.name+" for $"+String.valueOf(item.price)+"!");
        }
    }
}
