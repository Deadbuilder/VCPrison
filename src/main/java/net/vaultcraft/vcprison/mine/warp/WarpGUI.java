package net.vaultcraft.vcprison.mine.warp;

import net.minecraft.util.com.google.common.collect.Lists;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.utils.Rank;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by Connor on 8/4/14. Designed for the VCPrison project.
 */

public class WarpGUI {

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

        for (int i = start; i < make.getSize(); i++) {
            make.setItem(i, RANK_ITEMS[start-27]);
        }

        return make;
    }

    private static ItemStack[] RANK_ITEMS = new ItemStack[Rank.values().length];

    static {
        int pos = 0;
        for (Rank rank : Rank.values()) {

            System.out.println(rank.getMascot().toString());

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
}
