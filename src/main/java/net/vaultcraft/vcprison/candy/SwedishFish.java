package net.vaultcraft.vcprison.candy;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class SwedishFish implements Candy {

    @Override
    public CandyRecipe getRecipe() {
        CandyShapedRecipe rc = new CandyShapedRecipe(this.getCandyItem());
        rc.shape('X', 'Y', 'X',
                 'Z', 'A', 'Z',
                 'X', 'Y', 'X');
        rc.setItem('X', CandyItems.WRAPPER);
        rc.setItem('Y', CandyItems.NETHERWART);
        rc.setItem('Z', CandyItems.REDDYE);
        rc.setItem('A', CandyItems.SUGARCUBE);
        return rc;
    }

    @Override
    public ItemStack getCandyItem() {
        return CandyItems.SWEDISHFISH;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public int getHarmfulAfter() {
        return 4;
    }

    @Override
    public ItemStack onCandyConsume(Player player, boolean harmful) {
        if(harmful) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(PotionEffectType.SLOW.createEffect(20 * 10, 0));
            player.addPotionEffect(PotionEffectType.WEAKNESS.createEffect(20 * 10, 0));
        } else {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            player.removePotionEffect(PotionEffectType.SLOW);
            player.removePotionEffect(PotionEffectType.WEAKNESS);
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 10, 0));
            player.addPotionEffect(PotionEffectType.DAMAGE_RESISTANCE.createEffect(20 * 5, 0));
        }

        return CandyItems.USEDWRAPPER;
    }
}
