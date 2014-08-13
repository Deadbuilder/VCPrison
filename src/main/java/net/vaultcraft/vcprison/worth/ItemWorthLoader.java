package net.vaultcraft.vcprison.worth;

import net.minecraft.util.org.apache.commons.io.FileUtils;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Material;

import java.io.File;
import java.util.HashMap;

/**
 * Created by tacticalsk8er on 8/10/2014.
 */
public class ItemWorthLoader {

    private static HashMap<Mine, HashMap<Material, Double>> itemWorth = new HashMap<>();

    public static void loadItemWorth() {
        try {
            File folder = VCPrison.getInstance().getDataFolder();

            if (!folder.exists())
                folder.mkdir();

            File file = new File(folder, "mines.worth");
            if (!file.exists())
                file.createNewFile();

            for (String line : FileUtils.readLines(file)) {
                if(line.startsWith("#"))
                    continue;

                line = line.replace(" ", "").toLowerCase();
                if(line.contains("rank")) {
                    Rank rank = Rank.fromName(findInsideMap(line, "rank"));
                    String worth = findInsideMap(line, "worth");

                    HashMap<Material, Double> mineWorth = new HashMap<>();
                    for(String s : worth.split(",")) {
                        Material material = Material.getMaterial(s.split("\\|")[0]);
                        double price = Double.parseDouble(s.split("\\|")[1]);
                        mineWorth.put(material, price);
                    }
                    itemWorth.put(MineLoader.fromRank(rank), mineWorth);
                }
            }
        } catch (Exception e) {
            Logger.error(VCPrison.getInstance(), e);
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

    public static double getWorth(Mine mine, Material material) {
        HashMap<Material, Double> parse = itemWorth.get(mine);
        if (parse == null)
            return -1;

        return (parse.containsKey(material) ? parse.get(material) : -1);
    }

    public static double getWorth(Rank rank, Material material) {
        HashMap<Material, Double> parse = itemWorth.get(MineLoader.fromRank(rank));
        if (parse == null)
            return -1;

        return (parse.containsKey(material) ? parse.get(material) : -1);
    }

    public static HashMap<Mine, HashMap<Material, Double>> getAll() {
        return itemWorth;
    }

    public static HashMap<Material, Double> getAll(Mine mine) {
        return itemWorth.get(mine);
    }

    public static HashMap<Material, Double> getAll(Rank rank) {
        return itemWorth.get(MineLoader.fromRank(rank));
    }
}

