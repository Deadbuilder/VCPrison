package net.vaultcraft.vcprison;

import net.vaultcraft.vcprison.commands.VCPrestige;
import net.vaultcraft.vcprison.commands.VCRankup;
import net.vaultcraft.vcprison.commands.VCReset;
import net.vaultcraft.vcprison.commands.VCWarp;
import net.vaultcraft.vcprison.furance.FurnaceListener;
import net.vaultcraft.vcprison.listener.AsyncChatListener;
import net.vaultcraft.vcprison.listener.PrisonUserListener;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.pickaxe.PickaxeListener;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.command.CommandManager;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
        CommandManager.addCommand(new VCWarp("warp", Group.COMMON, "mine", "mines"));

        new AsyncChatListener();
        new PrisonUserListener();
        new PickaxeListener();
        new FurnaceListener();

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
