package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

/**
 * Created by CraftFest on 11/2/2014.
 */
public class VCCraft extends ICommand {

    private Inventory inv;

    public VCCraft(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] strings) {
        inv = Bukkit.getServer().createInventory(null, InventoryType.CRAFTING);
        player.openInventory(inv);
    }
}
