package net.vaultcraft.vcprison.crate;

import com.google.common.collect.Lists;
import net.minecraft.util.com.google.gson.Gson;
import net.minecraft.util.com.google.gson.GsonBuilder;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.file.FileController;
import net.vaultcraft.vcutils.item.ItemParser;
import net.vaultcraft.vcutils.item.ItemSerializer;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Connor on 9/16/14. Designed for the VCPrison project.
 */

public class CrateFile implements FileController {

    private static CrateFile instance;

    public static CrateFile getInstance() {
        return (instance == null ? instance = new CrateFile() : instance);
    }

    private static File file = new File(VCPrison.getInstance().getDataFolder().getAbsolutePath(), "crates.json");

    public static List<CrateItem> crateItems = Lists.newArrayList();

    public File getFile() {
        return file;
    }

    public void save() {
        try {
            JSONObject obj = new JSONObject();
            JSONArray regions = new JSONArray();

            for (CrateItem item : crateItems) {
                JSONObject crate = new JSONObject();
                crate.put("item", ItemSerializer.fromStack(item.getStack()));
                crate.put("chance", item.getChance());

                regions.add(crate);
            }

            obj.put("items", regions);
            JSONParser parser = new JSONParser();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            String toJson = gson.toJson(parser.parse(obj.toJSONString()));

            FileWriter out = new FileWriter(file);out.write(toJson);
            out.flush();
            out.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void load() {
        if (file == null)
            file = new File(VCPrison.getInstance().getDataFolder().getAbsolutePath(), "crates.json");
        if (!(VCPrison.getInstance().getDataFolder().exists()))
            VCPrison.getInstance().getDataFolder().mkdirs();
        if (!(file.exists()))
            VCPrison.getInstance().saveResource("crates.json", false);

        JSONParser parser = new JSONParser();
        JSONObject data = null;
        try {
            data = (JSONObject)parser.parse(new FileReader(file));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (data.isEmpty())
            return;

        JSONArray all = (JSONArray) data.get("items");

        Iterator items = all.iterator();
        while (items.hasNext()) {
            JSONObject item = (JSONObject)items.next();
            ItemStack stack = ItemSerializer.fromString((String)item.get("item"));
            double chance = (Double)item.get("chance");

            CrateItem ci = new CrateItem(stack, chance);
            crateItems.add(ci);
        }
    }
}
