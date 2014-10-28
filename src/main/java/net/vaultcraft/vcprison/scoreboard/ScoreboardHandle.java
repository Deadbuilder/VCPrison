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
import net.vaultcraft.vcutils.user.User;
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
        text.put(15, " ");
        text.put(14, "&5Current Rank");
        text.put(13, "&7{rank}");
        text.put(12, "  ");
        text.put(11, "&5Drop Party In");
        text.put(10, "&7{dp}");
        text.put(9, "   ");
        text.put(8, "&5Balance");
        text.put(7, "&7${bal}");
        text.put(6, "    ");
        text.put(5, "&5Next Rank");
        text.put(4, "&7{next}");
        text.put(3, "     ");
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
            use = use.replace("{bal}", Form.at(user.getUser().getMoney(), true));
            use = use.replace("{next}", percent(user.getPlayer()));
            use = use.replace("{ffa}", VCPrison.getFFA().size()+"");

            score.setName(ChatColor.translateAlternateColorCodes('&', use));
        }
    }

    private static String percent(Player player) {
        PrisonUser pu = PrisonUser.fromPlayer(player);
        User u = pu.getUser();

        double has = u.getMoney();
        double needs = Rank.next(pu.getRank()).getCost();

        double left = needs - has;

        int percent = (int)((left / needs)*5);
        String build = "&5";
        for (int x = 0; x < percent; x++) {
            build+="▋";
        }

        build+="&7";
        for (int x = 0; x < (5 - percent); x++) {
            build+="▋";
        }

        build+=(((left / needs) * 100)+"%");
        return build;
    }
}
