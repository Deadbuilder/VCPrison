package net.vaultcraft.vcprison.sword;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by tacticalsk8er on 10/23/2014.
 */
public class FireAspectSwordPerk extends SwordPerk {

    public FireAspectSwordPerk(Material icon, String name, int cost, int initLevel, int maxLevel, String... lore) {
        super(icon, name, cost, initLevel, maxLevel, lore);
    }

    @Override
    public ItemMeta changeMeta(Player player, ItemMeta itemMeta, int level) {
        if(level == 0)
            return itemMeta;
        itemMeta.addEnchant(Enchantment.FIRE_ASPECT, level, true);
        return itemMeta;
    }
}
