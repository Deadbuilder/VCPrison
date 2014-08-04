package net.vaultcraft.vcprison.pickaxe;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Created by tacticalsk8er on 8/1/2014.
 */
public class PickaxeListener implements Listener {

    public PickaxeListener() {
        Bukkit.getPluginManager().registerEvents(this, VCPrison.getInstance());
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null)
            if (event.getCurrentItem().getType() == Material.DIAMOND_PICKAXE)
                event.setCancelled(true);
        if (event.getAction() == InventoryAction.HOTBAR_SWAP) {
            event.setCancelled(true);
        }

        if (event.getView().getTopInventory() != null) {
            Inventory inventory = event.getView().getTopInventory();
            if (inventory.getName().contains("Pickaxe")) {
                event.setCancelled(true);
                Player player = (Player) event.getWhoClicked();
                Pickaxe pickaxe = PrisonUser.fromPlayer((Player) event.getWhoClicked()).getPickaxe();
                if(event.getCurrentItem() != null) {
                    if(event.getCurrentItem().getItemMeta() != null) {
                        if (event.getCurrentItem().getItemMeta().getDisplayName() != null) {
                            String itemName = event.getCurrentItem().getItemMeta().getDisplayName();
                            ItemStack up = event.getCurrentItem();

                            switch (itemName) {
                                case "Efficiency":
                                    if(pickaxe.getPICK_POINTS() < 1) {
                                        Form.at(player, Prefix.ERROR, "You don't have enough Pickaxe Stat Points!");
                                        break;
                                    }
                                    pickaxe.setDIG_SPEED_LEVEL(pickaxe.getDIG_SPEED_LEVEL() + 1);
                                    pickaxe.setPICK_POINTS(pickaxe.getPICK_POINTS() - 1);
                                    Form.at(player, Prefix.SUCCESS, "You bought one level of Efficiency!");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    up.setAmount(up.getAmount()+1);
                                    break;
                                case "Fortune":
                                    if(pickaxe.getPICK_POINTS() < 1) {
                                        Form.at(player, Prefix.ERROR, "You don't have enough Pickaxe Stat Points!");
                                        break;
                                    }
                                    pickaxe.setFORTUNE_LEVEL(pickaxe.getFORTUNE_LEVEL() + 1);
                                    pickaxe.setPICK_POINTS(pickaxe.getPICK_POINTS() - 1);
                                    Form.at(player, Prefix.SUCCESS, "You bought one level of Fortune!");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    up.setAmount(up.getAmount()+1);
                                    break;
                                case "Haste":
                                    if(pickaxe.getHASTE_LEVEL() == 4) {
                                        Form.at(player, Prefix.ERROR, "You have hit the level limit for Haste!");
                                        break;
                                    }
                                    if(pickaxe.getPICK_POINTS() < 5) {
                                        Form.at(player, Prefix.ERROR, "You don't have enough Pickaxe Stat Points!");
                                        break;
                                    }
                                    pickaxe.setHASTE_LEVEL(pickaxe.getHASTE_LEVEL() + 1);
                                    pickaxe.setPICK_POINTS(pickaxe.getPICK_POINTS() - 5);
                                    Form.at(player, Prefix.SUCCESS, "You bought one level of Haste!");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    up.setAmount(up.getAmount()+1);
                                    break;
                                case "Auto Smelt":
                                    if(pickaxe.getPICK_POINTS() < 8) {
                                        Form.at(player, Prefix.ERROR, "You don't have enough Pickaxe Stat Points!");
                                        break;
                                    }
                                    pickaxe.setHAS_AUTO_SMELT(true);
                                    pickaxe.setAUTO_SMELT(true);
                                    pickaxe.setPICK_POINTS(pickaxe.getPICK_POINTS() - 8);
                                    Form.at(player, Prefix.SUCCESS, "You bought Auto Smelt. You can toggle this on and off in the Pickaxe Stats menu.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    up.setAmount(up.getAmount()+1);
                                    break;
                                case "Toggle Auto Smelt Off":
                                    pickaxe.setAUTO_SMELT(false);
                                    Form.at(player, Prefix.SUCCESS, "You turned off Auto Smelt.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    player.openInventory(pickaxe.getStatsMenu());
                                    break;
                                case "Toggle Auto Smelt On":
                                    pickaxe.setAUTO_SMELT(true);
                                    Form.at(player, Prefix.SUCCESS, "You turned on Auto Smelt.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    player.openInventory(pickaxe.getStatsMenu());
                                    break;
                                case "Silk Touch":
                                    if(pickaxe.getPICK_POINTS() < 8) {
                                        Form.at(player, Prefix.ERROR, "You don't have enough Pickaxe Stat Points!");
                                        break;
                                    }
                                    pickaxe.setHAS_SILK_TOUCH(true);
                                    pickaxe.setSILK_TOUCH(true);
                                    pickaxe.setPICK_POINTS(pickaxe.getPICK_POINTS() - 8);
                                    Form.at(player, Prefix.SUCCESS, "You bought Silk Touch. You can toggle this on and off in the Pickaxe Stats menu.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    player.openInventory(pickaxe.getStatsMenu());
                                    break;
                                case "Toggle Silk Touch Off":
                                    pickaxe.setSILK_TOUCH(false);
                                    Form.at(player, Prefix.SUCCESS, "You turned off Silk Touch.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    player.openInventory(pickaxe.getStatsMenu());
                                    break;
                                case "Toggle Silk Touch On":
                                    pickaxe.setSILK_TOUCH(true);
                                    Form.at(player, Prefix.SUCCESS, "You turned on Silk Touch.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    player.openInventory(pickaxe.getStatsMenu());
                                    break;
                                case "Night Vision":
                                    if(pickaxe.getPICK_POINTS() < 8) {
                                        Form.at(player, Prefix.ERROR, "You don't have enough Pickaxe Stat Points!");
                                        break;
                                    }
                                    pickaxe.setHAS_NIGHT_VISION(true);
                                    pickaxe.setNIGHT_VISION(true);
                                    pickaxe.setPICK_POINTS(pickaxe.getPICK_POINTS() - 8);
                                    Form.at(player, Prefix.SUCCESS, "You bought Night Vision. You can toggle this on and off in the Pickaxe Stats menu.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    player.openInventory(pickaxe.getStatsMenu());
                                    break;
                                case "Toggle Night Vision Off":
                                    pickaxe.setNIGHT_VISION(false);
                                    Form.at(player, Prefix.SUCCESS, "You turned off Night Vision.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    player.openInventory(pickaxe.getStatsMenu());
                                    break;
                                case "Toggle Night Vision On":
                                    pickaxe.setNIGHT_VISION(true);
                                    Form.at(player, Prefix.SUCCESS, "You turned on Night Vision.");
                                    player.getInventory().setItem(0, pickaxe.getPickaxe());
                                    player.openInventory(pickaxe.getStatsMenu());
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onHotbarHover(PlayerItemHeldEvent event) {
        if(event.getNewSlot() == 0) {
            Pickaxe pickaxe = PrisonUser.fromPlayer(event.getPlayer()).getPickaxe();
            if(pickaxe.getHASTE_LEVEL() > 0)
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, Integer.MAX_VALUE, pickaxe.getHASTE_LEVEL() - 1, false));
            if(pickaxe.isNIGHT_VISION())
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 0, false));
        }

        if(event.getPreviousSlot() == 0) {
            Player player = event.getPlayer();
            if(player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
                player.removePotionEffect(PotionEffectType.FAST_DIGGING);
            if(player.hasPotionEffect(PotionEffectType.NIGHT_VISION))
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.DIAMOND_PICKAXE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            if(event.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
                Pickaxe pickaxe = PrisonUser.fromPlayer(event.getPlayer()).getPickaxe();
                event.getPlayer().openInventory(pickaxe.getStatsMenu());
            }
        }
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if (event.getRecipe().getResult().getType() == Material.DIAMOND_PICKAXE)
            event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.isCancelled())
            return;
        event.setCancelled(true);
        ItemStack item = new ItemStack(changeType(event.getBlock().getType()));
        PrisonUser user = PrisonUser.fromPlayer(event.getPlayer());
        if(user.getPickaxe().isSILK_TOUCH())
            item.setType(event.getBlock().getType());
        int amount = 1;
        if (user.getPlayer().getItemInHand().getType() == Material.DIAMOND_PICKAXE) {
            user.getPickaxe().mineBlock();
            if (user.getPickaxe().isAUTO_SMELT())
                item.setType(canSmelt(item.getType()));
            if (isFortuneBlock(item.getType()))
                amount = user.getPickaxe().getFortuneItems();
        }
        item.setAmount(amount);
        user.getPlayer().getInventory().addItem(item);
        event.getBlock().setType(Material.AIR);
    }

    public boolean isFortuneBlock(Material type) {
        switch (type) {
            case COAL:
                return true;
            case DIAMOND:
                return true;
            case IRON_INGOT:
                return true;
            case GOLD_INGOT:
                return true;
            case EMERALD:
                return true;
            default:
                return false;
        }
    }

    public Material changeType(Material type) {
        switch (type) {
            case COAL_ORE:
                return Material.COAL;
            case DIAMOND_ORE:
                return Material.DIAMOND;
            case EMERALD_ORE:
                return Material.EMERALD;
            case STONE:
                return Material.COBBLESTONE;
            default:
                return type;
        }
    }

    public Material canSmelt(Material type) {
        switch (type) {
            case IRON_ORE:
                return Material.IRON_INGOT;
            case GOLD_ORE:
                return Material.GOLD_INGOT;
            default:
                return type;
        }
    }
}
