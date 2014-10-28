package net.vaultcraft.vcprison.cells;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vaultcraft.vcprison.user.PrisonUser;
import net.vaultcraft.vcutils.VCUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CellManager {

    private World plotWorld;
    public CellManager(World plotWorld) {
        this.plotWorld = plotWorld;
    }

    public List<Cell> getCellsFromPlayer(Player player) {
        PrisonUser user = PrisonUser.fromPlayer(player);

        List<DBObject> objects = VCUtils.getInstance().getMongoDB().queryMutiple(VCUtils.mongoDBName, "Cells", "OwnerUUID", player.getUniqueId().toString());
        ArrayList<Cell> cells = new ArrayList<>();
        if(objects == null) {
            return cells;
        }
        for (DBObject o : objects) {
            Cell cell = new Cell();
            cell.ownerUUID = UUID.fromString((String) o.get("OwnerUUID"));
            String[] chunkString = ((String) o.get("Chunk")).split(",");
            cell.chunkX = Integer.parseInt(chunkString[0]);
            cell.chunkZ = Integer.parseInt(chunkString[1]);
            for (String s : ((String) o.get("Members")).split(",")) {
                cell.additionalUUIDs.add(UUID.fromString(s));
            }
            cell.name = (String) o.get("Name");
            cell.cellSpawn = stringToLocation((String) o.get("SpawnPoint"));
        }
        return cells;
    }

    public Cell getCellFromLocation(Location location) {
        if(location.getWorld() != this.plotWorld) {
            throw new IllegalArgumentException("World must be ChunkWorld!");
        }
        DBObject o = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Cells", "Chunk", location.getChunk().getX() + "," + location.getChunk().getZ());
        if(o == null) {
            return null;
        }
        Cell cell = new Cell();
        cell.ownerUUID = UUID.fromString((String) o.get("OwnerUUID"));
        String[] chunkString = ((String) o.get("Chunk")).split(",");
        cell.chunkX = Integer.parseInt(chunkString[0]);
        cell.chunkZ = Integer.parseInt(chunkString[1]);
        for(String s : ((String)o.get("Members")).split(",")) {
            cell.additionalUUIDs.add(UUID.fromString(s));
        }
        cell.name = (String) o.get("Name");
        cell.cellSpawn = stringToLocation((String) o.get("SpawnPoint"));


        return cell;
    }

    public void addOrUpdateCell(Cell theCell, Player updater) {
        Cell dbCell = getCellFromLocation(theCell.cellSpawn);
        if(dbCell == null) {
            DBObject o = new BasicDBObject();
            o.put("OwnerUUID", theCell.ownerUUID.toString());
            o.put("Chunk", theCell.cellSpawn.getChunk().getX() + "," + theCell.cellSpawn.getChunk().getZ());
            StringBuilder sb = new StringBuilder();
            for(UUID u : theCell.additionalUUIDs) {
                sb.append(u.toString()).append(",");
            }
            o.put("Members", sb.toString());
            o.put("Name", theCell.name);
            o.put("SpawnPoint", locationToString(theCell.cellSpawn));
            VCUtils.getInstance().getMongoDB().insert(VCUtils.mongoDBName, "Cells", o);
        } else {
            // Update cell
            DBObject o = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Cells", "Chunk", theCell.cellSpawn.getChunk().getX() + "," + theCell.cellSpawn.getChunk().getZ());
            o.put("OwnerUUID", theCell.ownerUUID.toString());
            o.put("Chunk", theCell.cellSpawn.getChunk().getX() + "," + theCell.cellSpawn.getChunk().getZ());
            StringBuilder sb = new StringBuilder();
            for(UUID u : theCell.additionalUUIDs) {
                sb.append(u.toString()).append(",");
            }
            o.put("Members", sb.toString());
            o.put("Name", theCell.name);
            o.put("SpawnPoint", locationToString(theCell.cellSpawn));
            DBObject o1 = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Cells", "Chunk", theCell.cellSpawn.getChunk().getX() + "," + theCell.cellSpawn.getChunk().getZ());
            VCUtils.getInstance().getMongoDB().update(VCUtils.mongoDBName, "Cells", o1, o);
        }
    }

    private Location stringToLocation(String s) {
        String[] strings = s.split(" ");
        return new Location(plotWorld, Double.parseDouble(strings[0]), Double.parseDouble(strings[1]),
                Double.parseDouble(strings[2]), Float.parseFloat(strings[3]), Float.parseFloat(strings[4]));
    }

    private String locationToString(Location l) {
        return l.getX() + " " + l.getY() + " " + l.getZ() + " " + l.getY() + " " + l.getPitch();
    }




}
