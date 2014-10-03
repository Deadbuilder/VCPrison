package net.vaultcraft.vcprison.gangs;

import net.minecraft.util.com.google.gson.Gson;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.database.sql.MySQL;
import net.vaultcraft.vcutils.database.sql.Statements;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * Created by tacticalsk8er on 10/1/2014.
 */
public class GangManager implements Listener {

    private static HashMap<String, Gang> gangs = new HashMap<>();

    public GangManager() {
        VCUtils.getInstance().getSqlite().updateThread.add(Statements.TABLE_SQLITE.getSql("Gangs", "Gang"));
        VCUtils.getInstance().getSqlite().addQuery(Statements.QUERYALL.getSql("Plots"), new MySQL.ISqlCallback() {
            @Override
            public void onSuccess(ResultSet resultSet) {
                Gson gson = new Gson();
                try {
                    while (resultSet.next()) {
                        String json = resultSet.getString("JSON");
                        Gang gang = gson.fromJson(json, Gang.class);
                        gangs.put(gang.getGangName(), gang);
                    }
                    Logger.log(VCPrison.getInstance(), "Gangs loaded.");
                } catch (SQLException e) {
                    Logger.error(VCPrison.getInstance(), e);
                }
            }

            @Override
            public void onFailure(SQLException e) {
                Logger.error(VCPrison.getInstance(), e);
            }
        });
    }

    public static Gang getGang(String name) {
        return gangs.get(name);
    }

    public static HashMap<String, Gang> getGangs() {
        return gangs;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        Player player = (Player) event.getEntity();
        Player damager = null;
        if(event.getDamager() instanceof Player)
            damager = (Player) event.getDamager();
        if(event.getDamager() instanceof Arrow) {
            if(((Arrow) event.getDamager()).getShooter() instanceof Player)
                damager = (Player) ((Arrow) event.getDamager()).getShooter();
        }

        if(damager == null)
            return;

        Gang playerGang = null;
        Gang damagerGang = null;

        for (Gang gang : gangs.values()) {
            if (gang.getOwnerUUID().equals(player.getUniqueId().toString())) {
                playerGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(player.getUniqueId().toString())) {
                    playerGang = gang;
                    break;
                }
            }
        }

        for (Gang gang : gangs.values()) {
            if (gang.getOwnerUUID().equals(damager.getUniqueId().toString())) {
                damagerGang = gang;
                break;
            }
            for (String memberUUID : gang.getMemberUUIDs()) {
                if (memberUUID.equals(damager.getUniqueId().toString())) {
                    damagerGang = gang;
                    break;
                }
            }
        }

        if(playerGang == null)
            return;
        if(damagerGang == null)
            return;
        if(playerGang.isFriendlyFire() || damagerGang.isFriendlyFire())
            return;

        if(playerGang.getGangName().equals(damagerGang.getGangName())) {
            event.setCancelled(true);
            return;
        }

        for(String gangName : playerGang.getAlliedGangs()) {
            if(gangName.equalsIgnoreCase(damagerGang.getGangName())) {
                event.setCancelled(true);
                return;
            }
        }
    }
}
