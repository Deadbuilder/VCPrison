package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public class CandyManager {

    private static HashMap<String, Candy> candy = new HashMap<>();

    public static Candy getCandy(String name) {
        return candy.get(name);
    }

    public static void registerCandy(String name, Candy c) {
        candy.put(name, c);
        VCPrison.getInstance().getServer().addRecipe(c.getRecipe());
        Bukkit.getPluginManager().registerEvents(c, VCPrison.getInstance());
    }

    public static Candy getCandy(ItemStack stack) {
        if (stack == null)
            return null;

        ItemMeta sMeta = stack.getItemMeta();
        for (Candy c : candy.values()) {
            ItemStack s = c.getCandyItem();
            ItemMeta cMeta = s.getItemMeta();

            if ((cMeta.getDisplayName() != null && sMeta.getDisplayName() != null) && cMeta.getDisplayName().equals(sMeta.getDisplayName())) {
                if (s.getType().equals(stack.getType()) && s.getData().getData() == stack.getData().getData())
                    return c;
            }
        }
        return null;
    }
}
