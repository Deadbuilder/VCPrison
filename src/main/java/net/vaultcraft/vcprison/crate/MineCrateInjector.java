package net.vaultcraft.vcprison.crate;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.mine.BlockInjector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import java.util.List;

/**
 * Created by Connor on 9/15/14. Designed for the VCPrison project.
 */

public class MineCrateInjector implements BlockInjector {

    private static List<Location> injected = Lists.newArrayList();

    public static List<Location> getInjected() {
        return injected;
    }

    public boolean doGenerate() {
        return (Math.random()*100) <= 0.001;
    }

    public void setBlock(Location loc) {
        if (injected.contains(loc))
            return;

        loc.getBlock().setType(Material.CHEST);
        Chest chest = (Chest)loc.getBlock().getState();

        Inventory inv = chest.getInventory();
        inv.clear();

        for (int i = 0; i < inv.getSize(); i++) {
            CrateItem random = CrateFile.crateItems.get((int)(Math.random()*CrateFile.crateItems.size()));
            if (random.runChance())
                inv.setItem(i, random.getStack());
        }

        injected.add(loc);
    }
}
