package net.vaultcraft.vcprison.scoreboard;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcprison.commands.VCDropParty;
import net.vaultcraft.vcprison.event.DropParty;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.scoreboard.VCDisplay;
import net.vaultcraft.vcutils.scoreboard.VCObjective;
import net.vaultcraft.vcutils.scoreboard.VCScore;
import net.vaultcraft.vcutils.scoreboard.VCScoreboard;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author Connor Hollasch
 * @since 10/17/14
 */
public class ScoreboardHandle implements Runnable {

    private HashMap<Integer, String> text = new HashMap<>();
    private VCScoreboard board;
    private VCObjective current;
    private Player player;

    public ScoreboardHandle(Player player) {
        board = new VCScoreboard(player);
        this.player = player;
        text.put(12, " ");
        text.put(11, "&5Current Rank");
        text.put(10, "&7{rank}");
        text.put(9, "  ");
        text.put(8, "&5Next DP");
        text.put(7, "&7{dp}");
        text.put(6, "   ");
        text.put(5, "&5Rankup Cost");
        text.put(4, "&7${next}");
        text.put(3, "    ");
        text.put(2, "&5Players In FFA");
        text.put(1, "&7{ffa}");
    }

    public void run() {
        PrisonUser user = PrisonUser.fromPlayer(player);

        if (current == null) {
            current = new VCObjective(ChatColor.translateAlternateColorCodes('&', "&5&lVaultCraft"));

            for (int x : text.keySet()) {
                String txt = text.get(x);
                current.addScore(new VCScore(txt, x, current));
            }

            current.addScoreboardAndDisplay(board, VCDisplay.SIDEBAR);
        }

        for (int x : text.keySet()) {
            VCScore score = current.getFirstScore(x);
            String use = text.get(x);

            use = use.replace("{rank}", user.getRank().toString());
            use = use.replace("{dp}", VCDropParty.MMSS(DropParty.getInstance().getTimeLeft())+"");
            use = use.replace("{next}", Form.at(Rank.next(user.getRank()).getCost(), true));
            use = use.replace("{ffa}", VCPrison.getFFA().size()+"");

            score.setName(ChatColor.translateAlternateColorCodes('&', use));
        }
    }
}
