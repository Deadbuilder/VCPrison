package net.vaultcraft.vcprison.mine;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.protection.Area;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Connor on 8/1/14. Designed for the VCPrison project.
 */

public class BlockCollection {

    private LinkedHashMap<Material, Double> reset = new LinkedHashMap<>();
    private double max;

    public BlockCollection(HashMap<Material, Double> reset) {
        double total = 0.0;
        for (Material material : reset.keySet()) {
            double chance = reset.get(material);
            this.reset.put(material, total+=chance);
        }
        max = total;
    }

    public void reset(final Collection<Block> blocks) {
        Runnable async = new Runnable() {
            public void run() {
                HashMap<Block, Material> fixed_reset_blocks = new HashMap<>();
                for (Block block : blocks) {
                    //pick material
                    Material use = null;
                    double random = (Math.random()*max);
                    for (Material m : reset.keySet()) {
                        double chance = reset.get(m);
                        if (chance > random) {
                            use = m;
                            break;
                        }
                    }
                    Validate.notNull(use);

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

        for (int x = xMin; x <= xMax; x++) {
            for (int y = yMin; y <= yMax; y++) {
                for (int z = zMin; z <= zMax; z++) {
                    Block hit = new Location(min.getWorld(), x, y, z).getBlock();
                    blocks.add(hit);
                }
            }
        }

        return blocks;
    }
}
