package net.vaultcraft.vcprison.mine;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.protection.Area;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Connor on 8/1/14. Designed for the VCPrison project.
 */

public class BlockCollection {

    private HashMap<Material, Double> reset = new HashMap<>();

    public BlockCollection(HashMap<Material, Double> reset) {
        this.reset = reset;
    }

    public void reset(final Collection<Block> blocks) {
        Runnable async = new Runnable() {
            public void run() {
                HashMap<Block, Material> fixed_reset_blocks = new HashMap<>();
                for (Block block : blocks) {
                    //pick material
                    Material use = null;
                    double random = (Math.random()*100);
                    while (use == null) {
                        for (Material key : reset.keySet()) {
                            double value = reset.get(key);
                            double b4 = (use == null ? 101 : reset.get(use));

                            if (value < b4 && value < random) {
                                //override
                                use = key;
                            }
                        }
                    }

                    fixed_reset_blocks.put(block, use);
                }

                final HashMap<Block, Material> clone = (HashMap<Block, Material>) fixed_reset_blocks.clone();
                Runnable sync = new Runnable() {
                    public void run() {
                        for (Block $k : clone.keySet()) {
                            Material $v = clone.get($k);
                            $k.setType($v);
                        }
                    }
                };
                Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), sync);
            }
        };
        Bukkit.getScheduler().scheduleAsyncDelayedTask(VCPrison.getInstance(), async);
    }

    public static Collection<Block> iterator(Area area) {
        Location max = area.getMax();
        Location min = area.getMin();

        int xMin = min.getBlockX();
        int xMax = max.getBlockX();
        int yMin = min.getBlockY();
        int yMax = max.getBlockY();
        int zMin = min.getBlockZ();
        int zMax = max.getBlockZ();

        Collection<Block> blocks = Lists.newArrayList();

        for (int x = xMin; x < xMax; x++) {
            for (int y = yMin; y < yMax; y++) {
                for (int z = zMin; z < zMax; z++) {
                    Block hit = new Location(min.getWorld(), x, y, z).getBlock();
                    blocks.add(hit);
                }
            }
        }

        return blocks;
    }
}
