package net.vaultcraft.vcprison.ffa;

import com.google.common.collect.Lists;
import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Connor Hollasch on 9/28/14.
 */

public class FFAItems {

    private static List<FFAItem> earned = Lists.newArrayList();

    static {
        earned.add(new FFAItem(new ItemStack(Material.GOLD_HELMET), 20, 12000));
    }

    public static class FFAItem {
        private ItemStack stack;
        private int chance;
        private double worth;

        public FFAItem(ItemStack stack, int chance, double worth) {
            this.stack = stack;
            this.chance = chance;
            this.worth = worth;
        }
    }
}
