package net.vaultcraft.vcprison.sword;

import net.vaultcraft.vcutils.chat.Form;
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
 * Created by tacticalsk8er on 10/5/2014.
 */
public class Sword {

    private Player player;
    private int level = 1;
    private int exp = 0;
    private int swordPoints = 0;
    private boolean inUse = false;

    private HashMap<SwordPerk, Integer> perkLevels = new HashMap<>();
    private HashMap<SwordPerk, Boolean> perkToggle = new HashMap<>();

    public Sword(Player player) {
        this.player = player;
        this.player = player;
        for (SwordPerk perk : SwordPerk.getPerks()) {
            perkLevels.put(perk, perk.getInitLevel());
            if (perk.isTogglable()) {
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

    public ItemStack getSword() {
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMeta = pick.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lV&7&lC&e: &7Prison Sword &e&n[Level " + level + "]&r &5&oExp: " + Form.at(exp) + " / " + Form.at(toNextLevel(level))));
        List<String> lore = new ArrayList<>();
        for (SwordPerk perk : perkLevels.keySet()) {
            if (perk.isTogglable())
                if (!perkToggle.get(perk))
                    continue;
            if (perkLevels.get(perk) == 0)
                continue;
            lore = perk.changeLore(player, lore, perkLevels.get(perk));
            itemMeta = perk.changeMeta(player, itemMeta, perkLevels.get(perk));
        }
        itemMeta.addEnchant(Enchantment.DURABILITY, 50, true);
        lore.add("Exp: " + Form.at(exp) + " / " + Form.at(toNextLevel(level)));
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

    public void hit() {
        exp++;
        if (exp >= toNextLevel(level)) {
            exp = 0;
            level++;
            swordPoints++;
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
            Firework firework = player.getWorld().spawn(player.getLocation(), Firework.class);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.ORANGE).withTrail().with(FireworkEffect.Type.STAR).build());
            fireworkMeta.setPower(2);
            firework.setFireworkMeta(fireworkMeta);
            Form.at(player, "Your sword leveled up! Shift and double right click to add new stats to the your sword.");
        }
        player.getInventory().setItem(0, getSword());
    }

    public Inventory getStatsMenu() {
        int rows = (int) Math.ceil(((perkLevels.size() + 2.0) / 9.0));
        Inventory inventory = Bukkit.getServer().createInventory(null, 9 * rows, "Sword Perks");
        for (SwordPerk perk : SwordPerk.getPerks()) {
            if (perk.isTogglable()) {
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
        return inventory;
    }

    public ItemStack getPointsIcon() {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lPerk Points: " + swordPoints));
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

    public static ItemStack getAddPointItem() {
        ItemStack itemStack = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lRight Click: &2&lAdd Perk Point"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }
}
