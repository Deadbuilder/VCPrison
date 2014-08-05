package net.vaultcraft.vcprison.pickaxe;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by tacticalsk8er on 8/3/2014.
 */
public class EfficiencyPerk extends PickaxePerk {

    public EfficiencyPerk(Material icon, String name, int cost, int initLevel, int maxLevel, String... lore) {
        super(icon, name, cost, initLevel, maxLevel, lore);
    }

    @Override
    public ItemMeta changeMeta(Player player, ItemMeta itemMeta, int level) {
        itemMeta.addEnchant(Enchantment.DIG_SPEED, level, true);
        return itemMeta;
    }
}
