package net.vaultcraft.vcprison.pickaxe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/3/2014.
 */
public class PickaxePerk {

    private static List<PickaxePerk> perks = new ArrayList<>();

    private String name;
    private String noColorName;
    private List<String> lore;
    private int cost;
    private int initLevel;
    private int maxLevel;
    private boolean togglable;

    private Material icon;
    private Material toggleOn;
    private Material toggleOff;

    public static void addPerk(PickaxePerk pickaxePerk, int order) {
        perks.add(order, pickaxePerk);
    }

    public static List<PickaxePerk> getPerks() {
        return perks;
    }

    public static PickaxePerk getPerkFromName(String name) {
        for (PickaxePerk perk : getPerks()) {
            if (perk.getNoColorName().equalsIgnoreCase(name))
                return perk;
        }
        return null;
    }

    public PickaxePerk(Material icon, String name, int cost, int initLevel, int maxLevel, String... lore) {
        this.icon = icon;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.noColorName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name));
        List<String> loreList = new ArrayList<>();
        for (String s : lore)
            loreList.add(ChatColor.translateAlternateColorCodes('&', s));
        loreList.add("Cost: " + cost);
        if (maxLevel >= 0)
            loreList.add("Max Level: " + maxLevel);
        this.lore = loreList;
        this.cost = cost;
        this.initLevel = initLevel;
        this.maxLevel = maxLevel;

        this.toggleOn = null;
        this.toggleOff = null;
        this.togglable = false;
    }

    public PickaxePerk(Material icon, Material toggleOn, Material toggleOff, String name, int cost, boolean initState, String... lore) {
        this.icon = icon;
        this.toggleOn = toggleOn;
        this.toggleOff = toggleOff;
        this.name = ChatColor.translateAlternateColorCodes('&', name);
        this.noColorName = ChatColor.stripColor(ChatColor.translateAlternateColorCodes('&', name));
        List<String> loreList = new ArrayList<>();
        for (String s : lore)
            loreList.add(ChatColor.translateAlternateColorCodes('&', s));
        loreList.add("Cost: " + cost);
        this.lore = loreList;
        this.cost = cost;
        this.togglable = true;
        if (initState)
            this.initLevel = 1;
        else
            this.initLevel = 0;
        this.maxLevel = -1;
    }

    public String getName() {
        return name;
    }

    public String getNoColorName() {
        return noColorName;
    }

    public int getCost() {
        return cost;
    }

    public int getInitLevel() {
        return initLevel;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public List<String> getLore() {
        return lore;
    }

    public ItemStack getIcon() {
        ItemStack itemStack = new ItemStack(icon);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getIcon(int level) {
        level++;
        ItemStack itemStack = new ItemStack(icon);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (level > maxLevel && maxLevel >= 0)
            itemMeta.setDisplayName(name + " Max");
        else
            itemMeta.setDisplayName(name + " " + level);
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getToggleOff() {
        ItemStack itemStack = new ItemStack(toggleOn);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Toggle Off " + name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemStack getToggleOn() {
        ItemStack itemStack = new ItemStack(toggleOff);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Toggle On " + name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean isTogglable() {
        return togglable;
    }

    //Modifiers

    public List<String> changeLore(Player player, List<String> lore, int level) {
        return lore;
    }

    public ItemMeta changeMeta(Player player, ItemMeta itemMeta, int level) {
        return itemMeta;
    }

    public void onHoverOn(Player player, int level) {

    }

    public void onHoverOff(Player player, int level) {

    }

    public void onPurchase(Player player, int level) {

    }

    public void onToggleOn(Player player) {

    }

    public void onToggleOff(Player player) {

    }

    public ItemStack onBreak(Player player, BlockBreakEvent event, ItemStack itemStack, int level) {
        return itemStack;
    }

    public void onStart(Player player, int level) {

    }

    public void onEnd(Player player) {

    }
}
