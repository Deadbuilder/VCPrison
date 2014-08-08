package net.vaultcraft.vcprison.mine.warp;

import net.minecraft.util.com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Connor on 8/4/14. Designed for the VCPrison project.
 */

public class WarpGUI implements Listener {

    public WarpGUI() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    private static final ItemStack G_SIDEBAR_L = build(Material.GOLD_INGOT, (byte)0, "&c&lRecommended warp -->");
    private static final ItemStack G_SIDEBAR_R = build(Material.GOLD_INGOT, (byte)0, "&c&l<-- Recommended warp");

    public static Inventory create(PrisonUser user) {
        Rank at = user.getRank();

        Inventory make = Bukkit.createInventory(user.getPlayer(), 9*6, "Mine warp menu");

        ItemStack set = new ItemStack(at.getMascot());
        ItemMeta meta = set.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', at.getPrefix()));
        set.setItemMeta(meta);

        make.setItem(9, G_SIDEBAR_L);
        make.setItem(10, G_SIDEBAR_L);


        make.setItem(13, set);


        make.setItem(16, G_SIDEBAR_R);
        make.setItem(17, G_SIDEBAR_R);

        int start = 27;

        int rank = 1;
        for (Rank r : Rank.values()) {
            if (r.equals(user.getRank()))
                break;

            rank++;
        }

        for (int i = start; i < rank+27; i++) {
            make.setItem(i, RANK_ITEMS[i-27]);
        }

        open.put(user.getPlayer(), make);
        return make;
    }

    private static ItemStack[] RANK_ITEMS = new ItemStack[Rank.values().length];

    static {
        int pos = 0;
        for (Rank rank : Rank.values()) {
            ItemStack set = new ItemStack(rank.getMascot(), pos+1);
            ItemMeta meta = set.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', rank.getPrefix()));
            set.setItemMeta(meta);

            RANK_ITEMS[pos++] = set;
        }
    }

    private static ItemStack build(Material type, byte data, String displayName, String... lore) {
        ItemStack stack = new ItemStack(type, 1, (short)1, data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (lore.length > 0) {
            List<String> l = Lists.newArrayList();
            for (String s : lore) {
                l.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            meta.setLore(l);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    private static HashMap<Player, Inventory> open = new HashMap<>();

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (open.containsKey(event.getWhoClicked())) {
            event.setCancelled(true);

            Player click = (Player)event.getWhoClicked();
            int slot = event.getSlot();

            if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR))
                return;

            Rank bySlot = findRankBySlot(slot-27);
            Location find = WarpLoader.getWarpLocation(bySlot);
            if (slot == 13)
                find = WarpLoader.getWarpLocation(PrisonUser.fromPlayer(click).getRank());
            if (find == null)
                return;

            click.teleport(find);
            click.playSound(click.getLocation(), Sound.FIREWORK_TWINKLE, 1, 1);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (open.containsKey(event.getPlayer())) {
            open.remove(event.getPlayer());
        }
    }

    private static Rank findRankBySlot(int slot) {
        int pos = -1;
        for (Rank r : Rank.values()) {
            if (++pos == slot)
                return r;
        }
        return null;
    }
}
