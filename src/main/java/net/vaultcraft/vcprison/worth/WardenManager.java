package net.vaultcraft.vcprison.worth;

import com.google.common.collect.Lists;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.vaultcraft.vcprison.VCPrison;
import org.bukkit.Bukkit;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Connor on 8/12/14. Designed for the VCPrison project.
 */

public class WardenManager {

    private static List<Warden> wardens = Lists.newArrayList();

    public WardenManager() {
        Runnable initDelay = new Runnable() {
            public void run() {
                NPCRegistry registry = CitizensAPI.getNPCRegistry();
                Iterator<NPC> npcs = registry.iterator();
                while (npcs.hasNext()) {
                    NPC next = npcs.next();

                    wardens.add(new Warden(next));
                }
            }
        }; //thanks citizens >:|
        Bukkit.getScheduler().scheduleSyncDelayedTask(VCPrison.getInstance(), initDelay, 20);
    }

    public static Warden fromNPC(NPC npc) {
        for (Warden w : wardens) {
            if (w.getNPCContainer().equals(npc))
                return w;
        }
        return null;
    }

    public static List<Warden> allWardens() {
        return wardens;
    }
}
