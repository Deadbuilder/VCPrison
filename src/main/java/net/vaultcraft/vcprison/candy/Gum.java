package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.util.Arrays;

/**
 * @author Connor Hollasch
 * @since 11/1/2014
 */
public class Gum extends Candy {

    public ShapedRecipe getRecipe() {
        ShapedRecipe rc = new ShapedRecipe(getCandyItem());
        rc.shape("XYX", "QZQ", "XYX");
        rc.setIngredient('X', Material.QUARTZ_ORE);
        rc.setIngredient('Y', Material.STONE);
        rc.setIngredient('Q', Material.STONE);
        rc.setIngredient('Z', Material.SUGAR);
        return rc;
    }

    private static ItemStack stack;
    protected static ItemStack chewed = ItemUtils.build(Material.INK_SACK, (byte)15, ChatColor.translateAlternateColorCodes('&', "&d&lChewed gum"), "Drop this to stick players onto the ground!");

    static {
        stack = new ItemStack(Material.INK_SACK, 1, (short)13);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&d&lBubble Gum"));
        meta.setLore(Arrays.asList(new String[]{"Consume to receive chewed gum, chewed gum can", "be dropped on the ground to give players slowness"}));
        stack.setItemMeta(meta);
    }

    public ItemStack getCandyItem() {
        return stack;
    }

    @Override
    public ItemStack onCandyConsume(Player player) {
        return chewed;
    }
}
