package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;

/**
 * Created by tacticalsk8er on 11/2/2014.
 */
public class SourPatch implements Candy {

    HashMap<String, Long> thornsMap = new HashMap<>();

    @Override
    public ShapedRecipe getRecipe() {
        ShapedRecipe shapedRecipe = new ShapedRecipe(CandyItems.SOURPATCH);
        shapedRecipe.shape("xyx", "yzy", "xyx");
        shapedRecipe.setIngredient('x', Material.QUARTZ);
        shapedRecipe.setIngredient('y', Material.INK_SACK.getNewData((byte)2));
        shapedRecipe.setIngredient('z', Material.SNOW_BLOCK);
        return shapedRecipe;
    }

    @Override
    public ItemStack getCandyItem() {
        return CandyItems.SOURPATCH;
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
            if(thornsMap.containsKey(player.getName()))
                thornsMap.remove(player.getName());
            player.removePotionEffect(PotionEffectType.POISON);
            player.addPotionEffect(PotionEffectType.POISON.createEffect(20 * 10, 0));
        } else {
            if(thornsMap.containsKey(player.getName()))
                thornsMap.remove(player.getName());
            thornsMap.put(player.getName(), System.currentTimeMillis() + 10000);
        }
        Form.at(player, Prefix.SUCCESS, "You now have Thorns I effect for 10 seconds.");
        return CandyItems.USEDWRAPPER;
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        LivingEntity damager = null;
        if(event.getDamager() instanceof LivingEntity)
            damager = (LivingEntity) event.getDamager();
        if(event.getDamager() instanceof Arrow)
            if(((Arrow) event.getDamager()).getShooter() instanceof LivingEntity)
                damager = (LivingEntity) ((Arrow) event.getDamager()).getShooter();
        if(damager == null)
            return;
        String name = ((Player) event.getEntity()).getName();
        if (!(thornsMap.containsKey(name)))
            return;

        if(thornsMap.get(name) <= System.currentTimeMillis()) {
            thornsMap.remove(name);
            return;
        }

        if(Math.random() <= .15) {
            int damage = (int) (Math.random() * 4);
            damager.damage(damage, event.getEntity());
        }
    }
}
