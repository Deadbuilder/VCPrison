package net.vaultcraft.vcprison.sword;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.ffa.FFADamageTracker;
import net.vaultcraft.vcprison.ffa.FFAPlayer;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.UserLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

/**
 * Created by tacticalsk8er on 10/5/2014.
 */
public class SwordListener implements Listener {

    public SwordListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onUserLoad(UserLoadedEvent event) {
        PrisonUser user = PrisonUser.fromPlayer(event.getUser().getPlayer());
        if (event.getUser().getUserdata("Sword") != null) {
            user.setSword(new Sword(user.getPlayer(), event.getUser().getUserdata("Sword")));
        } else {
            user.setSword(new Sword(user.getPlayer()));
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        Sword sword = PrisonUser.fromPlayer(event.getPlayer()).getSword();
        if (sword == null)
            return;
        if (!sword.isInUse())
            return;
        if(event.getItemDrop().getItemStack().getType().name().contains("SWORD"))
            event.setCancelled(true);
        if (event.getItemDrop().getItemStack().equals(sword.getSword())) {
            event.getPlayer().openInventory(sword.getStatsMenu());
        }

    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) {
            if (event.getCurrentItem().getType().name().contains("SWORD")) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            event.setCancelled(true);
            return;
        }

        Inventory perkMenu = event.getInventory();
        if (perkMenu == null)
            return;
        if (!perkMenu.getName().equalsIgnoreCase("Sword Perks"))
            return;
        if (event.getInventory() == null)
            return;
        if (event.getInventory().getName() == null)
            return;
        if (!event.getInventory().getName().equalsIgnoreCase("Sword Perks"))
            return;
        if (event.getCurrentItem() == null)
            return;
        if (event.getCurrentItem().getItemMeta() == null)
            return;
        if (event.getCurrentItem().getItemMeta().getDisplayName() == null)
            return;
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        Sword sword = PrisonUser.fromPlayer(player).getSword();
        if (!sword.isInUse())
            return;
        SwordPerk perk = SwordPerk.getPerkFromName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().replace("Toggle Off ", "").replace("Toggle On ", "").replaceAll(" \\d+", "").replace(" Max", "")));
        if(perk == null)
            return;
        if (sword.getPerkLevel(perk) == perk.getMaxLevel()) {
            Form.at(player, Prefix.ERROR, "Sword perk is at it highest level!");
            return;
        }
        if (perk.isToggleable()) {
            if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Toggle")) {
                sword.togglePerk(player, perk);
                if (sword.getToggle(perk))
                    perkMenu.setItem(event.getSlot(), perk.getToggleOff());
                else
                    perkMenu.setItem(event.getSlot(), perk.getToggleOn());
                return;
            }
            if (sword.getSwordPoints() < perk.getCost()) {
                Form.at(player, Prefix.ERROR, "You don't have enough perk points to buy this perk!");
                return;
            }
            sword.addPerkToggle(player, perk);
            perkMenu.setItem(event.getSlot(), perk.getToggleOff());
            perkMenu.setItem(SwordPerk.getPerks().size(), sword.getPointsIcon());
            return;
        }
        if (sword.getSwordPoints() < perk.getCost()) {
            Form.at(player, Prefix.ERROR, "You don't have enough perk points to buy this perk!");
            return;
        }
        sword.addPerkLevel(player, perk);
        perkMenu.setItem(event.getSlot(), perk.getIcon(sword.getPerkLevel(perk)));
        perkMenu.setItem(SwordPerk.getPerks().size(), sword.getPointsIcon());
    }

    @EventHandler()
    public void onDeath(PlayerDeathEvent event) {
        Sword sword = PrisonUser.fromPlayer(event.getEntity()).getSword();
        for(ItemStack itemStack : new ArrayList<>(event.getDrops())) {
            if(itemStack.getType().name().contains("SWORD"))
                event.getDrops().remove(itemStack);
        }
        if (!sword.isInUse())
            return;
        sword.reset();

        Player player = FFADamageTracker.getLastDamager(event.getEntity());
        if (player == null)
            return;
        PrisonUser user = PrisonUser.fromPlayer(player);
        if (user != null) {
            user.getSword().levelUp();
            player.getInventory().setItem(0, user.getSword().getSword());
            Form.at(player, Prefix.VAULT_CRAFT, "You gained a Sword Perk. Drop your sword to upgrade it!");
        }
        FFADamageTracker.reset(event.getEntity());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;
        Player defender = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();
        if(FFAPlayer.getFFAPlayerFromPlayer(attacker) == null || !FFAPlayer.getFFAPlayerFromPlayer(attacker).isPlaying())
            return;
        for (SwordPerk perk : SwordPerk.getPerks()) {
            int defenderLevel = PrisonUser.fromPlayer(defender).getSword().getPerkLevel(perk);
            int attackerLevel = PrisonUser.fromPlayer(attacker).getSword().getPerkLevel(perk);
            if (defenderLevel > 0 && defender.isBlocking())
                perk.onDefend(defender, attacker, defenderLevel);
            if (attackerLevel > 0)
                perk.onHit(attacker, defender, attackerLevel);
        }
        Sword sword = PrisonUser.fromPlayer(attacker).getSword();
        if (sword.isInUse())
            attacker.getInventory().setItem(0, sword.getSword());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Sword sword = PrisonUser.fromPlayer(event.getPlayer()).getSword();
        if (sword.isInUse())
            event.getPlayer().getInventory().setItem(0, sword.getSword());
    }
}
