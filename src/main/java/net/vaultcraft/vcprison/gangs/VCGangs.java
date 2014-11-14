package net.vaultcraft.vcprison.gangs;

import mkremins.fanciful.FancyMessage;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Created by tacticalsk8er on 10/1/2014.
 */
public class VCGangs extends ICommand {

    private HashMap<String, List<String>> gangInvites = new HashMap<>();
    private HashMap<String, List<String>> allyInvites = new HashMap<>();
    private HashMap<String, List<String>> neutralRequests = new HashMap<>();

    public VCGangs(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        if (args.length == 0) {
            this.sendHelp(player);
            return;
        }

        switch (args[0]) {
            case "help":
                sendHelp(player);
                return;
            case "create":
                createGang(player, args);
                return;
            case "info":
                infoGang(player, args);
                return;
            case "invite":
                inviteGang(player, args);
                return;
            case "join":
                joinGang(player, args);
                return;
            case "kick":
                removeGang(player, args);
                return;
            case "leave":
                leaveGang(player);
                return;
            case "ally":
                allyGang(player, args);
                return;
            case "enemy":
                enemyGang(player, args);
                return;
            case "ff":
                ffGang(player);
                break;
            case "disband":
                if(User.fromPlayer(player).getGroup().hasPermission(Group.ADMIN))
                    disbandGang(player, args);
                return;
            default:
                Form.at(player, Prefix.ERROR, "Please enter a correct argument.");
                sendHelp(player);
                break;
        }
    }

    public void sendHelp(Player sender) {
        sender.sendMessage(" ");
        new FancyMessage("=== VaultCraft Gangs Help ===").color(ChatColor.DARK_PURPLE).style(ChatColor.UNDERLINE).send(sender);
        new FancyMessage("Hover - Command Info | Click - Input Command").color(ChatColor.GRAY).send(sender);
        new FancyMessage("/gang help").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang help").tooltip("Shows this text.").send(sender);
        new FancyMessage("/gang create").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang create").tooltip("Creates a new gang if you are not apart of one.")
                .then(" <name>").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang create").tooltip("<name> = Name of the gang you want to create.").send(sender);
        new FancyMessage("/gang info").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang info").tooltip("Shows info about your gang or the specified one.")
                .then(" [name]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang info").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang info").tooltip("[name] = Name of the gang you want info on. Optional").send(sender);
        new FancyMessage("/gang invite").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang invite").tooltip("Invites a player to your gang.")
                .then(" <player_name>").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang invite").tooltip("<player_name> = Name of the player you want to invite.")
                .then(" [*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC, ChatColor.BOLD).suggest("/gang invite").tooltip("NOTE | You need to be the owner of the gang to use this command.").send(sender);
        new FancyMessage("/gang join").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang join").tooltip("Joins the gang you specify if you have been invited.")
                .then(" <gang_name>").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang join").tooltip("<gang_name> = Name of the gang you want to join.").send(sender);
        new FancyMessage("/gang kick").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang kick").tooltip("Kicks a member from your gang.")
                .then(" <name>").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang kick").tooltip("<name> = Name of the player/member you want to kick.")
                .then(" [*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC, ChatColor.BOLD).suggest("/gang kick").tooltip("NOTE | You need to be the owner of the gang to use this command.").send(sender);
        new FancyMessage("/gang leave").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang leave").tooltip("Leave your current gang.")
                .then(" [*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC, ChatColor.BOLD, ChatColor.UNDERLINE).suggest("/gang leave").tooltip("WARNING | If you are the owner of the gang, this command will disband your gang.").send(sender);
        new FancyMessage("/gang ally").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang ally").tooltip("Allies with the specified gang.")
                .then(" <gang_name>").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang ally").tooltip("<gang_name> = Name of the gang you want to ally with.")
                .then(" [*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang ally").tooltip("NOTE | Other gang owner needs to be online and accept your allying invitation.")
                .then("[*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang ally").tooltip("NOTE | Use this command to un-ally with a gang.")
                .then("[*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC, ChatColor.BOLD).suggest("/gang ally").tooltip("NOTE | You need to be the owner of the gang to use this command.").send(sender);
        new FancyMessage("/gang enemy").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang enemy").tooltip("Become enemies with the specified gang.")
                .then(" <gang_name>").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang enemy").tooltip("<gang_name> = Name of the gang you want to become enemies with.")
                .then(" [*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang enemy").tooltip("NOTE | Use this command to become neutral with a gang.")
                .then("[*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang enemy").tooltip("NOTE | In order to become neutral again, the other gang owner needs to be online and accept your offer to become neutral.")
                .then("[*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC, ChatColor.BOLD).suggest("/gang enemy").tooltip("NOTE | You need to be the owner of the gang to use this command.").send(sender);
        if(User.fromPlayer(sender).getGroup().hasPermission(Group.ADMIN))
            new FancyMessage("/gang disband").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang disband").tooltip("Disband the specified gang.")
                    .then(" <gang_name>").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).suggest("/gang disband").tooltip("<gang_name> = Name of the gang you want to disband.")
                    .then(" [*]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC, ChatColor.BOLD).suggest("/gang disband").tooltip("NOTE | This command can only be used by admins.").send(sender);
        sender.sendMessage(" ");
    }

    public void createGang(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to specify a gang name.");
            return;
        }

        String gangName = args[1];
        if (gangName.length() > 15) {
            Form.at(sender, Prefix.ERROR, "Your gang can only be 15 characters max!");
            return;
        }

        for (Map.Entry entry : GangManager.getGangs().entrySet()) {
            String name = (String) entry.getKey();
            if (name.equals(gangName)) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to pick a unique gang name.");
                return;
            }
            Gang gang = (Gang) entry.getValue();
            if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You can't be in another gang.");
                return;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(sender.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You can't be in another gang.");
                    return;
                }
            }
        }

        for (char c : gangName.toCharArray()) {
            if (Character.isLetterOrDigit(c))
                continue;

            Form.at(sender, Prefix.ERROR, "Your gang name can only have numbers and letters!");
            return;
        }

        GangManager.getGangs().put(gangName, new Gang(gangName, sender.getUniqueId().toString()));
        sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You created the " + gangName + " gang.");
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "Gangs" + ChatColor.GRAY + "] " + gangName + " has been created.");
    }

    public void infoGang(Player sender, String[] args) {
        if (args.length < 2) {
            for (Gang gang : GangManager.getGangs().values()) {
                if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                    sendGangInfo(sender, gang);
                    return;
                }
                for (String memberUUID : gang.getMemberUUIDs()) {
                    if (memberUUID.equals(sender.getUniqueId().toString())) {
                        sendGangInfo(sender, gang);
                        return;
                    }
                }
            }
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You are not apart of a gang.");
            return;
        }

        String gangName = args[1];
        Player player = Bukkit.getPlayer(args[1]);

        if(player != null) {
            for (Gang gang : GangManager.getGangs().values()) {
                if (gang.getOwnerUUID().equals(player.getUniqueId().toString())) {
                    sendGangInfo(sender, gang);
                    return;
                }
                for (String memberUUID : gang.getMemberUUIDs()) {
                    if (memberUUID.equals(player.getUniqueId().toString())) {
                        sendGangInfo(sender, gang);
                        return;
                    }
                }
            }
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + player.getName() + " is not apart of a gang.");
            return;
        }

        if (!GangManager.getGangs().containsKey(gangName)) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + gangName + " does not exist.");
            return;
        }

        sendGangInfo(sender, GangManager.getGangs().get(gangName));
    }

    public void inviteGang(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to specify a player to invite.");
            return;
        }

        Gang senderGang = null;

        for (Gang gang : GangManager.getGangs().values()) {
            if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                senderGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(sender.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be the owner of the gang in order to use this command.");
                    return;
                }
            }
        }

        if (senderGang == null) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be apart of a gang and be the owner of that gang to use this command.");
            return;

        }

        Player player = Bukkit.getPlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "That player is not online.");
            return;
        }

        if (gangInvites.containsKey(player.getName())) {
            List<String> invites = gangInvites.get(player.getName());
            if (invites.contains(senderGang.getGangName())) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You already sent " + player.getName() + " an invite.");
                return;
            }
            invites.add(senderGang.getGangName());
            gangInvites.put(player.getName(), invites);
        } else {
            List<String> invites = new ArrayList<>();
            invites.add(senderGang.getGangName());
            gangInvites.put(player.getName(), invites);
        }

        new FancyMessage(senderGang.getGangName() + " has invited you to join!").color(ChatColor.LIGHT_PURPLE).style(ChatColor.BOLD)
                .then(" [Click here to join]").color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).command("/gang join " + senderGang.getGangName()).send(player);
        sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + player.getName() + " has been invited to your gang.");
    }

    public void joinGang(Player sender, String[] args) {

        if (args.length < 2) {
            if (!gangInvites.containsKey(sender.getName())) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You have no pending invites.");
                return;
            }
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.DARK_PURPLE + "===Gang Invites===");
            sender.sendRawMessage(ChatColor.GRAY + "Click on gang name to join them.");
            for (String s : gangInvites.get(sender.getName()))
                new FancyMessage(s).color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).command("/gang join " + s).tooltip("Click to join " + s + " gang.").send(sender);
            sender.sendMessage(" ");
            return;
        }

        for (Gang gang : GangManager.getGangs().values()) {
            if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You are already apart of a gang. You need to leave your gang first before you can join.");
                return;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(sender.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You are already apart of a gang. You need to leave your gang first before you can join.");
                    return;
                }
            }
        }

        if (!gangInvites.containsKey(sender.getName())) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You have no pending invites.");
            return;
        }

        List<String> invites = gangInvites.get(sender.getName());
        String gangName = args[1];
        Gang gang = null;
        for (String s : invites) {
            if (s.equalsIgnoreCase(gangName)) {
                gang = GangManager.getGangs().get(gangName);
                break;
            }
        }
        if (gang == null) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "That gang hasn't invited you. Do /gang join to see what gangs invited you to join.");
            return;
        }
        gang.addMember(sender.getUniqueId().toString());
        sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have joined the " + gang.getGangName() + " gang.");
        gangInvites.remove(sender.getName());
    }

    public void allyGang(Player sender, String[] args) {

        Gang senderGang = null;

        for (Gang gang : GangManager.getGangs().values()) {
            if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                senderGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(sender.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be the owner of the gang in order to use this command.");
                    return;
                }
            }
        }

        if (senderGang == null) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be apart of a gang and be the owner of that gang to use this command.");
            return;
        }

        if (args.length < 2) {
            if (!allyInvites.containsKey(senderGang.getGangName())) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You have no pending ally invites.");
                return;
            }
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.LIGHT_PURPLE + "===Ally Invites===");
            sender.sendRawMessage(ChatColor.GRAY + "Click on gang name to become allies with them.");
            for (String s : allyInvites.get(senderGang.getGangName()))
                new FancyMessage(s).color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).command("/gang ally " + s).tooltip("Click to become allies with " + s + " gang.").send(sender);
            sender.sendMessage(" ");
            return;
        }

        if (!GangManager.getGangs().containsKey(args[1])) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "That gang does not exist.");
            return;
        }

        Gang gang = GangManager.getGangs().get(args[1]);

        if (gang.getGangName().equals(senderGang.getGangName())) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You cannot ally yourself!");
            return;
        }

        if (gang.getAlliedGangs().contains(senderGang.getGangName())) {
            gang.removeAlly(senderGang.getGangName());
            senderGang.removeAlly(gang.getGangName());
            sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have removed " + gang.getGangName() + " as an ally.");
            return;
        }

        if (allyInvites.containsKey(senderGang.getGangName())) {
            if (allyInvites.get(senderGang.getGangName()).contains(gang.getGangName())) {
                senderGang.addAlly(gang.getGangName());
                gang.addAlly(senderGang.getGangName());
                sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have become allies with " + gang.getGangName() + ".");
                if (Bukkit.getPlayer(UUID.fromString(gang.getOwnerUUID())) != null)
                    Bukkit.getPlayer(UUID.fromString(gang.getOwnerUUID())).sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + senderGang.getGangName() + " has agreed to become allies with you.");
                List<String> invites = allyInvites.get(senderGang.getGangName());
                invites.remove(gang.getGangName());
                allyInvites.put(senderGang.getGangName(), invites);
                return;
            }
        }

        if (allyInvites.containsKey(gang.getGangName())) {
            List<String> invites = allyInvites.get(gang.getGangName());
            if(invites.contains(senderGang.getGangName())) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You have already invited " + gang.getGangName() + " to become allies.");
                return;
            }
            invites.add(senderGang.getGangName());
            allyInvites.put(gang.getGangName(), invites);
        } else {
            List<String> invites = new ArrayList<>();
            invites.add(senderGang.getGangName());
            allyInvites.put(gang.getGangName(), invites);
        }

        sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have invited " + gang.getGangName() + " to become allies.");
        if (Bukkit.getPlayer(UUID.fromString(gang.getOwnerUUID())) != null)
            new FancyMessage("Notification: ").color(ChatColor.YELLOW)
                    .then(senderGang.getGangName() + " has requested to become allies with you.").color(ChatColor.WHITE)
                    .then("[Click here to ally]").command("/gang ally " + senderGang.getGangName()).send(Bukkit.getPlayer(UUID.fromString(gang.getOwnerUUID())));
    }

    public void enemyGang(Player sender, String[] args) {

        Gang senderGang = null;

        for (Gang gang : GangManager.getGangs().values()) {
            if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                senderGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(sender.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be the owner of the gang in order to use this command.");
                    return;
                }
            }
        }

        if (senderGang == null) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be apart of a gang and be the owner of that gang to use this command.");
            return;
        }

        if (args.length < 2) {
            if (!neutralRequests.containsKey(senderGang.getGangName())) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You have no pending neutral requests.");
                return;
            }
            sender.sendMessage(" ");
            sender.sendMessage(ChatColor.DARK_PURPLE + "===Neutral Requests===");
            sender.sendRawMessage(ChatColor.GRAY + "Click on gang name to become neutral with them.");
            for (String s : neutralRequests.get(senderGang.getGangName()))
                new FancyMessage(s).color(ChatColor.LIGHT_PURPLE).style(ChatColor.ITALIC).command("/gang enemy " + s).tooltip("Click to become allies with " + s + " gang.").send(sender);
            sender.sendMessage(" ");
            return;
        }

        if (!GangManager.getGangs().containsKey(args[1])) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "That gang does not exist.");
            return;
        }

        Gang gang = GangManager.getGangs().get(args[1]);

        if (!gang.getEnemyGangs().contains(senderGang.getGangName())) {
            gang.addEnemy(senderGang.getGangName());
            senderGang.addEnemy(gang.getGangName());
            sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have added " + gang.getGangName() + " as an enemy.");
            return;
        }

        if (neutralRequests.containsKey(senderGang.getGangName())) {
            if (neutralRequests.get(senderGang.getGangName()).contains(gang.getGangName())) {
                senderGang.removeEnemy(gang.getGangName());
                gang.removeEnemy(senderGang.getGangName());
                sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have become neutral with " + gang.getGangName() + ".");
                if (Bukkit.getPlayer(UUID.fromString(gang.getOwnerUUID())) != null)
                    Bukkit.getPlayer(UUID.fromString(gang.getOwnerUUID())).sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + senderGang.getGangName() + " has agreed to become neutral with you.");
                List<String> invites = neutralRequests.get(senderGang.getGangName());
                invites.remove(gang.getGangName());
                neutralRequests.put(senderGang.getGangName(), invites);
                return;
            }
        }

        if (neutralRequests.containsKey(gang.getGangName())) {
            List<String> invites = neutralRequests.get(gang.getGangName());
            if(invites.contains(senderGang.getGangName())) {
                sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You have already requested " + gang.getGangName() + " to become neutral.");
                return;
            }
            invites.add(senderGang.getGangName());
            neutralRequests.put(gang.getGangName(), invites);
        } else {
            List<String> invites = new ArrayList<>();
            invites.add(senderGang.getGangName());
            neutralRequests.put(gang.getGangName(), invites);
        }

        sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have requested " + gang.getGangName() + " to become neutral.");
        if (Bukkit.getPlayer(UUID.fromString(gang.getOwnerUUID())) != null)
            new FancyMessage("Notification: ").color(ChatColor.YELLOW)
                    .then(senderGang.getGangName() + " has requested to become neutral with you.").color(ChatColor.WHITE)
                    .then("[Click here to become neutral]").command("/gang enemy " + senderGang.getGangName()).send(Bukkit.getPlayer(UUID.fromString(gang.getOwnerUUID())));
    }

    public void removeGang(Player sender, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to specify and player to remove.");
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
        if (player == null) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "That player has never logged into this server.");
            return;
        }

        Gang senderGang = null;

        for (Gang gang : GangManager.getGangs().values()) {
            if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                senderGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(sender.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be the owner of the gang in order to use this command.");
                    return;
                }
            }
        }

        if (senderGang == null) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be apart of a gang and be the owner of that gang to use this command.");
            return;
        }

        for (String memberUUID : senderGang.getMemberUUIDs()) {
            if (memberUUID.equals(player.getUniqueId().toString())) {
                senderGang.removeMember(memberUUID);
                sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have kicked " + player.getName() + " from your gang.");
                if (player.isOnline())
                    player.getPlayer().sendMessage(ChatColor.YELLOW + "Notification: " + ChatColor.WHITE + "You have been kicked from your gang.");
                return;
            }
        }

        sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + player.getName() + " is not apart of your gang.");
    }

    public void leaveGang(Player sender) {

        for (Gang gang : GangManager.getGangs().values()) {
            if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                gang.disband();
                sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have disbanded your gang.");
                return;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(sender.getUniqueId().toString())) {
                    gang.removeMember(memberUUID);
                    sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + "You have left your gang.");
                    return;
                }
            }
        }

        sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You are not apart of a gang.");
    }

    public void ffGang(Player sender) {
        Gang senderGang = null;

        for (Gang gang : GangManager.getGangs().values()) {
            if (gang.getOwnerUUID().equals(sender.getUniqueId().toString())) {
                senderGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(sender.getUniqueId().toString())) {
                    sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be the owner of the gang in order to use this command.");
                    return;
                }
            }
        }

        if (senderGang == null) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to be apart of a gang and be the owner of that gang to use this command.");
            return;
        }

        senderGang.setFriendlyFire(!senderGang.isFriendlyFire());

        if(senderGang.isFriendlyFire())
            Form.at(sender, Prefix.SUCCESS, "Friendly fire is now turned on.");
        else
            Form.at(sender, Prefix.SUCCESS, "Friendly fire is now turned off.");
    }

    public void disbandGang(Player sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + "You need to specify a gang to disband.");
            return;
        }

        if(!GangManager.getGangs().containsKey(args[1])) {
            sender.sendMessage(ChatColor.RED + "Error: " + ChatColor.WHITE + args[1] + " does not exist.");
            return;
        }

        Gang gang = GangManager.getGangs().get(args[1]);
        sender.sendMessage(ChatColor.GREEN + "Success: " + ChatColor.WHITE + gang.getGangName() + " has been disbanded.");
        gang.disband();
    }

    public void sendGangInfo(Player sender, Gang gang) {
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.DARK_PURPLE + "===" + gang.getGangName() + " Gang Info===");
        OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(gang.getOwnerUUID()));
        if(player.isOnline())
            sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.ITALIC + "Owner: " + ChatColor.GREEN + player.getName());
        else
            sender.sendMessage(ChatColor.GOLD.toString() + ChatColor.ITALIC + "Owner: " +  ChatColor.RED + player.getName());
        StringBuilder sb = new StringBuilder();
        sb.append(ChatColor.LIGHT_PURPLE).append(ChatColor.ITALIC).append("Members: ");
        for (String memberUUID : gang.getMemberUUIDs()) {
            player = Bukkit.getOfflinePlayer(UUID.fromString(memberUUID));
            if(player.isOnline())
                sb.append(ChatColor.GREEN).append(player.getName()).append(ChatColor.WHITE).append(", ");
            else
                sb.append(ChatColor.RED).append(player.getName()).append(ChatColor.WHITE).append(", ");
        }
        sender.sendMessage(sb.toString());
        StringBuilder sb1 = new StringBuilder();
        sb1.append(ChatColor.AQUA).append(ChatColor.ITALIC).append("Allies: ");
        for (String allyName : gang.getAlliedGangs()) {
            sb1.append(allyName).append(", ");
        }
        sender.sendMessage(sb1.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(ChatColor.RED).append(ChatColor.ITALIC).append("Enemies: ");
        for (String enemyName : gang.getEnemyGangs()) {
            sb2.append(enemyName).append(", ");
        }
        sender.sendMessage(sb2.toString());
        sender.sendMessage(" ");
    }
}
