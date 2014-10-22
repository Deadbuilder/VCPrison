package net.vaultcraft.vcprison.shop;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sean on 10/18/2014.
 */
public class PrisonShopListener implements Listener {

    private static List<Player> inShop = new ArrayList<>();
    private final ArrayList<ShopItem> items = new ArrayList<ShopItem>() {{
        add(new ShopItem(Material.SMOOTH_BRICK, 3000, "x16", "Stone Brick", 16));
        add(new ShopItem(Material.TORCH, 50000, "x4", "Torch", 4));
        add(new ShopItem(Material.ICE, 5000, "x2", "Ice", 2));
        add(new ShopItem(Material.IRON_FENCE, 4000, "x8", "Iron Bars", 8));
        add(new ShopItem(Material.ENCHANTMENT_TABLE, 4000, "x1", "Enchantment Table", 1));
        add(new ShopItem(Material.BED, 20000, "x1", "Bed", 1));
        add(new ShopItem(Material.ENDER_CHEST, 50000, "x1", "Ender Chest", 1));
        add(new ShopItem(Material.CHEST, 6000, "x2", "Chest", 2));
        add(new ShopItem(Material.GLOWSTONE, 8000, "x8", "Glowstone", 8));
        add(new ShopItem(Material.BOOK_AND_QUILL, 10000, "x1", "Book and Quill", 1));
        add(new ShopItem(Material.DIRT, 16000, "x16", "Dirt", 16));
        add(new ShopItem(Material.SAND, 16000, "x16", "Sand", 16));
        add(new ShopItem(Material.DIAMOND_HOE, 10000, "x1", "Diamond Hoe", 1));
        add(new ShopItem(Material.SEEDS, 4000, "x2", "Seeds", 2));
        add(new ShopItem(Material.LOG, 3000, "x8", "Oak Wood", 16));
        add(new ShopItem(Material.BOOK, 2000, "x1", "Book", 1));
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
            Inventory inv = Bukkit.createInventory(null,  27, "VaultCraft Cells Shop");
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
            event.setCancelled(true);
            if(event.getSlot() != event.getRawSlot()) { //FIXME: This is kinda hackish
                return;
            }
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

            if (event.getWhoClicked().getInventory().firstEmpty() == -1) {
                Form.at((Player)event.getWhoClicked(), Prefix.ERROR, "You have no empty slots in your inventory!");
                return;
            }

            event.getWhoClicked().getInventory().addItem(new ItemStack(item.itemType, item.quantity));
            user.setMoney(user.getMoney() - item.price);
            Form.at((Player) event.getWhoClicked(), Prefix.SUCCESS, "You bought a "+item.name+" for $"+String.valueOf(item.price)+"!");

            ((Player) event.getWhoClicked()).updateInventory();
        }
    }
}
