package net.vaultcraft.vcprison.pickaxe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

/**
 * Created by tacticalsk8er on 8/3/2014.
 */
public class HastePerk extends PickaxePerk {

    public HastePerk(Material icon, String name, int cost, int initLevel, int maxLevel, String... lore) {
        super(icon, name, cost, initLevel, maxLevel, lore);
    }

    @Override
    public List<String> changeLore(Player player, List<String> lore, int level) {
        lore.add(ChatColor.GRAY + "Haste " + level);
        return lore;
    }

    @Override
    public void onPurchase(Player player, int level) {
        if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level - 1, false));
    }

    @Override
    public void onHoverOn(Player player, int level) {
        if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level - 1, false));
    }

    @Override
    public void onHoverOff(Player player, int level) {
        player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }
}
