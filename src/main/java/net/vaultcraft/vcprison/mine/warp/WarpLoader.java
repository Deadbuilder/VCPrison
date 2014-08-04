package net.vaultcraft.vcprison.mine.warp;

import net.minecraft.util.org.apache.commons.io.FileUtils;
import net.vaultcraft.vcprison.VCPrison;

import java.io.File;

/**
 * Created by Connor on 8/4/14. Designed for the VCPrison project.
 */

public class WarpLoader {

    public static void loadMines() {
        try {
            //load
            File folder = VCPrison.getInstance().getDataFolder();
            if (!(folder.exists()))
                folder.mkdirs();

            File file = new File(folder, "mine.warps");
            if (!(file.exists()))
                file.createNewFile();

            for (String line : FileUtils.readLines(file)) {
                line = line.replace(" ", "").toLowerCase();
                String in = findInsideMap(line, "rank");
                String loc = findInsideMap(line, "location");

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
}
