package net.vaultcraft.vcprison.mine;

import net.minecraft.util.org.apache.commons.io.FileUtils;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.protection.Area;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Connor on 8/1/14. Designed for the VCPrison project.
 */

public class MineLoader {

    private static HashMap<Mine, MineUtil> mines = new HashMap<>();

    public static void loadMines() {
        try {
            //load
            File folder = VCPrison.getInstance().getDataFolder();
            if (!(folder.exists()))
                folder.mkdirs();

            File file = new File(folder, ".mines");
            if (!(file.exists()))
                file.createNewFile();

            for (String line : FileUtils.readLines(file)) {
                if (line.startsWith("#"))
                    continue;

                line = line.toLowerCase().replace(" ", "");
                HashMap<String, Object> data = new HashMap<>();
                if (line.contains("rank")) {
                    Rank parse = Rank.fromName(findInsideMap(line, "rank"));
                    String blocks = findInsideMap(line, "blocks");
                    data.put("rank", parse);

                    //load block data
                    HashMap<Material, Double> blcks = new HashMap<>();
                    for (String key : blocks.split(",")) {
                        String use = key.replaceAll("[\\),\\(]", "");
                        double percent = Double.parseDouble(use.split("%")[0]);
                        Material type = Material.getMaterial(use.split("%")[1].toUpperCase());
                        blcks.put(type, percent);
                    }
                    data.put("blocks", blcks);
                }

                Rank rank = (Rank)data.get("rank");
                Mine mine = new Mine(rank, ProtectionManager.getInstance().getArea(rank.toString()).getArea());
                mine.setInitialBlocks(MineUtil.iterator(mine.getArea()).size());
                mines.put(mine, new MineUtil((HashMap<Material, Double>)data.get("blocks")));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String findInsideMap(String total, String key) {
        int after = total.indexOf(key)+key.length()+1;
        String modify = total.substring(after);
        String make = "";
        int deep = 0;
        for (char c : modify.toCharArray()) {
            if (c == '(')
                deep++;
            if (c == ')')
                deep--;

            if (deep >= 0)
                make+=c;
            else
                break;
        }
        return make;
    }

    public static void resetMine(Mine mine) {
        if (mine.isResetting())
            return;

        mines.get(mine).reset(MineUtil.iterator(mine.getArea()), mine);

        //just for the fun
        Area area = mine.getArea();
        int y = area.getMax().getBlockY()+1;
        int dist = area.getMax().getBlockX()-area.getMin().getBlockX();
        int xStart = area.getMin().getBlockX();
        int zStart = area.getMin().getBlockZ();

        Location[] all = new Location[4];
        all[0] = new Location(area.getMax().getWorld(), xStart, y, zStart);
        all[1] = new Location(area.getMax().getWorld(), xStart+dist, y, zStart+dist);
        all[2] = new Location(area.getMax().getWorld(), xStart, y, zStart+dist);
        all[3] = new Location(area.getMax().getWorld(), xStart+dist, y, zStart);

        for (Location loc : all) {
            Firework fw = (Firework)loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            FireworkEffect effect = FireworkEffect.builder()
                    .withColor(Color.fromRGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)))
                    .with(FireworkEffect.Type.BALL_LARGE)
                    .withTrail()
                    .withFade(Color.fromRGB((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)))
                    .build();
            FireworkMeta m = fw.getFireworkMeta();
            m.addEffect(effect);
            m.setPower(2);
            fw.setFireworkMeta(m);
        }

        mine.reset();
    }

    public static Collection<Mine> getMines() {
        return mines.keySet();
    }

    public static Mine fromRank(Rank rank) {
        for (Mine mine : mines.keySet()) {
            if (mine.getRank().equals(rank))
                return mine;
        }
        return null;
    }

    public static Mine fromLocation(Location location) {
        for (Mine mine : mines.keySet()) {
            if (mine.getArea().isInArea(location))
                return mine;
        }
        return null;
    }
}
