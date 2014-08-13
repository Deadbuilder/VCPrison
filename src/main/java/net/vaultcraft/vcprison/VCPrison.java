package net.vaultcraft.vcprison;

import net.vaultcraft.vcprison.commands.*;
import net.vaultcraft.vcprison.furance.FurnaceListener;
import net.vaultcraft.vcprison.listener.AsyncChatListener;
import net.vaultcraft.vcprison.listener.PrisonUserListener;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.mine.warp.WarpGUI;
import net.vaultcraft.vcprison.mine.warp.WarpLoader;
import net.vaultcraft.vcprison.worth.ItemWorthLoader;
import net.vaultcraft.vcprison.pickaxe.*;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.worth.WardenManager;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.sign.SignManager;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;

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
        CommandManager.addCommand(new VCWarp("warp", Group.COMMON, "mine", "mines"));

        new AsyncChatListener();
        new PrisonUserListener();
        new PickaxeListener();
        new FurnaceListener();

        new WarpGUI();

        PickaxePerk.addPerk(new EfficiencyPerk(Material.STONE, "Efficiency", 1, 5, 100, "Adds a level of efficiency to your pick."), 0);
        PickaxePerk.addPerk(new FortunePerk(Material.DIAMOND, "Fortune", 1, 5, 100, "Adds a level of fortune to your pick."), 1);
        PickaxePerk.addPerk(new HastePerk(Material.DIAMOND_PICKAXE, "Haste", 5, 0, 4, "Adds a level of haste when you have your pick selected."), 2);
        PickaxePerk.addPerk(new ExplosionPerk(Material.TNT, "Explosion", 3, 0, 25, "Blow up the blocks you wish to mine"), 3);
        PickaxePerk.addPerk(new AutoSmeltPerk(Material.FIRE, Material.FIRE, Material.FURNACE, "Auto Smelt", 8, false, "Smelt things as you go!", "Toggable"), 4);
        PickaxePerk.addPerk(new SilkTouchPerk(Material.WEB, Material.WEB, Material.STRING, "Silk Touch", 8, false, "Adds the Silk Touch enchantment to your pick.", "Toggable"), 5);
        PickaxePerk.addPerk(new NightVisionPerk(Material.EYE_OF_ENDER, Material.EYE_OF_ENDER, Material.ENDER_PEARL, "Night Vision", 8, false, "Adds night vision when you have your pick selected.", "Toggable"), 6);
        PickaxePerk.addPerk(new SpeedPerk(Material.DIAMOND_BOOTS, Material.DIAMOND_BOOTS, Material.LEATHER_BOOTS, "Speed Boost", 8, false, "Adds Speed when you have your pick selected.", "Toggable"), 7);

        for(Player player : Bukkit.getOnlinePlayers()) {
            new PrisonUser(player);
        }

        MineLoader.loadMines();
        WarpLoader.loadWarps();
        ItemWorthLoader.loadItemWorth();

        new WardenManager();

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

        //reset at start
        for (Mine mine : MineLoader.getMines()) {
            MineLoader.resetMine(mine);
        }
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&5&lV&7&lC&e: &7Mines reset!"));

        Runnable minePercentUpdate = new Runnable() {
            public void run() {
                for (Mine mine : MineLoader.getMines()) {
                    if (SignManager.fromMeta("mine%" + mine.getRank().toString()) == null)
                        continue;

                    SignManager.updateSigns("mine%"+mine.getRank().toString(), "&m---&c=&0&m---", "&5Percent Mined", "&8&l&n"+(df.format(mine.getPercent() * 100))+"%", "&m---&c=&0&m---");
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, minePercentUpdate, 20, 20);

        Runnable daytime = new Runnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.setPlayerTime(6000, true);
                }
            }
        };
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, daytime, 20*60, 20*60);
    }

    private static DecimalFormat df = new DecimalFormat("0.00");

    public void onDisable() {
        PrisonUser.disable();
    }

    public static VCPrison getInstance() {
        return instance;
    }
}
