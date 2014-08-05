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
public class SilkTouchPerk extends PickaxePerk {

    public SilkTouchPerk(Material icon, Material toggleOn, Material toggleOff, String name, int cost, boolean initState, String... lore) {
        super(icon, toggleOn, toggleOff, name, cost, initState, lore);
    }

    @Override
    public ItemMeta changeMeta(Player player, ItemMeta itemMeta, int level) {
        itemMeta.addEnchant(Enchantment.SILK_TOUCH, 1, true);
        return itemMeta;
    }

    @Override
    public ItemStack onBreak(Player player, BlockBreakEvent event, ItemStack itemStack, int level) {
        itemStack.setType(event.getBlock().getType());
        return itemStack;
    }
}
