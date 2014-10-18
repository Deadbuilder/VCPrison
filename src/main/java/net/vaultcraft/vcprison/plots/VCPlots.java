package net.vaultcraft.vcprison.plots;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class VCPlots extends ICommand {


    public VCPlots(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if(args.length == 0) {
            executePlot(player);
            return;
        }
        switch (args[0].toLowerCase()) {
            case "setspawn":
                executeSetSpawn(player);
                break;
            case "addbuilder":
                executeAddBuilder(player, args);
                break;
            case "removebuilder":
                executeRemoveBuilder(player, args);
                break;
            case "delete":
                executeDelete(player);
                break;
            case "claim":
                executeClaim(player);
                break;
            case "info":
                executeInfo(player);
                break;
            default:
                executePlotPlayer(player, args);
                break;
        }
    }

    public void executePlot(Player player) {
        PlotWorld.getPlotMenu().open(player, player);
    }

    public void executeSetSpawn(Player player) {
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(player.getLocation());
        if(plot == null) {
            Form.at(player, Prefix.ERROR, "You have to be in a plot in order to use this command");
            return;
        }

        if(!plot.getOwnerUUID().equals(player.getUniqueId().toString())) {
            Form.at(player, Prefix.ERROR, "You have to be the owner of this plot to change the spawn location.");
            return;
        }
        plot.setPlotSpawn(player.getLocation());
        Form.at(player, Prefix.SUCCESS, "You have set the plot spawn location.");
    }

    public void executeAddBuilder(Player player, String[] args) {
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(player.getLocation());
        if(plot == null) {
            Form.at(player, Prefix.ERROR, "You have to be in a plot in order to use this command");
            return;
        }

        if(!plot.getOwnerUUID().equals(player.getUniqueId().toString())) {
            Form.at(player, Prefix.ERROR, "You have to be the owner of this plot to add a builder.");
            return;
        }

        if(args.length < 2) {
            Form.at(player, Prefix.ERROR, "You have to specify a player to add.");
            return;
        }

        OfflinePlayer builder = Bukkit.getPlayer(args[1]);
        if(builder == null) {
            builder = Bukkit.getOfflinePlayer(args[1]);
            if(builder == null) {
                Form.at(player, Prefix.ERROR, args[1] + " has never been on this server.");
                return;
            }
        }

        plot.addBuildUUID(builder.getUniqueId().toString());
        Form.at(player, Prefix.SUCCESS, "You have added " + builder.getName() + " as a builder in this plot.");
    }

    public void executeRemoveBuilder(Player player, String[] args) {
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(player.getLocation());
        if(plot == null) {
            Form.at(player, Prefix.ERROR, "You have to be in a plot in order to use this command");
            return;
        }

        if(!plot.getOwnerUUID().equals(player.getUniqueId().toString())) {
            Form.at(player, Prefix.ERROR, "You have to be the owner of this plot to remove a builder.");
            return;
        }

        if(args.length < 2) {
            Form.at(player, Prefix.ERROR, "You have to specify a player to add.");
            return;
        }

        OfflinePlayer builder = Bukkit.getPlayer(args[1]);
        if(builder == null) {
            builder = Bukkit.getOfflinePlayer(args[1]);
            if(builder == null) {
                Form.at(player, Prefix.ERROR, args[1] + " has never been on this server.");
                return;
            }
        }

        if(plot.getCanBuildUUIDs().contains(builder.getUniqueId().toString())) {
            Form.at(player, Prefix.ERROR, builder.getName() + " is not a builder in this plot.");
            return;
        }

        plot.removeBuildUUID(builder.getUniqueId().toString());
        Form.at(player, Prefix.SUCCESS, "You have removed " + builder.getName() + " as a builder in this plot.");
    }

    public void executeDelete(Player player) {
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(player.getLocation());
        if(plot == null) {
            Form.at(player, Prefix.ERROR, "You have to be in a plot in order to use this command");
            return;
        }

        if(!plot.getOwnerUUID().equals(player.getUniqueId().toString())) {
            Form.at(player, Prefix.ERROR, "You have to be the owner of this plot to delete it.");
            return;
        }

        if(plot.delete())
            Form.at(player, Prefix.SUCCESS, "You have deleted this plot and it is no longer yours.");
        else
            Form.at(player, Prefix.ERROR, "Something went wrong. Please notify a staff member.");
    }

    public void executeClaim(Player player) {
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(player.getLocation());
        if(plot == null) {
            Form.at(player, Prefix.ERROR, "You have to be in a plot in order to use this command");
            return;
        }

        if(!plot.getOwnerUUID().isEmpty()) {
            Form.at(player, Prefix.ERROR, "Another player already owns this plot");
            return;
        }

        plot.setOwnerUUID(player.getUniqueId().toString());
        Form.at(player, Prefix.SUCCESS, "You have claimed this plot.");
    }

    public void executeInfo(Player player) {
        Plot plot = PlotWorld.getPlotManager().getPlotFromLocation(player.getLocation());
        if(plot == null) {
            Form.at(player, Prefix.ERROR, "You have to be in a plot in order to use this command");
            return;
        }
        OfflinePlayer owner = Bukkit.getOfflinePlayer(UUID.fromString(plot.getOwnerUUID()));
        player.sendMessage(owner.getName() + "'s Plot Info");

        if(owner.isOnline())
            player.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.GREEN + owner.getName());
        else
            player.sendMessage(ChatColor.GOLD + "Owner: " + ChatColor.RED + owner.getName());
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < plot.getCanBuildUUIDs().size(); i++) {
            OfflinePlayer builder = Bukkit.getOfflinePlayer(UUID.fromString(plot.getCanBuildUUIDs().get(i)));
            if(builder.isOnline())
                sb.append(ChatColor.GREEN).append(builder.getName());
            else
                sb.append(ChatColor.RED).append(builder.getName());
            if(i + 1 != plot.getCanBuildUUIDs().size())
                sb.append(ChatColor.WHITE).append(", ");
        }
        player.sendMessage(ChatColor.BLUE + "Builders: " + sb.toString());
        if(plot.getCanBuildUUIDs().contains(player.getUniqueId().toString()) || plot.getOwnerUUID().equals(player.getUniqueId().toString()))
            player.sendMessage(ChatColor.WHITE + "Can Build: " + ChatColor.GREEN + "TRUE");
        else
            player.sendMessage(ChatColor.WHITE + "Can Build: " + ChatColor.RED + "FALSE");
    }

    public void executePlotPlayer(Player player, String[] args) {
        if(args.length < 2) {
            Form.at(player, Prefix.ERROR, "You have to specify a player to add.");
            return;
        }

        OfflinePlayer builder = Bukkit.getPlayer(args[1]);
        if(builder == null) {
            builder = Bukkit.getOfflinePlayer(args[1]);
            if(builder == null) {
                Form.at(player, Prefix.ERROR, args[1] + " has never been on this server.");
                return;
            }
        }

        PlotWorld.getPlotMenu().open(builder, player);
    }
}
