package net.vaultcraft.vcprison.crate;

import org.bukkit.inventory.ItemStack;

/**
 * Created by Connor on 9/16/14. Designed for the VCPrison project.
 */

public class CrateItem {

    private double chance;
    private ItemStack stack;

    public CrateItem(ItemStack stack, double chance) {
        this.chance = chance;
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }

    public boolean runChance() {
        return (Math.random()*100 <= chance);
    }

    public double getChance() {
        return chance;
    }
}
