package net.vaultcraft.vcprison.sword;

import net.vaultcraft.vcprison.user.PrisonUser;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by tacticalsk8er on 10/23/2014.
 */
public class NightVisionSwordPerk extends SwordPerk {

    public NightVisionSwordPerk(Material icon, Material toggleOn, Material toggleOff, String name, int cost, boolean initState, String... lore) {
        super(icon, toggleOn, toggleOff, name, cost, initState, lore);
    }

    @Override
    public void onHoverOn(Player player, int level) {
        if(level == 0)
            return;
        if(!PrisonUser.fromPlayer(player).getSword().getToggle(this))
            return;
        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, level - 1, false));
    }

    @Override
    public void onHoverOff(Player player, int level) {
        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public void onToggleOff(Player player) {
        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }

    @Override
    public void onStart(Player player, int level) {
        if(level == 0)
            return;
        if(!PrisonUser.fromPlayer(player).getSword().getToggle(this))
            return;
        if(player.getInventory().getHeldItemSlot() != 0)
            return;
        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, level - 1, false));
    }

    @Override
    public void onEnd(Player player) {
        if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
            player.removePotionEffect(PotionEffectType.NIGHT_VISION);
    }
}
