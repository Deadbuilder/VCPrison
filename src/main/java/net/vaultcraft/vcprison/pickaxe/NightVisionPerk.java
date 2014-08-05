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
public class NightVisionPerk extends PickaxePerk {

    public NightVisionPerk(Material icon, Material toggleOn, Material toggleOff, String name, int cost, boolean initState, String... lore) {
        super(icon, toggleOn, toggleOff, name, cost, initState, lore);
    }

    @Override
    public List<String> changeLore(Player player, List<String> lore, int level) {
        lore.add(ChatColor.GRAY + "Night Vision");
        return lore;
    }

    @Override
    public void onHoverOn(Player player, int level) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false));
    }

    @Override
    public void onHoverOff(Player player, int level) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public void onToggleOff(Player player) {
        player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public void onToggleOn(Player player) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false));
    }

    @Override
    public void onPurchase(Player player, int level) {
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false));
    }
}
