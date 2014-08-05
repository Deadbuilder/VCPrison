package net.vaultcraft.vcprison;

import net.vaultcraft.vcprison.commands.VCPrestige;
import net.vaultcraft.vcprison.commands.VCRankup;
import net.vaultcraft.vcprison.commands.VCReset;
import net.vaultcraft.vcprison.furance.FurnaceListener;
import net.vaultcraft.vcprison.listener.AsyncChatListener;
import net.vaultcraft.vcprison.listener.PrisonUserListener;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.pickaxe.*;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by tacticalsk8er on 7/30/2014.
 */
public class VCPrison extends JavaPlugin {

    private static VCPrison instance;

    public void onEnable() {
        instance = this;

        CommandManager.addCommand(new VCRankup("rankup", Group.COMMON, "nextrank"));
        CommandManager.addCommand(new VCPrestige("prestige", Group.COMMON, "startover"));
        CommandManager.addCommand(new VCReset("reset", Group.ADMIN));

        new AsyncChatListener();
        new PrisonUserListener();
        new PickaxeListener();
        new FurnaceListener();

        PickaxePerk.addPerk(new EfficiencyPerk(Material.STONE, "Efficiency", 1, 5, -1, "Adds a level of efficiency to your pick."), 0);
        PickaxePerk.addPerk(new FortunePerk(Material.DIAMOND, "Fortune", 1, 5, -1, "Adds a level of fortune to your pick."), 1);
        PickaxePerk.addPerk(new HastePerk(Material.DIAMOND_PICKAXE, "Haste", 5, 0, 4, "Adds a level of haste when you have your pick selected."), 2);
        PickaxePerk.addPerk(new AutoSmeltPerk(Material.FIRE, Material.FIRE, Material.FURNACE, "Auto Smelt", 8, false, "Smelt things as you go!", "Toggable"), 3);
        PickaxePerk.addPerk(new SilkTouchPerk(Material.WEB, Material.WEB, Material.STRING, "Silk Touch", 8, false, "Adds the Silk Touch enchanment to your pick.", "Toggable"), 4);
        PickaxePerk.addPerk(new NightVisionPerk(Material.EYE_OF_ENDER, Material.EYE_OF_ENDER, Material.ENDER_PEARL, "Night Vision", 8, false, "Adds night vision when you have your pick selected.", "Toggable"), 5);
        PickaxePerk.addPerk(new SpeedPerk(Material.DIAMOND_BOOTS, Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, "Speed Boost", 8, false, "Adds Speed when you have your pick selected.", "Toggable"), 6);

        for(Player player : Bukkit.getOnlinePlayers()) {
            new PrisonUser(player);
        }

        MineLoader.loadMines();
        Runnable resetSchedule = new Runnable() {
            @Override
            public void run() {
                for (Mine mine : MineLoader.getMines()) {
                    MineLoader.resetMine(mine);
                }
                Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&5&lV&7&lC&e: &7Mines reset!"));
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, resetSchedule, 20*900, 20*900);
    }

    public void onDisable() {
        PrisonUser.disable();
    }

    public static VCPrison getInstance() {
        return instance;
    }
}
