package net.vaultcraft.vcprison.pickaxe;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.Group;
import net.vaultcraft.vcutils.user.User;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/3/2014.
 */
public class Pickaxe {

    private Player player;
    private int level = 1;
    private int blocksMined = 0;
    private long totalBlocksMined = 0;
    private int pickPoints = 0;
    private boolean inUse = true;

    private HashMap<PickaxePerk, Integer> perkLevels = new HashMap<>();
    private HashMap<PickaxePerk, Boolean> perkToggle = new HashMap<>();

    public Pickaxe(Player player) {
        this.player = player;
        for (PickaxePerk perk : PickaxePerk.getPerks()) {
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
        player.getInventory().setItem(0, getPickaxe());
    }

    public Pickaxe(Player player, String s) {
        this.player = player;
        if (s.contains(".")) {
            String[] parts = s.split("\\.");
            for (String part : parts) {
                String[] value = part.split("\\|");
                if (value[0].contains("-Toggle")) {
                    String perkName = value[0].replace("-Toggle", "");
                    perkToggle.put(PickaxePerk.getPerkFromName(perkName), Boolean.parseBoolean(value[1]));
                    if(Boolean.parseBoolean(value[1]))
                        PickaxePerk.getPerkFromName(perkName).onToggleOn(player);
                    continue;
                }
                if(PickaxePerk.getPerkFromName(value[0]) != null) {
                    perkLevels.put(PickaxePerk.getPerkFromName(value[0]), Integer.parseInt(value[1]));
                    PickaxePerk.getPerkFromName(value[0]).onStart(player, Integer.parseInt(value[1]));
                    if(player.getInventory().getHeldItemSlot() == 0 && Integer.parseInt(value[1]) > 0)
                        PickaxePerk.getPerkFromName(value[0]).onHoverOn(player, Integer.parseInt(value[1]));
                    else
                        PickaxePerk.getPerkFromName(value[0]).onHoverOff(player, Integer.parseInt(value[1]));
                    continue;
                }

                switch (value[0]) {
                    case "Level":
                        level = Integer.parseInt(value[1]);
                        break;
                    case "BlocksMined":
                        blocksMined = Integer.parseInt(value[1]);
                        break;
                    case "TotalBlocksMined":
                        totalBlocksMined = Long.parseLong(value[1]);
                    case "Points":
                        pickPoints = Integer.parseInt(value[1]);
                        break;
                }
            }
        }

        for (PickaxePerk perk : PickaxePerk.getPerks()) {
            if (perkLevels.containsKey(perk))
                continue;
            perkLevels.put(perk, perk.getInitLevel());
            if (perk.isToggleable())
                perkToggle.put(perk, perk.getInitLevel() == 1);
        }
        player.getInventory().setItem(0, getPickaxe());
    }

    public void reset() {

        this.level = 1;
        this.blocksMined = 0;
        this.pickPoints = 0;

        for (PickaxePerk perk : PickaxePerk.getPerks()) {
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
        player.getInventory().setItem(0, getPickaxe());
    }

    public ItemStack getPickaxe() {
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMeta = pick.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lV&7&lC&e: &7Prison Pickaxe &e&n[Level " + level + "]&r &5&oExp: " + Form.at(blocksMined) + " / " + Form.at(toNextLevel(level))));
        List<String> lore = new ArrayList<>();
        for (PickaxePerk perk : perkLevels.keySet()) {
            if (perk.isToggleable())
                if (!perkToggle.get(perk))
                    continue;
            if (perkLevels.get(perk) == 0)
                continue;
            lore = perk.changeLore(player, lore, perkLevels.get(perk));
            itemMeta = perk.changeMeta(player, itemMeta, perkLevels.get(perk));
        }
        itemMeta.addEnchant(Enchantment.DURABILITY, 50, true);
        lore.add("Exp: " + Form.at(blocksMined) + " / " + Form.at(toNextLevel(level)));
        lore.add("Blocks Mined: " + Form.at(totalBlocksMined));
        itemMeta.setLore(lore);
        pick.setItemMeta(itemMeta);
        return pick;
    }

    public int toNextLevel(int level) {
        if (level < 50)
            return (600 + (600 * (level - 1)));
        else
            return (30000 + (1500 * (level - 50)));
    }

    public float getDonorMultiplier(User user) {
        if(user.getGroup().hasPermission(Group.ENDERDRAGON))
            return 2.5f;
        if(user.getGroup().hasPermission(Group.WITHER))
            return 2;
        if(user.getGroup().hasPermission(Group.ENDERMAN))
            return 1.8f;
        if(user.getGroup().hasPermission(Group.SKELETON))
            return 1.6f;
        if(user.getGroup().hasPermission(Group.SLIME))
            return 1.4f;
        if(user.getGroup().hasPermission(Group.WOLF))
            return 1.2f;
        return 1;
    }

    public void mineBlock(Material material) {
        blocksMined += Math.ceil((BlockExp.fromMaterial(material).getExp() * getDonorMultiplier(User.fromPlayer(player))));
        totalBlocksMined++;
        if (blocksMined >= toNextLevel(level)) {
            blocksMined = 0;
            level++;
            pickPoints++;
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.ORANGE).withTrail().with(FireworkEffect.Type.STAR).build());
            fireworkMeta.setPower(2);
            firework.setFireworkMeta(fireworkMeta);
            Form.at(player, "Your pickaxe leveled up! Right click with the pickaxe to add new stats to the pickaxe.");
        }
        player.getInventory().setItem(0, getPickaxe());
    }

    public Inventory getStatsMenu() {
        int rows = (int) Math.ceil(((perkLevels.size() + 3.0) / 9.0));
        Inventory inventory = Bukkit.getServer().createInventory(null, 9 * rows, "Pickaxe Perks");
        for (PickaxePerk perk : PickaxePerk.getPerks()) {
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
        inventory.addItem(getWarpIcon());
        inventory.addItem(getFFAIcon());
        return inventory;
    }

    public ItemStack getPointsIcon() {
        ItemStack itemStack = new ItemStack(Material.DOUBLE_PLANT);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lPerk Points: " + pickPoints));
        itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&2Click to get a Perk Point Item")));
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

    public ItemStack getFFAIcon() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lFF&e&lA"));
        itemMeta.setLore(Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&6Click to teleport to the FFA.")));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack getAddPointItem() {
        ItemStack itemStack = new ItemStack(Material.DOUBLE_PLANT);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lRight Click: &2&lAdd Perk Point"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public int getPerkLevel(PickaxePerk perk) {
        if (perk == null)
            return 0;

        return perkLevels.get(perk);
    }

    public int getPickPoints() {
        return pickPoints;
    }

    public void setPickPoints(int pickPoints) {
        this.pickPoints = pickPoints;
    }

    public void addPerkLevel(Player player, PickaxePerk perk) {
        int level = perkLevels.get(perk) + 1;
        perkLevels.remove(perk);
        perkLevels.put(perk, level);
        perk.onPurchase(player, getPerkLevel(perk));
        pickPoints -= perk.getCost();
        Form.at(player, Prefix.SUCCESS, "You bought one level of " + perk.getName());
        player.getInventory().setItem(0, getPickaxe());
    }

    public void addPerkToggle(Player player, PickaxePerk perk) {
        perkLevels.remove(perk);
        perkLevels.put(perk, 1);
        perkToggle.remove(perk);
        perkToggle.put(perk, true);
        perk.onToggleOn(player);
        perk.onPurchase(player, 1);
        pickPoints -= perk.getCost();
        Form.at(player, Prefix.SUCCESS, "You bought " + perk.getName() + ". You can toggle this on and off in the perk menu.");
        player.getInventory().setItem(0, getPickaxe());
    }

    public void togglePerk(Player player, PickaxePerk perk) {
        boolean toggle = !perkToggle.get(perk);
        perkToggle.remove(perk);
        perkToggle.put(perk, toggle);
        if(toggle) {
            perk.onToggleOn(player);
            player.getInventory().setItem(0, getPickaxe());
            Form.at(player, Prefix.SUCCESS, "You toggled " + perk.getName() + " on!");
        } else {
            perk.onToggleOff(player);
            player.getInventory().setItem(0, getPickaxe());
            Form.at(player, Prefix.SUCCESS, "You toggled " + perk.getName() + " off!");
        }
    }

    public boolean getToggle(PickaxePerk perk) {
        if(perk == null)
            return false;
        return perkToggle.get(perk);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int counter = 0;
        sb.append("Level|").append(level).append(".");
        sb.append("BlocksMined|").append(blocksMined).append(".");
        sb.append("TotalBlocksMined|").append(totalBlocksMined).append(".");
        sb.append("Points|").append(pickPoints).append(".");
        for (PickaxePerk perk : perkLevels.keySet()) {
            counter++;
            if (perk.isToggleable()) {
                sb.append(perk.getNoColorName()).append("-Toggle|").append(perkToggle.get(perk)).append(".");
            }
            if (perkLevels.size() == counter) {
                sb.append(perk.getNoColorName()).append("|").append(perkLevels.get(perk));
                continue;
            }
            sb.append(perk.getNoColorName()).append("|").append(perkLevels.get(perk)).append(".");
        }
        return sb.toString();
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
        if(!inUse) {
            player.getInventory().setItem(0, new ItemStack(Material.AIR));
        } else {
            player.getInventory().setItem(0, getPickaxe());
        }
    }

    public boolean isInUse() {
        return inUse;
    }
}
