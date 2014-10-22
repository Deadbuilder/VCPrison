package net.vaultcraft.vcprison.ffa.combatlog;

import net.minecraft.server.v1_7_R4.MathHelper;
import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 10/21/14
 */
public class CombatLog {

    public static HashMap<Player, Long> inPvP = new HashMap<>();
    public static HashMap<Player, Villager> logged = new HashMap<>();

    public static void wasTagged(Player player) {
        if (inPvP.containsKey(player)) {
            long x = inPvP.get(player);
            x+=(1000 * 10);
            inPvP.put(player, x);
        }
        else
            inPvP.put(player, System.currentTimeMillis() + (1000 * 10));
    }

    public static boolean isSafeLogout(Player player) {
        if (inPvP.containsKey(player)) {
            long at = inPvP.get(player);
            if (at <= System.currentTimeMillis())
                inPvP.remove(player);
        }

        return !inPvP.containsKey(player);
    }

    public static void handleLeave(Player player) {
        if (isSafeLogout(player))
            return;

        ItemStack[] armor = player.getEquipment().getArmorContents();
        ItemStack[] inventory = player.getInventory().getContents();

        ItemStack[] drops = new ItemStack[armor.length + inventory.length];
        System.arraycopy(armor, 0, drops, 0, armor.length);
        System.arraycopy(inventory, 0, drops, armor.length, inventory.length);

        StillVillager still = new StillVillager(((CraftWorld) player.getWorld()).getHandle());
        Location loc = player.getLocation();

        still.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());

        int i = MathHelper.floor(still.locX / 16.0D);
        int j = MathHelper.floor(still.locZ / 16.0D);
        net.minecraft.server.v1_7_R4.World world = ((CraftWorld) player.getWorld()).getHandle();

        world.getChunkAt(i, j).a(still);
        world.entityList.add(still);

        try {
            Method method = world.getClass().getDeclaredMethod("a", net.minecraft.server.v1_7_R4.Entity.class);
            method.setAccessible(true);

            method.invoke(world, still);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        final Villager wrap = (Villager) still.getBukkitEntity();

        wrap.addPotionEffect(PotionEffectType.SLOW.createEffect(Integer.MAX_VALUE, 100));
        wrap.addPotionEffect(PotionEffectType.JUMP.createEffect(Integer.MAX_VALUE, 100));

        PlayerInventory pi = player.getInventory();
        if (pi.getHelmet() != null) {
            wrap.getEquipment().setHelmet(pi.getHelmet());
            wrap.getEquipment().setHelmetDropChance(0F);
        }
        if (pi.getChestplate() != null) {
            wrap.getEquipment().setChestplate(pi.getChestplate());
            wrap.getEquipment().setChestplateDropChance(0F);
        }
        if (pi.getLeggings() != null) {
            wrap.getEquipment().setLeggings(pi.getLeggings());
            wrap.getEquipment().setLeggingsDropChance(0F);
        }
        if (pi.getBoots() != null) {
            wrap.getEquipment().setBoots(pi.getBoots());
            wrap.getEquipment().setBootsDropChance(0F);
        }

        wrap.setMetadata("dummy", new FixedMetadataValue(VCPrison.getInstance(), drops));
        wrap.setAgeLock(true);
        wrap.setHealth(((Damageable) player).getHealth());

        logged.put(player, wrap);

        Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), () -> {
            if (!wrap.isDead() && wrap.isValid()) {
                wrap.remove();
                logged.remove(player);
            }
        }, 15 * 20L);
    }
}
