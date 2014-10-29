package net.vaultcraft.vcprison.cells;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.entity.Player;

import java.util.List;

public class VCListCell extends ICommand {

    public VCListCell(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        List<Cell> cells = VCPrison.getInstance().getCellManager().getCellsFromPlayer(player);
        Form.at(player, Prefix.VAULT_CRAFT, "You own " + cells.size() + " cells.");
        for(Cell c : cells) {
            Form.at(player, Prefix.VAULT_CRAFT, "Cell at " + c.chunkX + " , " + c.chunkZ);
        }
    }
}
