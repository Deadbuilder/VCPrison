package net.vaultcraft.vcprison.cells;

import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.chat.Form;
import net.vaultcraft.vcutils.chat.Prefix;
import net.vaultcraft.vcutils.command.ICommand;
import net.vaultcraft.vcutils.user.Group;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class VCGetCell extends ICommand {
    public VCGetCell(String name, Group permission, String... aliases) {
        super(name, permission, aliases);
    }

    @Override
    public void processCommand(Player player, String[] args) {
        //TODO Remove this command
        Chunk nextFree = VCPrison.getInstance().getCellManager().getNextOpenCell();
        if(nextFree == null) {
            Form.at(player, Prefix.ERROR, "Ask VC to smack CyberKisune pls");
            return;
        }

        Cell newCell = new Cell();
        newCell.ownerUUID = player.getUniqueId();
        newCell.chunkX = nextFree.getX();
        newCell.chunkZ = nextFree.getZ();
        newCell.cellSpawn = new Location(nextFree.getWorld(), (nextFree.getX()*16) + 13, 88, (nextFree.getZ()*16) + 12);
        VCPrison.getInstance().getCellManager().addCell(newCell);
        player.teleport(newCell.cellSpawn);
        Form.at(player, Prefix.VAULT_CRAFT, "Teleported to your new cell at ROW " + nextFree.getX() + " COL " + nextFree.getZ());
    }
}
