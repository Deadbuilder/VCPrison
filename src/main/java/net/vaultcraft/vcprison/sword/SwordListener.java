package net.vaultcraft.vcprison.sword;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.mine.warp.WarpGUI;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.user.UserLoadedEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by tacticalsk8er on 10/5/2014.
 */
public class SwordListener implements Listener {

    public SwordListener () {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onUserLoad(UserLoadedEvent event) {
        PrisonUser user = PrisonUser.fromPlayer(event.getUser().getPlayer());
        if (event.getUser().getUserdata("Pickaxe") != null) {
            //user.setSword(new Sword(user.getPlayer(), event.getUser().getUserdata("Sword")));
        } else {
            user.setSword(new Sword(user.getPlayer()));
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getSlotType() == InventoryType.SlotType.QUICKBAR) {
            if (event.getCurrentItem().getType().name().contains("SWORD")) {
                if(event.getAction().name().contains("DROP"))
                    event.getWhoClicked().openInventory(PrisonUser.fromPlayer((Player) event.getWhoClicked()).getSword().getStatsMenu());
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
        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        Sword sword = PrisonUser.fromPlayer(player).getSword();
        if(!sword.isInUse())
            return;
        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Perk Points:")) {
            if (sword.getSwordPoints() == 0) {
                Form.at(player, Prefix.ERROR, "You have no perk points!");
                return;
            }
            HashMap<Integer, ItemStack> noRoom;
            noRoom = player.getInventory().addItem(Sword.getAddPointItem());
            if (noRoom.isEmpty()) {
                Form.at(player, Prefix.SUCCESS, "You have 1 more perk point item in your inventory.");
                sword.setSwordPoints(sword.getSwordPoints() - 1);
                perkMenu.setItem(SwordPerk.getPerks().size(), sword.getPointsIcon());
                return;
            } else {
                Form.at(player, Prefix.ERROR, "You need to clear space in your inventory!");
                return;
            }
        }
        if (event.getCurrentItem().getItemMeta().getDisplayName().contains("Warps")) {
            player.closeInventory();
            player.openInventory(WarpGUI.create(PrisonUser.fromPlayer(player)));
            return;
        }
        SwordPerk perk = SwordPerk.getPerkFromName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().replace("Toggle Off ", "").replace("Toggle On ", "").replaceAll(" \\d+", "").replace(" Max", "")));
        if (sword.getPerkLevel(perk) == perk.getMaxLevel()) {
            Form.at(player, Prefix.ERROR, "Sword perk is at it highest level!");
            return;
        }
        if (perk.isTogglable()) {
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

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Sword sword = PrisonUser.fromPlayer(event.getEntity()).getSword();
        event.getDrops().remove(sword.getSword());
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player))
            return;
        Player defender = (Player) event.getEntity();
        Player attacker = (Player) event.getDamager();
        for(SwordPerk perk : SwordPerk.getPerks()) {
            int defenderLevel = PrisonUser.fromPlayer(defender).getSword().getPerkLevel(perk);
            int attackerLevel = PrisonUser.fromPlayer(attacker).getSword().getPerkLevel(perk);
            if(defenderLevel > 0 && defender.isBlocking())
                perk.onDefend(defender, attacker, defenderLevel);
            if(attackerLevel > 0)
                perk.onHit(attacker, defender, attackerLevel);
        }

        PrisonUser.fromPlayer(attacker).getSword().hit();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Sword sword = PrisonUser.fromPlayer(event.getPlayer()).getSword();
        if(!sword.isInUse())
            return;
        if (event.getAction().name().contains("RIGHT")) {
            if (event.getPlayer().getItemInHand() != null) {
                if (event.getPlayer().getItemInHand().getItemMeta() != null) {
                    if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName() != null) {
                        if (event.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&6&lRight Click: &2&lAdd Perk Point"))) {
                            if(PrisonUser.fromPlayer(event.getPlayer()).getPickaxe().isInUse()) {
                                if (event.getPlayer().getItemInHand().getAmount() != 1)
                                    event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount() - 1);
                                else
                                    event.getPlayer().getInventory().remove(event.getPlayer().getItemInHand());
                                PrisonUser.fromPlayer(event.getPlayer()).getSword().setSwordPoints(PrisonUser.fromPlayer(event.getPlayer()).getSword().getSwordPoints() + 1);
                                Form.at(event.getPlayer(), Prefix.SUCCESS, "You have added a perk point to your sword!");
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Sword sword = PrisonUser.fromPlayer(event.getPlayer()).getSword();
        if(sword.isInUse())
            event.getPlayer().getInventory().setItem(0, sword.getSword());
    }
}
