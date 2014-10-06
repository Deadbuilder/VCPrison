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

    public static final ItemStack startingHelmet = ItemUtils.build(Material.LEATHER_HELMET, "&5&lFFA &7&lHelmet", "&e&oStarting helmet");
    public static final ItemStack startingChestplate = ItemUtils.build(Material.LEATHER_CHESTPLATE, "&5&lFFA &7&lChestplate", "&e&oStarting chestplate");
    public static final ItemStack startingLeggings = ItemUtils.build(Material.LEATHER_LEGGINGS, "&5&lFFA &7&lLeggings", "&e&oStarting leggings");
    public static final ItemStack startingBoots = ItemUtils.build(Material.LEATHER_BOOTS, "&5&lFFA &7&lBoots", "&e&oStarting boots");
    public static final ItemStack startingSword = ItemUtils.build(Material.STONE_SWORD, "&5&lFFA &7&lSword", "&e&oStarting sword");

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
