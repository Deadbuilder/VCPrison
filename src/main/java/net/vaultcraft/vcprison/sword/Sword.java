package net.vaultcraft.vcprison.sword;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tacticalsk8er on 10/5/2014.
 */
public class Sword {

    private Player player;
    private int killstreak = 0;
    private int swordPoints = 0;
    private boolean inUse = false;

    private int kills = 0;
    private int deaths = 0;

    private HashMap<SwordPerk, Integer> perkLevels = new HashMap<>();
    private HashMap<SwordPerk, Boolean> perkToggle = new HashMap<>();

    public Sword(Player player) {
        this.player = player;
        for (SwordPerk perk : SwordPerk.getPerks()) {
            perkLevels.put(perk, perk.getInitLevel());
            if (perk.isToggleable()) {
                perkToggle.put(perk, perk.getInitLevel() == 1);
                if(perk.getInitLevel() == 1)
                    perk.onToggleOn(player);
            }
            if(perk.getInitLevel() > 0) {
                perk.onStart(player, perk.getInitLevel());
                if(player.getInventory().getHeldItemSlot() == 0)
                    perk.onHoverOn(player, perk.getInitLevel());
                else
                    perk.onHoverOff(player, perk.getInitLevel());
            }
        }
    }

    public Sword(Player player, String s) {
        this.player = player;
        if (s.contains(".")) {
            String[] parts = s.split("\\.");
            for (String part : parts) {
                String[] value = part.split("\\|");
                if (value[0].contains("-Toggle")) {
                    String perkName = value[0].replace("-Toggle", "");
                    perkToggle.put(SwordPerk.getPerkFromName(perkName), Boolean.parseBoolean(value[1]));
                    if(Boolean.parseBoolean(value[1]))
                        SwordPerk.getPerkFromName(perkName).onToggleOn(player);
                    continue;
                }
                if(SwordPerk.getPerkFromName(value[0]) != null) {
                    perkLevels.put(SwordPerk.getPerkFromName(value[0]), Integer.parseInt(value[1]));
                    SwordPerk.getPerkFromName(value[0]).onStart(player, Integer.parseInt(value[1]));
                    if(player.getInventory().getHeldItemSlot() == 0 && Integer.parseInt(value[1]) > 0)
                        SwordPerk.getPerkFromName(value[0]).onHoverOn(player, Integer.parseInt(value[1]));
                    else
                        SwordPerk.getPerkFromName(value[0]).onHoverOff(player, Integer.parseInt(value[1]));
                    continue;
                }

                switch (value[0]) {
                    case "KillStreak":
                        killstreak = Integer.parseInt(value[1]);
                        break;
                    case "Kills":
                        kills = Integer.parseInt(value[1]);
                        break;
                    case "Deaths":
                        deaths = Integer.parseInt(value[1]);
                        break;
                    case "Points":
                        swordPoints = Integer.parseInt(value[1]);
                        break;
                }
            }
        }

        for (SwordPerk perk : SwordPerk.getPerks()) {
            if (perkLevels.containsKey(perk))
                continue;
            perkLevels.put(perk, perk.getInitLevel());
            if (perk.isToggleable())
                perkToggle.put(perk, perk.getInitLevel() == 1);
        }
    }

    public ItemStack getSword() {
        ItemStack pick = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = pick.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lV&7&lC&e: &7Prison Sword &e&n[Kills " + kills + "]&r &5Drop to Upgrade"));
        List<String> lore = new ArrayList<>();
        for (SwordPerk perk : perkLevels.keySet()) {
            if (perk.isToggleable())
                if (!perkToggle.get(perk))
                    continue;
            if (perkLevels.get(perk) == 0)
                continue;
            lore = perk.changeLore(player, lore, perkLevels.get(perk));
            itemMeta = perk.changeMeta(player, itemMeta, perkLevels.get(perk));
        }
        itemMeta.addEnchant(Enchantment.DURABILITY, 50, true);
        lore.add("Kills: " + Form.at(kills));
        lore.add("Deaths: " + Form.at(deaths));
        lore.add("KDR: " + new DecimalFormat("#,###.##").format(((double)kills  * 1.0) / (double)deaths));
        itemMeta.setLore(lore);
        pick.setItemMeta(itemMeta);
        return pick;
    }

    public Inventory getStatsMenu() {
        int rows = (int) Math.ceil(((perkLevels.size() + 1.0) / 9.0));
        Inventory inventory = Bukkit.getServer().createInventory(null, 9 * rows, "Sword Perks");
        for (SwordPerk perk : SwordPerk.getPerks()) {
            if (perk.isToggleable()) {
                if (perkLevels.get(perk) == 1) {
                    if (perkToggle.get(perk)) {
                        inventory.addItem(perk.getToggleOff());
                        continue;
                    } else {
                        inventory.addItem(perk.getToggleOn());
                        continue;
                    }
                }
                inventory.addItem(perk.getIcon());
                continue;
            }
            inventory.addItem(perk.getIcon(perkLevels.get(perk)));
        }
        inventory.addItem(getPointsIcon());
        return inventory;
    }

    public ItemStack getPointsIcon() {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lPerk Points: " + swordPoints));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getWarpIcon() {
        ItemStack itemStack = new ItemStack(Material.COMPASS);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lMine Warps"));
        itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&6Click to open mine warps.")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getAddPointItem() {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lRight Click: &2&lAdd Perk Point"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public void addPerkLevel(Player player, SwordPerk perk) {
        int level = perkLevels.get(perk) + 1;
        perkLevels.remove(perk);
        perkLevels.put(perk, level);
        perk.onPurchase(player, getPerkLevel(perk));
        swordPoints -= perk.getCost();
        Form.at(player, Prefix.SUCCESS, "You bought one level of " + perk.getName());
        player.getInventory().setItem(0, getSword());
    }

    public void addPerkToggle(Player player, SwordPerk perk) {
        perkLevels.remove(perk);
        perkLevels.put(perk, 1);
        perkToggle.remove(perk);
        perkToggle.put(perk, true);
        perk.onToggleOn(player);
        perk.onPurchase(player, 1);
        swordPoints -= perk.getCost();
        Form.at(player, Prefix.SUCCESS, "You bought " + perk.getName() + ". You can toggle this on and off in the perk menu.");
        player.getInventory().setItem(0, getSword());
    }

    public void togglePerk(Player player, SwordPerk perk) {
        boolean toggle = !perkToggle.get(perk);
        perkToggle.remove(perk);
        perkToggle.put(perk, toggle);
        if(toggle) {
            perk.onToggleOn(player);
            player.getInventory().setItem(0, getSword());
            Form.at(player, Prefix.SUCCESS, "You toggled " + perk.getName() + " on!");
        } else {
            perk.onToggleOff(player);
            player.getInventory().setItem(0, getSword());
            Form.at(player, Prefix.SUCCESS, "You toggled " + perk.getName() + " off!");
        }
    }

    public void levelUp() {
        kills++;
        killstreak++;
        swordPoints++;
        player.sendMessage("");
    }

    public void reset() {
        killstreak = 0;
        swordPoints = 0;
        deaths++;
        perkLevels.clear();
        perkToggle.clear();
        for (SwordPerk perk : SwordPerk.getPerks()) {
            perkLevels.put(perk, perk.getInitLevel());
            if (perk.isToggleable()) {
                perkToggle.put(perk, perk.getInitLevel() == 1);
                if(perk.getInitLevel() == 1)
                    perk.onToggleOn(player);
            }
            if(perk.getInitLevel() > 0) {
                perk.onStart(player, perk.getInitLevel());
                if(player.getInventory().getHeldItemSlot() == 0)
                    perk.onHoverOn(player, perk.getInitLevel());
                else
                    perk.onHoverOff(player, perk.getInitLevel());
            }
        }
    }

    public int getPerkLevel(SwordPerk perk) {
        if (perk == null)
            return 0;

        return perkLevels.get(perk);
    }

    public boolean getToggle(SwordPerk perk) {
        if(perk == null)
            return false;
        return perkToggle.get(perk);
    }

    public int getSwordPoints() {
        return swordPoints;
    }

    public void setSwordPoints(int pickPoints) {
        this.swordPoints = pickPoints;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
        if(!inUse) {
            player.getInventory().setItem(0, new ItemStack(Material.AIR));
        } else {
            player.getInventory().setItem(0, getSword());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KillStreak|").append(killstreak).append(".");
        sb.append("Kills|").append(kills).append(".");
        sb.append("Deaths|").append(deaths).append(".");
        sb.append("Points|").append(swordPoints).append(".");
        for (SwordPerk perk : perkLevels.keySet()) {
            if (perk.isToggleable())
                sb.append(perk.getNoColorName()).append("-Toggle|").append(perkToggle.get(perk)).append(".");

            sb.append(perk.getNoColorName()).append("|").append(perkLevels.get(perk)).append(".");
        }

        if (sb.toString().endsWith("."))
            sb.substring(0, sb.length()-1);

        return sb.toString();
    }
}
