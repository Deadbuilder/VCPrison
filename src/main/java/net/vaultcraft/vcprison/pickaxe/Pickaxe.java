package net.vaultcraft.vcprison.pickaxe;

import net.vaultcraft.vcutils.chat.Form;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 7/31/2014.
 */
public class Pickaxe {

    private int DIG_SPEED_LEVEL;
    private int FORTUNE_LEVEL;
    private int HASTE_LEVEL;

    private int PICK_LEVEL;
    private int BLOCKS_MINE;


    public Pickaxe() {
        DIG_SPEED_LEVEL = 10;
        FORTUNE_LEVEL = 10;
        HASTE_LEVEL = 0;
    }

    public ItemStack getPickaxe() {
        ItemStack pick = new ItemStack(Material.DIAMOND_PICKAXE);
        pick.addEnchantment(Enchantment.DIG_SPEED, DIG_SPEED_LEVEL);
        pick.addEnchantment(Enchantment.LOOT_BONUS_BLOCKS, FORTUNE_LEVEL);
        ItemMeta itemMeta = pick.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_PURPLE + "V" + ChatColor.DARK_GRAY + "C" + ChatColor.WHITE + "Pick [Level " + Form.at(PICK_LEVEL) + "]");
        List<String> lore = new ArrayList<>();
        lore.add("Exp: " + Form.at(BLOCKS_MINE) + " / " + Form.at(toNextLevel(PICK_LEVEL)));
        itemMeta.setLore(lore);
        pick.setItemMeta(itemMeta);
        return pick;
    }

    public void setDIG_SPEED_LEVEL(int DIG_SPEED_LEVEL) {
        this.DIG_SPEED_LEVEL = DIG_SPEED_LEVEL;
    }

    public void setFORTUNE_LEVEL(int FORTUNE_LEVEL) {
        this.FORTUNE_LEVEL = FORTUNE_LEVEL;
    }

    public void setHASTE_LEVEL(int HASTE_LEVEL) {
        this.HASTE_LEVEL = HASTE_LEVEL;
    }

    public int toNextLevel(int level) {
        return 2000 + (500 * level);
    }
}
