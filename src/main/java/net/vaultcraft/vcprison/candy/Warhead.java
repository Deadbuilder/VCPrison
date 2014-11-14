package net.vaultcraft.vcprison.candy;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class Warhead implements Candy {

    @Override
    public CandyRecipe getRecipe() {
        CandyShapedRecipe rc = new CandyShapedRecipe(this.getCandyItem());
        rc.shape('X', 'Y', 'X',
                 'Y', 'Z', 'Y',
                 'X', 'Y', 'X');
        rc.setItem('X', CandyItems.WRAPPER);
        rc.setItem('Y', CandyItems.SOURPATCH);
        rc.setItem('Z', CandyItems.SUGARCUBE);
        return rc;
    }

    @Override
    public ItemStack getCandyItem() {
        return CandyItems.WARHEAD;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public int getHarmfulAfter() {
        return 3;
    }

    @Override
    public ItemStack onCandyConsume(Player player, boolean harmful) {
        if(harmful) {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20 * 15, 0));
        } else {
            player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(PotionEffectType.INCREASE_DAMAGE.createEffect(20 * 10, 0));
        }
        return CandyItems.USEDWRAPPER;
    }
}
