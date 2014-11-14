package net.vaultcraft.vcprison.event;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.events.ServerEvent;
import net.vaultcraft.vcutils.innerplugin.InnerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Connor Hollasch
 * @since 10/12/14
 */
public class DropParty extends InnerPlugin {

    private DropEvent dropEvent;
    private static DropParty instance;
    private double timeLeft = 60*60;

    private static final ItemStack DP_TOKEN;
    static {
        DP_TOKEN = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = DP_TOKEN.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lRight Click: &2&lActivate Drop Party"));
        meta.setLore(Arrays.asList(new String[]{"Right click to throw this creating a mobile drop party!", "Items can be picked up by anyone"}));
        DP_TOKEN.setItemMeta(meta);
    }

    private static HashSet<Integer> warn = new HashSet<>();
    static {
        warn.add(2);   //1
        warn.add(4);   //2
        warn.add(6);   //3
        warn.add(8);   //4
        warn.add(10);  //5
        warn.add(20);  //10
        warn.add(60);  //30
        warn.add(120); //1 min
        warn.add(360); //3 min
        warn.add(600); //5 min
    }

    public void onEnable() {
        instance = this;
        dropEvent = new DropEvent();

        Runnable task = () -> {
            timeLeft-=0.5;
            if (timeLeft <= 0) {
                dropEvent.onEvent(VCPrison.getInstance());
                timeLeft = (60*60);
                return;
            }

            if (warn.contains((int)(timeLeft))) {
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&6&lDROP-PARTY&7: &fThe drop party will start in " + format((int)timeLeft) + "!"));
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VCPrison.getInstance(), task, 20, 20);
    }

    private static String format(int seconds) {
        seconds/=2;

        if (seconds/60 >= 1) {
            return (seconds/60) + " minutes";
        } else {
            return (seconds) + " seconds";
        }
    }
    public void onDisable() {

    }

    public void setTimeLeft(int timeLeft) {
        this.timeLeft = timeLeft;
    }

    public DropEvent getDropEvent() {
        return dropEvent;
    }

    public static DropParty getInstance() {
        return instance;
    }

    @Override
    public Plugin getWrapperPlugin() {
        return VCPrison.getInstance();
    }

    public int getTimeLeft() {
        return (int)Math.ceil(timeLeft);
    }

    public static ItemStack getDpToken() {
        return DP_TOKEN;
    }
}
