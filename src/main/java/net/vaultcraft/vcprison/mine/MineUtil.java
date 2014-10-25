package net.vaultcraft.vcprison.mine;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.protection.Area;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Connor on 8/1/14. Designed for the VCPrison project.
 */

public class MineUtil {

    private LinkedHashMap<Material, Double> reset = new LinkedHashMap<>();
    private static List<BlockInjector> injectors = Lists.newArrayList();

    private double max;

    public MineUtil(HashMap<Material, Double> reset) {
        double total = 0.0;
        for (Material material : reset.keySet()) {
            double chance = reset.get(material);
            this.reset.put(material, total+=chance);
        }
        max = total;
    }

    public static void createBlockInjector(BlockInjector injector) {
        injectors.add(injector);
    }

    public void reset(final Collection<Block> blocks, final Mine mine) {
        Runnable async = new Runnable() {
            public void run() {
                HashMap<Block, Material> fixed_reset_blocks = new HashMap<>();
                for (Block block : blocks) {
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
                        block: for (Block $k : clone.keySet()) {
                            Chunk c = $k.getWorld().getChunkAt($k);
                            if (!c.isLoaded())
                                c.load();

                            if ($k.getType().equals(Material.CHEST)) {
                                Chest chest = (Chest)$k.getState();
                                chest.getInventory().clear();
                                chest.update();
                            }
                            for (BlockInjector injector : injectors) {
                                if (injector.doGenerate()) {
                                    injector.setBlock($k.getLocation());
                                    continue block;
                                }
                            }

                            Material $v = clone.get($k);
                            $k.setType($v);
                        }

                        for (Player player : mine.getArea().getMax().getWorld().getPlayers()) {
                            if (mine.getArea().isInArea(player.getLocation())) {
                                Location tp = player.getLocation().clone();
                                tp.setY(mine.getArea().getMax().getY()+1);
                                player.teleport(tp);
                            }
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
                    Block hit = min.getWorld().getBlockAt(x, y, z);
                    blocks.add(hit);
                }
            }
        }

        return blocks;
    }
}
