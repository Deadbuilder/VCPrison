package net.vaultcraft.vcprison.candy;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public class Gum implements Candy {

    public ShapedRecipe getRecipe() {
        ShapedRecipe rc = new ShapedRecipe(getCandyItem());
        rc.shape("XYX", "QZQ", "XYX");
        rc.setIngredient('X', Material.QUARTZ);
        rc.setIngredient('Y', Material.INK_SACK.getNewData((byte)9));
        rc.setIngredient('Q', Material.INK_SACK.getNewData((byte)8));
        rc.setIngredient('Z', Material.SNOW_BLOCK);
        return rc;
    }

    protected static ItemStack chewed = CandyItems.CHEWEDGUM;

    public ItemStack getCandyItem() {
        return CandyItems.GUM;
    }

    @Override
    public int getCooldown() {
        return 1;
    }

    @Override
    public int getHarmfulAfter() {
        return 20;
    }

    public ItemStack onCandyConsume(Player player, boolean harmful) {
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.SPEED);
        player.addPotionEffect(PotionEffectType.SPEED.createEffect(20 * 5, 1));
        return chewed;
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        ItemStack stack = event.getItem().getItemStack().clone();
        stack.setAmount(1);

        if (chewed.equals(stack)) {
            event.setCancelled(true);
            player.removePotionEffect(PotionEffectType.SLOW);
            player.addPotionEffect(PotionEffectType.SLOW.createEffect(20 * 10, 1));
            player.playSound(player.getLocation(), Sound.DIG_GRAVEL, 1, 2);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&d&lYou stepped in sticky gum!"));
            event.getItem().remove();
        }
    }
}
