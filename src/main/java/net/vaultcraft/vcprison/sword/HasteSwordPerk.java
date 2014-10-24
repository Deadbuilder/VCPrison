package net.vaultcraft.vcprison.sword;

import net.vaultcraft.vcprison.user.PrisonUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by tacticalsk8er on 10/23/2014.
 */
public class HasteSwordPerk extends SwordPerk {

    public HasteSwordPerk(Material icon, String name, int cost, int initLevel, int maxLevel, String... lore) {
        super(icon, name, cost, initLevel, maxLevel, lore);
    }

    @Override
    public void onHoverOn(Player player, int level) {
        if(level == 0)
            return;
        if(!PrisonUser.fromPlayer(player).getSword().getToggle(this))
            return;
        if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level - 1, false));
    }

    @Override
    public void onHoverOff(Player player, int level) {
        if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public void onToggleOff(Player player) {
        if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }

    @Override
    public void onStart(Player player, int level) {
        if(level == 0)
            return;
        if(!PrisonUser.fromPlayer(player).getSword().getToggle(this))
            return;
        if(player.getInventory().getHeldItemSlot() != 0)
            return;
        if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
        player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, level - 1, false));
    }

    @Override
    public void onEnd(Player player) {
        if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
            player.removePotionEffect(PotionEffectType.FAST_DIGGING);
    }
}
