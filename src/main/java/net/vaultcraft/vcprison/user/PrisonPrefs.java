package net.vaultcraft.vcprison.user;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by tacticalsk8er on 10/28/2014.
 */
public class PrisonPrefs {

    private static HashMap<Player, PrisonPrefs> prefsMap = new HashMap<>();

    boolean explosions = true;
    boolean guardMessages = true;
    boolean chatMessages = true;
    boolean privateMessages = true;
    boolean autoSell = true;
    boolean ffaMessages = true;
    boolean pickaxeMessages = true;
    boolean swordMessages = true;

    public PrisonPrefs() {

    }

    public PrisonPrefs(String s) {

    }

    public boolean isExplosions() {
        return explosions;
    }

    public boolean isGuardMessages() {
        return guardMessages;
    }

    public boolean isChatMessages() {
        return chatMessages;
    }

    public boolean isPrivateMessages() {
        return privateMessages;
    }

    public boolean isFfaMessages() {
        return ffaMessages;
    }

    public boolean isAutoSell() {
        return autoSell;
    }

    public boolean isPickaxeMessages() {
        return pickaxeMessages;
    }

    public boolean isSwordMessages() {
        return swordMessages;
    }

    public void setExplosions(boolean explosions) {
        this.explosions = explosions;
    }

    public void setGuardMessages(boolean guardMessages) {
        this.guardMessages = guardMessages;
    }

    public void setChatMessages(boolean chatMessages) {
        this.chatMessages = chatMessages;
    }

    public void setPrivateMessages(boolean privateMessages) {
        this.privateMessages = privateMessages;
    }

    public void setFfaMessages(boolean ffaMessages) {
        this.ffaMessages = ffaMessages;
    }

    public void setAutoSell(boolean autoSell) {
        this.autoSell = autoSell;
    }

    public void setPickaxeMessages(boolean pickaxeMessages) {
        this.pickaxeMessages = pickaxeMessages;
    }

    public void setSwordMessages(boolean swordMessages) {
        this.swordMessages = swordMessages;
    }

    @Override
    public String toString() {
        return "Explosions:" + explosions + ",GuardMessages:" + guardMessages + ",ChatMessages:" + chatMessages
                + ",PrivateMessages:" + privateMessages + ",AutoSell:" + autoSell + ",FFAMessages:" + ffaMessages
                + ",PickaxeMessages:" + pickaxeMessages + ",SwordMessages:" + swordMessages;
    }
}
