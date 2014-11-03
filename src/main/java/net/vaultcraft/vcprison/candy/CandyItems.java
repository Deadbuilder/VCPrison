package net.vaultcraft.vcprison.candy;

import net.vaultcraft.vcutils.item.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * @author Connor Hollasch
 * @since 11/2/2014
 */
public class CandyItems {

    public static final ItemStack WRAPPER = ItemUtils.build(Material.QUARTZ, ChatColor.translateAlternateColorCodes('&', "&7&lCandy Wrapper"),
            "You'll need something to contain sticky candies!");
    public static final ItemStack BUTTER = ItemUtils.build(Material.INK_SACK, (byte) 11, ChatColor.translateAlternateColorCodes('&', "&e&lButter"),
            "Used to create some types of candies.");
    public static final ItemStack COCOA = ItemUtils.build(Material.CLAY_BRICK, ChatColor.GOLD.toString() + ChatColor.BOLD + "Co-Coa",
            "Used to create some types of candies.");
    public static final ItemStack JAWBREAKER = ItemUtils.build(Material.SNOW_BALL, ChatColor.translateAlternateColorCodes('&', "&lJawbreaker"),
            "Ow! Jawbreakers can do some serious damage to your teeth!");
    public static final ItemStack CHEWEDGUM = ItemUtils.build(Material.INK_SACK, (byte)15, ChatColor.translateAlternateColorCodes('&', "&d&lChewed gum"),
            "Drop this to stick players onto the ground!");
    public static final ItemStack GUM = ItemUtils.build(Material.INK_SACK, (byte)13,
            ChatColor.translateAlternateColorCodes('&', "&d&lBubble Gum"),
            "Consume to receive chewed gum, chewed gum can", "be dropped on the ground to give players slowness.");
    public static final ItemStack SUGARCUBE = ItemUtils.build(Material.SNOW_BLOCK, ChatColor.translateAlternateColorCodes('&', "&lSugar Cube"),
            "Core item used to create new candies.",
            "Sugar cubes may also be consumed to receive ", "speed and jump boosts.",
            ChatColor.translateAlternateColorCodes('&', "&c&lWARNING&f: &5&oEating too many sugar cubes in such"), "short time can have negative effects!");
    public static final ItemStack BUTTERSCOTCH = ItemUtils.build(Material.INK_SACK, (byte)14, ChatColor.translateAlternateColorCodes('&', "&6&lButterscotch"),
            "Receive 30 minutes of fire resistance.");
    public static final ItemStack SOURPATCH = ItemUtils.build(Material.MELON, ChatColor.translateAlternateColorCodes('&', "&2&lSour&6&lPatch"),
            "Consume to get a Thorns effect for 15 seconds.");
    public static final ItemStack USEDWRAPPER = ItemUtils.build(Material.INK_SACK, (byte)7, ChatColor.translateAlternateColorCodes('&', "&8&lUsed Candy Wrapper"),
            "Smelt to get 4 rubber back.");
    public static final ItemStack CHOCOLATEBAR = ItemUtils.build(Material.NETHER_BRICK, ChatColor.translateAlternateColorCodes('&', "&6&lChocolate Bar"),
            "Consume to get 20 seconds of invisibility" , "and 10 seconds of resistance.");
    public static final ItemStack CANDYAPPLE = ItemUtils.build(Material.APPLE, ChatColor.translateAlternateColorCodes('&', "&d&lCandy &c&lApple"),
            "Right to throw. After 3 seconds, near",
            "by players will receive slowness and",
            "blindness.");
    public static final ItemStack RUBBER = ItemUtils.build(Material.INK_SACK, (byte)8, ChatColor.translateAlternateColorCodes('&', "&o&lRubber"),
            "Used to create gum and wrappers.");
    public static final ItemStack WARHEAD = ItemUtils.build(Material.MAGMA_CREAM, ChatColor.translateAlternateColorCodes('&', "&e&lWarHead"),
            "Consume to get 10 seconds of Strength");
    public static final ItemStack COOKIE = ItemUtils.build(Material.COOKIE, ChatColor.translateAlternateColorCodes('&', "&6&lC&e&lO&6&lO&e&lK&6&lI&e&lE"),
            "Consume to rain down Cocoa Beans",
            "that explode!");
    public static final ItemStack SWEDISHFISH = ItemUtils.build(Material.RAW_FISH, (byte)1, ChatColor.translateAlternateColorCodes('&', "&4&lSwedish Fish"),
            "Consume to get 10 seconds of speed",
            "and 5 seconds of resistance.");
    public static final ItemStack REDDYE = ItemUtils.build(Material.INK_SACK,(byte)1, ChatColor.translateAlternateColorCodes('&', "&4&lApple Skin"),
            "Used to create some types of candies.");
    public static final ItemStack NETHERSTAR = ItemUtils.build(Material.NETHER_STAR, ChatColor.translateAlternateColorCodes('&', "&5&lCandy&f&lCore"),
            "Used to create some types of candies.");
    public static final ItemStack NETHERWART = ItemUtils.build(Material.NETHER_WARTS, ChatColor.translateAlternateColorCodes('&', "&4&lFish &7&lScales"),
            "Used to create some types of candies.");
    public static final ItemStack PINKDYE = ItemUtils.build(Material.INK_SACK, (byte)9, ChatColor.translateAlternateColorCodes('&', "&d&lGum Flavoring"),
            "Used to create gum.");
}
