package net.vaultcraft.vcprison.candy;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class SugarCube implements Candy {
    @Override
    public Recipe getRecipe() {
        ItemStack itemStack = this.getCandyItem().clone();
        itemStack.setAmount(8);
        ShapedRecipe rc = new ShapedRecipe(itemStack);
        rc.shape("XXX", "XYX", "XXX");
        rc.setIngredient('X', Material.SUGAR);
        rc.setIngredient('Y', Material.MILK_BUCKET);
        return rc;
//        CandyShapedRecipe rc = new CandyShapedRecipe(this.getCandyItem());
//        rc.shape('X', 'X', 'X',
//                 'X', 'Y', 'X',
//                 'X', 'X', 'X');
//        rc.setItem('X', new ItemStack(Material.SUGAR));
//        rc.setItem('Y', new ItemStack(Material.MILK_BUCKET));
//        return rc;
    }

    public ItemStack getCandyItem() {
        return CandyItems.SUGARCUBE;
    }

    @Override
    public int getCooldown() {
        return 10;
    }

    @Override
    public int getHarmfulAfter() {
        return 4;
    }

    public ItemStack onCandyConsume(Player player, boolean harmful) {

        if (harmful) {
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.removePotionEffect(PotionEffectType.CONFUSION);

            player.addPotionEffect(PotionEffectType.BLINDNESS.createEffect(20 * 25, 0));
            player.addPotionEffect(PotionEffectType.CONFUSION.createEffect(20 * 25, 0));
        } else {
            player.removePotionEffect(PotionEffectType.JUMP);
            player.removePotionEffect(PotionEffectType.SPEED);

            player.addPotionEffect(PotionEffectType.JUMP.createEffect(20 * 5, 0));
            player.addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 5, 0));
        }
        return null;
    }
}
