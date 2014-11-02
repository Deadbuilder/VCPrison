package net.vaultcraft.vcprison.event;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.events.ServerEvent;
import net.vaultcraft.vcutils.innerplugin.InnerPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

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
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(VCPrison.getInstance(), task, 20, 20);
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
