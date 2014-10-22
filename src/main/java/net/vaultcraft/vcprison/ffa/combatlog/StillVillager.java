package net.vaultcraft.vcprison.ffa.combatlog;

import net.minecraft.server.v1_7_R4.*;
import org.bukkit.craftbukkit.v1_7_R4.util.UnsafeList;

import java.lang.reflect.Field;

/**
 * @author Connor Hollasch
 * @since 10/21/14
 */
public class StillVillager extends EntityVillager {

    public StillVillager(World world) {
        super(world);

        try {
            Field gsa = PathfinderGoalSelector.class.getDeclaredField("b");
            gsa.setAccessible(true);

            gsa.set(goalSelector, new UnsafeList());
            gsa.set(targetSelector, new UnsafeList());

        }
        catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void h() {
        motX = 0D;
        motY = 0D;
        motZ = 0D;

        super.h();
    }

    @Override
    public void collide(Entity arg0) {}

    public EntityAgeable createChild(EntityAgeable entityAgeable) {
        return null;
    }
}
