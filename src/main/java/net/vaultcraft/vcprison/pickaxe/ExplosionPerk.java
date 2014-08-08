package net.vaultcraft.vcprison.pickaxe;

import com.google.common.collect.Lists;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.mine.Mine;
import net.vaultcraft.vcprison.mine.MineLoader;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.protection.ProtectionManager;
import net.vaultcraft.vcutils.protection.flag.FlagType;
import net.vaultcraft.vcutils.uncommon.Explosion;
import net.vaultcraft.vcutils.uncommon.Particles;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by Connor on 8/8/14. Designed for the VCPrison project.
 */

public class ExplosionPerk extends PickaxePerk {

    public ExplosionPerk(Material icon, String name, int cost, int initLevel, int maxLevel, String... lore) {
        super(icon, name, cost, initLevel, maxLevel, lore);
    }

    @Override
    public ItemStack onBreak(final Player player, final BlockBreakEvent event, ItemStack itemStack, final int level) {
        if (Math.random()*300 < (level > 18 ? (level/1.5) : level)) {
            final Mine hit = MineLoader.fromLocation(event.getBlock().getLocation());
            if (hit == null)
                return itemStack;

            Runnable async = new Runnable() {
                public void run() {
                    List<Block> fromExplosion = Explosion.formExplosion(event.getBlock().getLocation().clone().add(Math.random(), Math.random(), Math.random()), (level > 15 ? 3.5 : 2.5));
                    final List<Block> scanned = Lists.newArrayList();
                    for (Block bx : fromExplosion) {
                        if (!(ProtectionManager.getInstance().getState(FlagType.BLOCK_BREAK, bx.getLocation()).isCancelled())) {
                            scanned.add(bx);
                            hit.tickBlocks();
                        }
                    }

                    Runnable sync = new Runnable() {
                        public void run() {
                            Location block = event.getBlock().getLocation();
                            block.getWorld().playSound(block, Sound.EXPLODE, 3, 0);
                            Particles.HUGE_EXPLOSION.sendToLocation(block, 0F, 0F, 0F, 1, 1);
                            for (Block b : scanned) {
                                player.getInventory().addItem(new ItemStack(b.getType()));
                                b.getWorld().playEffect(b.getLocation(), Effect.STEP_SOUND, b.getTypeId(), b.getData());
                                b.setType(Material.AIR);
                            }
                        }
                    };
                    Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), sync);
                }
            };
            Bukkit.getScheduler().scheduleAsyncDelayedTask(VCPrison.getInstance(), async);
        }

        return itemStack;
    }
}
