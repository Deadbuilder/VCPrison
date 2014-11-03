package net.vaultcraft.vcprison.shop;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.candy.CandyItems;
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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class CandyManListener implements Listener {

    private static List<Player> inShop = new ArrayList<>();
    private final HashMap<Integer, ShopItem> items = new HashMap<Integer, ShopItem>() {{
        put(2, new ShopItem(CandyItems.RUBBER, 4000, "Quantity: 8", 8));
        put(3, new ShopItem(CandyItems.PINKDYE, 4000, "Quantity: 4", 4));
        put(4, new ShopItem(CandyItems.REDDYE, 8000, "Quantity: 2", 2));
        put(5, new ShopItem(CandyItems.NETHERWART, 16000, "Quantity: 2", 2));
        put(6, new ShopItem(CandyItems.NETHERSTAR, 1000000000000d, "Quantity: 1", 1));
    }};

    private final HashMap<Integer, RecipeMenu> recipes = new HashMap<Integer, RecipeMenu>() {{
        put(9, new RecipeMenu(CandyItems.SUGARCUBE, new ArrayList<>(Arrays.asList(new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR),
                new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR),
                new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR), new ItemStack(Material.SUGAR))), false));
        put(10, new RecipeMenu(CandyItems.WRAPPER, new ArrayList<>(Arrays.asList(CandyItems.RUBBER, CandyItems.RUBBER, CandyItems.RUBBER,
                CandyItems.RUBBER, CandyItems.SUGARCUBE, CandyItems.RUBBER,
                CandyItems.RUBBER, CandyItems.RUBBER, CandyItems.RUBBER)), false));
        put(11, new RecipeMenu(CandyItems.BUTTER, new ArrayList<>(Arrays.asList(new ItemStack(Material.WHEAT), new ItemStack(Material.GLOWSTONE_DUST), new ItemStack(Material.WHEAT),
                new ItemStack(Material.GLOWSTONE_DUST), new ItemStack(Material.WHEAT), new ItemStack(Material.GLOWSTONE_DUST),
                new ItemStack(Material.WHEAT), new ItemStack(Material.GLOWSTONE_DUST), new ItemStack(Material.WHEAT))), false));
        put(12, new RecipeMenu(CandyItems.COCOA, new ArrayList<>(Arrays.asList(new ItemStack(Material.INK_SACK, 1, (short) 3), new ItemStack(Material.SUGAR), new ItemStack(Material.INK_SACK, 1, (short) 3),
                new ItemStack(Material.SUGAR), new ItemStack(Material.INK_SACK, 1, (short) 3), new ItemStack(Material.SUGAR),
                new ItemStack(Material.INK_SACK, 1, (short) 3), new ItemStack(Material.SUGAR), new ItemStack(Material.INK_SACK, 1, (short) 3))), false));
        put(13, new RecipeMenu(CandyItems.RUBBER, new ArrayList<>(Arrays.asList(CandyItems.USEDWRAPPER)), true));
        put(14, new RecipeMenu(CandyItems.WRAPPER, new ArrayList<>(Arrays.asList(CandyItems.RUBBER, CandyItems.RUBBER, CandyItems.RUBBER,
                CandyItems.RUBBER, CandyItems.SUGARCUBE, CandyItems.RUBBER,
                CandyItems.RUBBER, CandyItems.RUBBER, CandyItems.RUBBER)), false));
        put(15, new RecipeMenu(CandyItems.GUM, new ArrayList<>(Arrays.asList(CandyItems.WRAPPER, CandyItems.PINKDYE, CandyItems.WRAPPER,
                CandyItems.RUBBER, CandyItems.SUGARCUBE, CandyItems.RUBBER,
                CandyItems.WRAPPER, CandyItems.PINKDYE, CandyItems.WRAPPER)), false));
        put(16, new RecipeMenu(CandyItems.JAWBREAKER, new ArrayList<>(Arrays.asList(CandyItems.SUGARCUBE, CandyItems.SUGARCUBE, CandyItems.SUGARCUBE,
                CandyItems.SUGARCUBE)), false));
        put(17, new RecipeMenu(CandyItems.GUM, new ArrayList<>(Arrays.asList(CandyItems.WRAPPER, new ItemStack(Material.INK_SACK, 1, (short) 2), CandyItems.WRAPPER,
                new ItemStack(Material.INK_SACK, 1, (short) 2), CandyItems.SUGARCUBE, new ItemStack(Material.INK_SACK, 1, (short) 2),
                CandyItems.WRAPPER, new ItemStack(Material.INK_SACK, 1, (short) 2), CandyItems.WRAPPER)), false));
        put(19, new RecipeMenu(CandyItems.WARHEAD, new ArrayList<>(Arrays.asList(CandyItems.WRAPPER, new ItemStack(Material.MELON), CandyItems.WRAPPER,
                new ItemStack(Material.MELON), CandyItems.SUGARCUBE, new ItemStack(Material.MELON),
                CandyItems.WRAPPER, new ItemStack(Material.MELON), CandyItems.WRAPPER)), false));
        put(20, new RecipeMenu(CandyItems.SWEDISHFISH, new ArrayList<>(Arrays.asList(CandyItems.WRAPPER, CandyItems.NETHERWART, CandyItems.WRAPPER,
                CandyItems.REDDYE, CandyItems.SUGARCUBE, CandyItems.REDDYE,
                CandyItems.WRAPPER, CandyItems.NETHERWART, CandyItems.WRAPPER)), false));
        put(21, new RecipeMenu(CandyItems.CHOCOLATEBAR, new ArrayList<>(Arrays.asList(CandyItems.WRAPPER, new ItemStack(Material.INK_SACK, 1, (short) 3), CandyItems.WRAPPER,
                CandyItems.COCOA, CandyItems.SUGARCUBE, CandyItems.COCOA,
                CandyItems.WRAPPER, new ItemStack(Material.INK_SACK, 1, (short) 3), CandyItems.WRAPPER)), false));
        put(22, new RecipeMenu(CandyItems.SWEDISHFISH, new ArrayList<>(Arrays.asList(CandyItems.WRAPPER, CandyItems.NETHERWART, CandyItems.WRAPPER,
                CandyItems.REDDYE, CandyItems.SUGARCUBE, CandyItems.REDDYE,
                CandyItems.WRAPPER, CandyItems.NETHERWART, CandyItems.WRAPPER)), false));
        put(23, new RecipeMenu(CandyItems.BUTTERSCOTCH, new ArrayList<>(Arrays.asList(CandyItems.WRAPPER, CandyItems.BUTTER, CandyItems.WRAPPER,
                CandyItems.BUTTER, CandyItems.SUGARCUBE, CandyItems.BUTTER,
                CandyItems.WRAPPER, CandyItems.BUTTER, CandyItems.WRAPPER)), false));
        put(24, new RecipeMenu(CandyItems.CANDYAPPLE, new ArrayList<>(Arrays.asList(CandyItems.REDDYE, CandyItems.NETHERSTAR, CandyItems.REDDYE,
                CandyItems.REDDYE, CandyItems.SUGARCUBE, CandyItems.REDDYE,
                CandyItems.REDDYE, CandyItems.REDDYE, CandyItems.REDDYE)), false));
        put(25, new RecipeMenu(CandyItems.COOKIE, new ArrayList<>(Arrays.asList(CandyItems.BUTTER, CandyItems.COCOA, CandyItems.NETHERSTAR,
                CandyItems.COCOA, CandyItems.SUGARCUBE, CandyItems.COCOA,
                CandyItems.NETHERSTAR, CandyItems.COCOA, CandyItems.BUTTER)), false));
    }};


    public CandyManListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    private class RecipeMenu {

        private ItemStack result;
        private List<ItemStack> ingredients;
        private boolean furance;

        public RecipeMenu(ItemStack result, List<ItemStack> ingredients, boolean furance) {
            this.result = result;
            this.ingredients = ingredients;
            this.furance = furance;
        }

        public Inventory getInv() {
            if (furance) {
                Inventory inventory = Bukkit.createInventory(null, InventoryType.FURNACE, result.getItemMeta().getDisplayName() + "'s Recipe");
                inventory.setItem(0, ingredients.get(0));
                inventory.setItem(1, new ItemStack(Material.COAL));
                inventory.setItem(2, result);
                return inventory;
            }
            Inventory inventory = Bukkit.createInventory(null, InventoryType.WORKBENCH, result.getItemMeta().getDisplayName() + "'s Recipe");
            for (int i = 0; i < ingredients.size() || i < 9; i++) {
                inventory.setItem(i, ingredients.get(i));
            }
            inventory.setItem(9, result);
            return inventory;
        }
    }

    private class ShopItem {
        private ItemStack item;
        private double price;
        private String description;
        private int quantity;

        public ShopItem(ItemStack item, double price, String description, int quantity) {
            this.item = item;
            this.price = price;
            this.description = description;
            this.quantity = quantity;
        }

        public ItemStack getItemStack() {
            ItemStack itemStack = new ItemStack(item);
            ItemMeta itemMeta = itemStack.getItemMeta();
            ArrayList<String> itemLore = new ArrayList<>();
            itemLore.add("Price: $" + Form.at(price, true));
            if (!description.equals("")) {
                itemLore.add(description);
            }
            itemMeta.setLore(itemLore);
            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }

        public ItemStack getItem() {
            return item;
        }
    }

    private Inventory getInv(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "VaultCraft Candy Man");
        for (Map.Entry entry : items.entrySet()) {
            inv.setItem((Integer) entry.getKey(), ((ShopItem) entry.getValue()).item);
        }
        for (Map.Entry entry : recipes.entrySet()) {
            inv.setItem((Integer) entry.getKey(), ((RecipeMenu) entry.getValue()).result);
        }
        inShop.add(player);
        return inv;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if(inShop.contains(event.getPlayer()))
            inShop.remove(event.getPlayer());
    }

    @EventHandler
    public void onEntInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ENDERMAN && !inShop.contains(event.getPlayer())) {
            event.getPlayer().openInventory(getInv(event.getPlayer()));
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (inShop.contains(event.getPlayer())) {
            inShop.remove(event.getPlayer());
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if(event.getWhoClicked() instanceof Player && event.getInventory().getName().contains("Recipe")) {
            event.setCancelled(true);
            event.getWhoClicked().openInventory(getInv((Player) event.getWhoClicked()));
            return;
        }

        if (event.getWhoClicked() instanceof Player && inShop.contains(event.getWhoClicked())) {
            event.setCancelled(true);
            if (event.getSlot() != event.getRawSlot()) { //FIXME: This is kinda hackish
                return;
            }
            int clickedSlot = event.getSlot();

            ShopItem item = items.get(clickedSlot);
            if(item == null) {
                RecipeMenu recipeMenu = recipes.get(clickedSlot);
                if(recipeMenu == null)
                    return;
                event.getWhoClicked().openInventory(recipeMenu.getInv());
                return;
            }
            User user = User.fromPlayer((Player) event.getWhoClicked());
            if (user.getMoney() < item.price) {
                Form.at((Player) event.getWhoClicked(), Prefix.ERROR, "You do not have enough money to purchase this item!");
                event.getWhoClicked().closeInventory();
                return;
            }

            if (event.getWhoClicked().getInventory().firstEmpty() == -1) {
                Form.at((Player) event.getWhoClicked(), Prefix.ERROR, "You have no empty slots in your inventory!");
                return;
            }

            ItemStack itemStack = item.getItemStack().clone();
            itemStack.setAmount(item.quantity);
            event.getWhoClicked().getInventory().addItem(itemStack);
            user.setMoney(user.getMoney() - item.price);
            Form.at((Player) event.getWhoClicked(), Prefix.SUCCESS, "You bought a " + item.getItem().getItemMeta().getDisplayName() + " for $" + String.valueOf(item.price) + "!");

            ((Player) event.getWhoClicked()).updateInventory();
        }
    }
}
