package net.vaultcraft.vcprison.pickaxe;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by tacticalsk8er on 8/3/2014.
 */
public class AutoSmeltPerk extends PickaxePerk {

    public AutoSmeltPerk(Material icon, Material toggleOn, Material toggleOff, String name, int cost, boolean initState, String... lore) {
        super(icon, toggleOn, toggleOff, name, cost, initState, lore);
    }

    @Override
    public List<String> changeLore(Player player, List<String> lore, int level) {
        lore.add(ChatColor.GRAY + "Auto Smelt");
        return lore;
    }

    @Override
    public ItemStack onBreak(Player player, BlockBreakEvent event, Block block, ItemStack itemStack, int level) {
        if(isSmeltable(itemStack.getType()))
            itemStack.setType(smelt(itemStack.getType()));
        return itemStack;
    }

    private boolean isSmeltable(Material type) {
        switch (type) {
            case GOLD_ORE:
                return true;
            case IRON_ORE:
                return true;
            case COBBLESTONE:
                return true;
            default:
                return false;
        }
    }

    private Material smelt(Material type) {
        switch (type) {
            case GOLD_ORE:
                return Material.GOLD_INGOT;
            case IRON_ORE:
                return Material.IRON_INGOT;
            case COBBLESTONE:
                return Material.STONE;
            default:
                return type;
        }
    }




}
