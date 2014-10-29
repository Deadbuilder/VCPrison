package net.vaultcraft.vcprison.pickaxe;

import net.vaultcraft.vcprison.mine.MineLoader;
import org.bukkit.Material;
import org.bukkit.block.Block;
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
    public ItemStack onBreak(Player player, BlockBreakEvent event, Block block, ItemStack itemStack, int level) {
        if (MineLoader.fromLocation(block.getLocation()) != null)
            if(itemStack.getType() == Material.CHEST)
                return itemStack;
            if (itemStack.getType() == Material.REDSTONE || itemStack.getType() == Material.INK_SACK)
                itemStack.setAmount(fortune(level) * 2);
            else
                itemStack.setAmount(fortune(level));
        return itemStack;
    }

    public int fortune(int level) {
        int fortune = (int) ((Math.random() * level) / 1.2);
        if (fortune == 0)
            return 1;
        return fortune;
    }
}
