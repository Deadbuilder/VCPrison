package net.vaultcraft.vcprison.gangs;

import net.minecraft.util.com.google.gson.Gson;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by tacticalsk8er on 10/1/2014.
 */
public class GangManager implements Listener {

    private static HashMap<String, Gang> gangs = new HashMap<>();
    private static FileConfiguration gangsConfig = YamlConfiguration.loadConfiguration(new File(VCPrison.getInstance().getDataFolder(), "gangs.yml"));
    private static BukkitTask task = null;

    public GangManager() {
        ConfigurationSection section = gangsConfig.getConfigurationSection("Gangs");
        Gson gson = new Gson();
        if(section != null) {
            for (String key : section.getKeys(false)) {
                if(section.getString(key).equals("_disbanded"))
                    continue;
                gangs.put(key, gson.fromJson(section.getString(key), Gang.class));
            }
        }
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(VCPrison.getInstance(),
                () -> {
                    try {
                        gangsConfig.save(new File(VCPrison.getInstance().getDataFolder(), "gangs.yml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                },  18000l, 18000l);
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    public static Gang getGang(String name) {
        return gangs.get(name);
    }

    public static HashMap<String, Gang> getGangs() {
        return gangs;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        Player damager = null;
        if(event.getDamager() instanceof Player)
            damager = (Player) event.getDamager();
        if(event.getDamager() instanceof Arrow) {
            if(((Arrow) event.getDamager()).getShooter() instanceof Player)
                damager = (Player) ((Arrow) event.getDamager()).getShooter();
        }

        if(damager == null)
            return;

        Gang playerGang = null;
        Gang damagerGang = null;

        for (Gang gang : gangs.values()) {
            if (gang.getOwnerUUID().equals(player.getUniqueId().toString())) {
                playerGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(player.getUniqueId().toString())) {
                    playerGang = gang;
                    break;
                }
            }
        }

        for (Gang gang : gangs.values()) {
            if (gang.getOwnerUUID().equals(damager.getUniqueId().toString())) {
                damagerGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(damager.getUniqueId().toString())) {
                    damagerGang = gang;
                    break;
                }
            }
        }

        if(playerGang == null)
            return;
        if(damagerGang == null)
            return;
        if(playerGang.isFriendlyFire() && damagerGang.isFriendlyFire())
            return;

        if(playerGang.getGangName().equals(damagerGang.getGangName())) {
            event.setCancelled(true);
            Form.at(damager, Prefix.WARNING, "You can't hurt somebody in your gang!");
            return;
        }

        for(String gangName : playerGang.getAlliedGangs()) {
            if(gangName.equalsIgnoreCase(damagerGang.getGangName())) {
                Form.at(damager, Prefix.WARNING, "You can't hurt somebody a gang allied with yours!");
                event.setCancelled(true);
                return;
            }
        }
    }

    public static void disable() {
        if(task != null)
            task.cancel();
        try {
            gangsConfig.save(new File(VCPrison.getInstance().getDataFolder(), "gangs.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static FileConfiguration getGangsConfig() {
        return gangsConfig;
    }
}
