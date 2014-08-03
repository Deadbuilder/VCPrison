package net.vaultcraft.vcprison.pickaxe;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.item.ItemUtils;
import net.vaultcraft.vcutils.logging.Logger;
import net.vaultcraft.vcutils.uncommon.FireworkEffectPlayer;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 7/31/2014.
 */
public class Pickaxe {

    private Player player;
    private int DIG_SPEED_LEVEL;
    private int FORTUNE_LEVEL;
    private int HASTE_LEVEL;
    private boolean HAS_AUTO_SMELT;
    private boolean AUTO_SMELT;

    private int PICK_LEVEL;
    private int BLOCKS_MINED;
    private int PICK_POINTS;


    public Pickaxe(Player player) {
        this.player = player;
        DIG_SPEED_LEVEL = 5;
        FORTUNE_LEVEL = 5;
        HASTE_LEVEL = 0;
        PICK_LEVEL = 1;
        BLOCKS_MINED = 0;
        PICK_POINTS = 0;
    }

    public Pickaxe(Player player, String s) {
        this.player = player;
        String[] parts = s.split("\\.");
        for(String part : parts) {
            String[] value = part.split("\\|");
            switch (value[0]) {
                case "DIG_SPEED_LEVEL":
                    DIG_SPEED_LEVEL = Integer.parseInt(value[1]);
                    break;
                case "FORTUNE_LEVEL":
                    FORTUNE_LEVEL = Integer.parseInt(value[1]);
                    break;
                case "HASTE_LEVEL":
                    HASTE_LEVEL = Integer.parseInt(value[1]);
                    break;
                case "PICK_LEVEL":
                    PICK_LEVEL = Integer.parseInt(value[1]);
                    break;
                case "BLOCKS_MINED":
                    BLOCKS_MINED = Integer.parseInt(value[1]);
                    break;
                case "PICK_POINTS":
                    PICK_POINTS = Integer.parseInt(value[1]);
                    break;
                case "HAS_AUTO_SMELT":
                    HAS_AUTO_SMELT = Boolean.parseBoolean(value[1]);
                    break;
                case "AUTO_SMELT":
                    AUTO_SMELT = Boolean.parseBoolean(value[1]);
                    break;
            }
        }
    }

    public ItemStack getPickaxe() {
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta itemMeta = pick.getItemMeta();
        itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&5&lV&7&lC&e: &7Prison Pickaxe &e&n[Level " + PICK_LEVEL + "]&r &5&oExp: " + Form.at(BLOCKS_MINED) + " / " + Form.at(toNextLevel(PICK_LEVEL))));
        List<String> lore = new ArrayList<>();
        if(HASTE_LEVEL > 0)
            lore.add(ChatColor.GRAY + "Haste " + HASTE_LEVEL);
        if(AUTO_SMELT)
            lore.add(ChatColor.GRAY + "Auto Smelt");
        lore.add("Exp: " + Form.at(BLOCKS_MINED) + " / " + Form.at(toNextLevel(PICK_LEVEL)));
        itemMeta.setLore(lore);
        itemMeta.addEnchant(Enchantment.DIG_SPEED, DIG_SPEED_LEVEL, true);
        itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, FORTUNE_LEVEL, true);
        itemMeta.addEnchant(Enchantment.DURABILITY, 50, true);
        pick.setItemMeta(itemMeta);
        return pick;
    }

    public void setDIG_SPEED_LEVEL(int DIG_SPEED_LEVEL) {
        this.DIG_SPEED_LEVEL = DIG_SPEED_LEVEL;
    }

    public int getDIG_SPEED_LEVEL() {
        return DIG_SPEED_LEVEL;
    }

    public void setFORTUNE_LEVEL(int FORTUNE_LEVEL) {
        this.FORTUNE_LEVEL = FORTUNE_LEVEL;
    }

    public int getFORTUNE_LEVEL() {
        return FORTUNE_LEVEL;
    }

    public void setHASTE_LEVEL(int HASTE_LEVEL) {
        this.HASTE_LEVEL = HASTE_LEVEL;
    }

    public int getHASTE_LEVEL() {
        return HASTE_LEVEL;
    }

    public void setHAS_AUTO_SMELT(boolean HAS_AUTO_SMELT) {
        this.HAS_AUTO_SMELT = HAS_AUTO_SMELT;
    }

    public void setAUTO_SMELT(boolean AUTO_SMELT) {
        this.AUTO_SMELT = AUTO_SMELT;
    }

    public boolean isAUTO_SMELT() {
        return AUTO_SMELT;
    }

    public void setPICK_POINTS(int PICK_POINTS) {
        this.PICK_POINTS = PICK_POINTS;
    }

    public int getPICK_POINTS() {
        return PICK_POINTS;
    }

    public void mineBlock() {
        BLOCKS_MINED++;
        if(BLOCKS_MINED == toNextLevel(PICK_LEVEL)) {
            BLOCKS_MINED = 0;
            PICK_LEVEL++;
            PICK_POINTS++;
            player.getWorld().playSound(player.getLocation(), Sound.LEVEL_UP, 5, 1);
            try {
                FireworkEffectPlayer.playFirework(player.getWorld(), player.getEyeLocation(), FireworkEffect.builder().withColor(Color.YELLOW).withFade(Color.ORANGE).withTrail().with(FireworkEffect.Type.STAR).build());
            } catch (Exception e) {
                Logger.error(VCPrison.getInstance(), e);
            }
            Form.at(player, "Your pickaxe leveled up! Right click with the pickaxe to add new stats to the pickaxe.");
        }
        player.getInventory().setItem(0, getPickaxe());
    }

    public int toNextLevel(int level) {
        if(level < 50)
            return (600 + (600 * (level - 1)));
        else
            return 30000;
    }

    @Override
    public String toString() {
        return "DIG_SPEED_LEVEL|" + DIG_SPEED_LEVEL + ".FORTUNE_LEVEL|" + FORTUNE_LEVEL +".HASTE_LEVEL|" + HASTE_LEVEL + ".PICK_LEVEL|" + PICK_LEVEL + ".BLOCKS_MINED|" + BLOCKS_MINED + ".PICK_POINTS|" + PICK_POINTS + ".HAS_AUTO_SMELT|" + HAS_AUTO_SMELT + ".AUTO_SMELT|" + AUTO_SMELT;
    }

    public int getFortuneItems() {
        int items = (int) ((Math.random() * FORTUNE_LEVEL) / 1.2);
        if(items == 0)
            return 1;
        else
            return items;
    }

    public Inventory getStatsMenu() {
        Inventory inventory = Bukkit.getServer().createInventory(null, 9, "Pickaxe Stat Points: " + PICK_POINTS);
        
        ItemStack digSpeedItem = new ItemStack(Material.STONE, DIG_SPEED_LEVEL);
        digSpeedItem = ItemUtils.setName(digSpeedItem, "Efficiency");
        List<String> lore = new ArrayList<>();
        lore.add("Add one level of Efficency to you pick.");
        lore.add("Cost: 1");
        digSpeedItem = ItemUtils.setLore(digSpeedItem, lore);
        inventory.setItem(0, digSpeedItem);
        
        ItemStack fortuneItem = new ItemStack(Material.DIAMOND, FORTUNE_LEVEL);
        fortuneItem = ItemUtils.setName(fortuneItem, "Fortune");
        List<String> lore1 = new ArrayList<>();
        lore1.add("Add one level of Fortune to you pick.");
        lore1.add("Cost: 1");
        fortuneItem = ItemUtils.setLore(fortuneItem, lore1);
        inventory.setItem(1, fortuneItem);

        ItemStack hasteItem = new ItemStack(Material.DIAMOND_PICKAXE, HASTE_LEVEL == 0 ? 1 : HASTE_LEVEL);
        hasteItem = ItemUtils.setName(hasteItem, "Haste");
        List<String> lore2 = new ArrayList<>();
        lore2.add("Add one level of Haste to you pick.");
        lore2.add("Max Level: 4");
        lore2.add("Cost: 5");
        hasteItem = ItemUtils.setLore(hasteItem, lore2);
        inventory.setItem(2, hasteItem);

        if(HAS_AUTO_SMELT) {
            if(AUTO_SMELT) {
                ItemStack autoSmeltItem = new ItemStack(Material.FIRE);
                autoSmeltItem = ItemUtils.setName(autoSmeltItem, "Toggle Auto Smelt Off");
                inventory.setItem(3, autoSmeltItem);
            } else {
                ItemStack autoSmeltItem = new ItemStack(Material.FURNACE);
                autoSmeltItem = ItemUtils.setName(autoSmeltItem, "Toggle Auto Smelt On");
                inventory.setItem(3, autoSmeltItem);
            }
        } else {
            ItemStack autoSmeltItem = new ItemStack(Material.FIRE);
            autoSmeltItem = ItemUtils.setName(autoSmeltItem, "Auto Smelt");
            List<String> lore3 = new ArrayList<>();
            lore3.add("Smelts items as you mine them.");
            lore3.add("Cost: 8");
            autoSmeltItem = ItemUtils.setLore(autoSmeltItem, lore3);
            inventory.setItem(3, autoSmeltItem);
        }

        return inventory;
    }
}
