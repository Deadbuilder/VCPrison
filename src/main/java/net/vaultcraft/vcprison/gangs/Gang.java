package net.vaultcraft.vcprison.gangs;

import net.minecraft.util.com.google.gson.Gson;
import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

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
    private static Gson gson = new Gson();

    public Gang(String gangName, String ownerUUID) {
        this.gangName = gangName;
        this.ownerUUID = ownerUUID;
        GangManager.getGangs().put(gangName.toLowerCase(), this);
        new SaveGangTask(this);
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
        if(enemyGangs.contains(gangName))
            enemyGangs.remove(gangName);
        alliedGangs.add(gangName);
    }

    public void removeAlly(String gangName) {
        alliedGangs.remove(gangName);
    }

    public void addEnemy(String gangName) {
        if(enemyGangs.contains(gangName))
            return;
        if(alliedGangs.contains(gangName))
            alliedGangs.remove(gangName);
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

        for(String alliedGang : new ArrayList<>(alliedGangs)) {
            Gang gang = GangManager.getGang(alliedGang);
            if(gang == null) {
                continue;
            }
            gang.removeAlly(gangName);
        }

        for(String enemyGang : new ArrayList<>(enemyGangs)) {
            Gang gang = GangManager.getGang(enemyGang);
            if(gang == null) {
                continue;
            }
            gang.removeEnemy(gangName);
        }

        GangManager.getGangs().remove(gangName);
        memberUUIDs.clear();
        alliedGangs.clear();
        enemyGangs.clear();
        ownerUUID = "";
        Bukkit.broadcastMessage(ChatColor.GRAY + "[" + ChatColor.DARK_PURPLE + "Gangs" + ChatColor.GRAY + "] " + gangName + " has been disbanded!");
        GangManager.getGangsConfig().set("Gangs." + gangName, "_disbanded");

    }

    private class SaveGangTask extends BukkitRunnable {

        private Gang gang;

        public SaveGangTask(Gang gang) {
            this.gang = gang;
            this.runTaskTimer(VCPrison.getInstance(), 1200l, 1200l);
        }

        public void run() {
            if(gang == null || gang.getOwnerUUID().isEmpty()) {
                this.cancel();
                return;
            }
            GangManager.getGangsConfig().set("Gangs." + gang.getGangName(), Gang.gson.toJson(gang));
        }
    }
}
