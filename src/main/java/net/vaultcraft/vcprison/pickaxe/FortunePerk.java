package net.vaultcraft.vcprison.pickaxe;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by tacticalsk8er on 8/3/2014.
 */
public class FortunePerk extends PickaxePerk {

    public FortunePerk(Material icon, String name, int cost, int initLevel, int maxLevel, String... lore) {
        super(icon, name, cost, initLevel, maxLevel, lore);
    }

    @Override
    public ItemMeta changeMeta(Player player, ItemMeta itemMeta, int level) {
        itemMeta.addEnchant(Enchantment.LOOT_BONUS_BLOCKS, level, true);
        return itemMeta;
    }

    @Override
    public ItemStack onBreak(Player player, BlockBreakEvent event, ItemStack itemStack, int level) {
        if (isFortuneBlock(itemStack.getType()))
            if (itemStack.getType() == Material.REDSTONE || itemStack.getType() == Material.INK_SACK)
                itemStack.setAmount(fortune(level) * 2);
            else
                itemStack.setAmount(fortune(level));
        return itemStack;
    }

    public boolean isFortuneBlock(Material type) {
        switch (type) {
            case COAL:
                return true;
            case DIAMOND:
                return true;
            case REDSTONE:
                return true;
            case INK_SACK:
                return true;
            case IRON_INGOT:
                return true;
            case GOLD_INGOT:
                return true;
            case EMERALD:
                return true;
            default:
                return false;
        }
    }

    public int fortune(int level) {
        int fortune = (int) ((Math.random() * level) / 1.2);
        if (fortune == 0)
            return 1;
        return fortune;
    }
}
