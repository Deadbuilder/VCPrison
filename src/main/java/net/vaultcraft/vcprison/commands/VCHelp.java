package net.vaultcraft.vcprison.commands;

import com.google.common.collect.Lists;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Created by CraftFest on 10/9/2014.
 */

public class VCHelp extends ICommand {

    public static final ItemStack MINES = build(Material.DIAMOND_PICKAXE, "&d&lMines", "&7Mines are where you collect", "&7materials to sell! Use the", "&7command &6/warp &7to view", "&7the mining GUI.", "&7Rank up using &6/rankup&7!");
    public static final ItemStack CELLS = build(Material.IRON_FENCE, "&d&lCells", "&7Cells are where you call home.", "&7Use the command &6/plot &7for", "&7further usage. You can build,", "&7farm and sell items in your", "&7cells!");
    public static final ItemStack GANGS = build(Material.DIAMOND_HELMET, "&d&lGangs", "&7Gangs are groups of prisoners.", "&7Without a group you are left", "&7unprotected. Use the command", "&6/gang &7for more information.");
    public static final ItemStack SHOP = build(Material.EMERALD, "&d&lShop", "&7The shop is located at the", "&7spawn. Use the command &6/spawn", "&7to get there! You can buy", "&7all prison items there.");
    public static final ItemStack FFA = build(Material.DIAMOND_SWORD, "&d&lFFA", "&7FFA or Free For All is", "&7where prisoners go to fight.", "&7Upgrade your sword and use", "&7armor! Watch your back. Use", "&6/ffa &7 for more information.");

    public static Inventory phelp = Bukkit.createInventory(null, 9, "VaultCraft - Prison Guide");

    public VCHelp(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] strings) {
        phelp.setItem(0, new ItemStack(MINES));
        phelp.setItem(2, new ItemStack(CELLS));
        phelp.setItem(4, new ItemStack(GANGS));
        phelp.setItem(6, new ItemStack(SHOP));
        phelp.setItem(8, new ItemStack(FFA));
        player.openInventory(phelp);
    }

    public static ItemStack build(Material type, String displayName, String... lore) {
        return build(type, (byte)0, displayName, lore);
    }

    public static ItemStack build(Material type, byte data, String displayName, String... lore) {
        ItemStack stack = new ItemStack(type, 1, (short)1, data);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
        if (lore.length > 0) {
            List<String> l = Lists.newArrayList();
            for (String s : lore) {
                l.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            meta.setLore(l);
        }
        stack.setItemMeta(meta);
        return stack;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory().getName().equals("VaultCraft - Prison Guide"))
            event.setCancelled(true);
    }

}
