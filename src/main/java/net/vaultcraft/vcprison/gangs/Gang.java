package net.vaultcraft.vcprison.gangs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by tacticalsk8er on 10/1/2014.
 */
public class Gang {
    private String gangName;
    private String ownerUUID;
    private List<String> memberUUIDs = new ArrayList<>();
    private List<String> alliedGangs = new ArrayList<>();
    private List<String> enemyGangs = new ArrayList<>();
    private boolean friendlyFire = false;

    public Gang(String gangName, String ownerUUID) {
        this.gangName = gangName;
        this.ownerUUID = ownerUUID;
        GangManager.getGangs().put(gangName.toLowerCase(), this);
    }

    public String getGangName() {
        return gangName;
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public List<String> getMemberUUIDs() {
        return memberUUIDs;
    }

    public List<String> getAlliedGangs() {
        return alliedGangs;
    }

    public List<String> getEnemyGangs() {
        return enemyGangs;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public void addMember(String memberUUID) {
        if(memberUUIDs.contains(memberUUID))
            return;
        memberUUIDs.add(memberUUID);
    }

    public void removeMember(String memberUUID) {
        memberUUIDs.remove(memberUUID);
    }

    public void addAlly(String gangName) {
        if(alliedGangs.contains(gangName))
            return;
        alliedGangs.add(gangName);
    }

    public void removeAlly(String gangName) {
        alliedGangs.remove(gangName);
    }

    public void addEnemy(String gangName) {
        if(enemyGangs.contains(gangName))
            return;
        enemyGangs.add(gangName);
    }

    public void removeEnemy(String gangName) {
        enemyGangs.remove(gangName);
    }

    public void disband() {
        for(String memberUUID : memberUUIDs) {
            if(Bukkit.getPlayer(UUID.fromString(memberUUID)) != null)
                Bukkit.getPlayer(UUID.fromString(memberUUID)).sendMessage(ChatColor.YELLOW + "Notification: " + ChatColor.WHITE + "Your gang has been disbanded.");
        }

        for(String alliedGang : alliedGangs) {
            Gang gang = GangManager.getGang(alliedGang.toLowerCase());
            gang.removeAlly(gangName);
        }

        for(String enemyGang : enemyGangs) {
            Gang gang = GangManager.getGang(enemyGang.toLowerCase());
            gang.removeEnemy(gangName);
        }

        GangManager.getGangs().remove(gangName.toLowerCase());
        memberUUIDs.clear();
        alliedGangs.clear();
        enemyGangs.clear();
        ownerUUID = "";
        Bukkit.broadcastMessage("[" + ChatColor.BLUE + "Gangs" + ChatColor.WHITE + "] " + gangName + " has disbanded!");
        gangName = "";
    }
}
