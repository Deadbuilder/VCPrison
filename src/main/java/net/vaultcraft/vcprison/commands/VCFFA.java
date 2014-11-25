package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcprison.ffa.combatlog.CombatLog;
import net.vaultcraft.vcprison.ffa.combatlog.FFAWarmup;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 10/21/14
 */
public class VCFFA extends ICommand {

    public VCFFA(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    public void processCommand(Player player, String[] strings) {
        FFAPlayer ffa = FFAPlayer.getFFAPlayerFromPlayer(player);

        if (FFAWarmup.isInWarmup(player)) {
            Form.at(player, Prefix.WARNING, "You are in warmup right now!");
            return;
        }

        if (ffa.isPlaying()) {
            //end
            if(!CombatLog.isSafeLogout(player)) {
                Form.at(player, Prefix.ERROR, "You can't leave ffa if you are tagged.");
                return;
            }

            FFAWarmup.addToWarmup(player);
            Runnable go = () -> {
                FFAWarmup.removeFromWarmup(player);
                ffa.endFFA();
                Form.at(player, Prefix.SUCCESS, "You have left the FFA!");
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), go, 20 * 4);

            Form.at(player, Prefix.SUCCESS, "Teleportation will commence in 4 seconds!");
        } else {
            FFAWarmup.addToWarmup(player);
            Runnable go = () -> {
                FFAWarmup.removeFromWarmup(player);
                ffa.beginFFA();
                Form.at(player, Prefix.SUCCESS, "You have joined the FFA! Use /ffa to leave.");
            };
            Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), go, 20 * 4);

            Form.at(player, Prefix.SUCCESS, "Teleportation will commence in 4 seconds!");
        }
    }
}
