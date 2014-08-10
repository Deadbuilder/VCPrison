package net.vaultcraft.vcprison.worth;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.vaultcraft.vcprison.utils.Rank;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

/**
 * Created by Connor on 8/8/14. Designed for the VCPrison project.
 */

public class Warden {

    private Rank rank;
    private NPC capture;

    public Warden(Rank rank, Location location) {
        this.rank = rank;
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, ChatColor.translateAlternateColorCodes('&', "&5&lWarden"));
        npc.spawn(location);
        this.capture = npc;
    }
}
