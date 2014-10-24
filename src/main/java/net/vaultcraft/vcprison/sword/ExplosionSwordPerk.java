package net.vaultcraft.vcprison.sword;

import net.vaultcraft.vcutils.uncommon.Particles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * @author Connor Hollasch
 * @since 10/24/14
 */
public class ExplosionSwordPerk extends SwordPerk {

    public ExplosionSwordPerk(Material icon, String name, int cost, int initLevel, int maxLevel, String... lore) {
        super(icon, name, cost, initLevel, maxLevel, lore);
    }

    @Override
    public void onHit(Player player, Player otherPlayer, int level) {
        if ((Math.random()*100) < (level*10)) {
            Location at = otherPlayer.getLocation();
            Particles.HUGE_EXPLOSION.sendToLocation(at, 1f, 1f, 1f, 1, 3);
            otherPlayer.setVelocity(new Vector((Math.random()*2) - 1, (Math.random()*3), (Math.random()*2) - 1));
            at.getWorld().playSound(at, Sound.EXPLODE, 1, 1);
            otherPlayer.damage(3.0);
        }
    }
}
