package net.vaultcraft.vcprison.vote;

import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.voting.rewards.VoteReward;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author Connor Hollasch
 * @since 1/4/2015
 */
public class MoneyReward implements VoteReward {

    public Material getIdentifier() {
        return Material.GOLD_INGOT;
    }

    public String[] reward(Player player) {
        PrisonUser pu = PrisonUser.fromPlayer(player);
        double money = (Rank.next(pu.getRank()).getCost() / 1000.0);
        pu.getUser().addMoney(money);
        return new String[]{ChatColor.translateAlternateColorCodes('&', "&a&l$" + money + "!")};
    }
}
