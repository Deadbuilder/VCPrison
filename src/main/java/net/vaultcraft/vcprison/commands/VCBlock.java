package net.vaultcraft.vcprison.commands;

import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * Created by tacticalsk8er on 11/5/2014.
 */
public class VCBlock extends ICommand {

    public VCBlock(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    //Emerald
    //Diamonds
    //Coal
    //Iron
    //Lapis

    @Override
    public void processCommand(Player player, String[] strings) {
        int iron = 0, diamond = 0, coal = 0, emerald = 0, lapis = 0;
        int ironBlock = 0, diamondBlock = 0, coalBlock = 0, emeraldBlock = 0, lapisBlock = 0;

        for(ItemStack itemStack : player.getInventory().getContents()) {
            if(itemStack == null)
                continue;
            if(itemStack.getType().equals(Material.IRON_INGOT))
                iron += itemStack.getAmount();
            if(itemStack.getType().equals(Material.DIAMOND))
                diamond += itemStack.getAmount();
            if(itemStack.getType().equals(Material.EMERALD))
                emerald += itemStack.getAmount();
            if(itemStack.getType().equals(Material.COAL))
                coal += itemStack.getAmount();
            if(itemStack.getData().equals(new MaterialData(Material.INK_SACK, (byte)4)))
                lapis += itemStack.getAmount();
        }

        if(iron / 9 != 0) {
            ironBlock = iron / 9;
            ItemStack itemStack = new ItemStack(Material.IRON_BLOCK, ironBlock);
            ItemStack itemStack1 = new ItemStack(Material.IRON_INGOT, ironBlock * 9);
            player.getInventory().remove(itemStack1);
            player.getInventory().addItem(itemStack);
        }

        if(diamond / 9 != 0) {
            diamondBlock = diamond / 9;
            ItemStack itemStack = new ItemStack(Material.DIAMOND_BLOCK, diamondBlock);
            ItemStack itemStack1 = new ItemStack(Material.DIAMOND, diamondBlock * 9);
            player.getInventory().remove(itemStack1);
            player.getInventory().addItem(itemStack);
        }

        if(coal / 9 != 0) {
            coalBlock = coal / 9;
            ItemStack itemStack = new ItemStack(Material.COAL_BLOCK, coalBlock);
            ItemStack itemStack1 = new ItemStack(Material.COAL, coalBlock * 9);
            player.getInventory().remove(itemStack1);
            player.getInventory().addItem(itemStack);
        }

        if(emerald / 9 != 0) {
            emeraldBlock = emerald / 9;
            ItemStack itemStack = new ItemStack(Material.EMERALD_BLOCK, emeraldBlock);
            ItemStack itemStack1 = new ItemStack(Material.EMERALD, emerald);
            player.getInventory().remove(itemStack1);
            player.getInventory().addItem(itemStack);
        }

        if(lapis / 9 != 0) {
            lapisBlock = lapis / 9;
            ItemStack itemStack = new ItemStack(Material.LAPIS_BLOCK, lapisBlock);
            ItemStack itemStack1 = new ItemStack(Material.INK_SACK, lapis, (short)0, (byte)4);
            player.getInventory().remove(itemStack1);
            player.getInventory().addItem(itemStack);
        }

        Form.at(player, Prefix.SUCCESS, "You items have turned into blocks!");
    }
}
