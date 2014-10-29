package net.vaultcraft.vcprison.cells;


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.VCUtils;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CellManager {

    private World plotWorld;
    private volatile List<Cell> cells = new ArrayList<>();

    public CellManager() {
        WorldCreator wc = new WorldCreator("Cells");
        wc.generator(new CellGen());
        this.plotWorld = wc.createWorld();
        loadCells();
    }

    /**
     * Loads cells from the DB.
     */
    public void loadCells() {
        List<DBObject> allTheDBObjects = VCUtils.getInstance().getMongoDB().getAll(VCUtils.mongoDBName, "Cells");
        for(DBObject o : allTheDBObjects) {
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
            cells.add(cell);
        }
        Logger.log(VCPrison.getInstance(), cells.size() + " cells loaded from the DB.");
    }


    /**
     * Returns a list of all cells that a player owns. If a player does not own any cells, it'll be an empty list.
     * @param player Player to get cells for
     * @return List of cells said player owns, or an empty list if they don't own any.
     */
    public List<Cell> getCellsFromPlayer(Player player) {
        ArrayList<Cell> playerCells = new ArrayList<>();
        for(Cell c : cells) {
            if(c.ownerUUID == player.getUniqueId()) {
                playerCells.add(c);
            }
        }

        return playerCells;
    }

    /**
     * Returns the cell object for a location, returns null if there is no cell at that location yet.
     * @param location Location to get a cell for
     * @return The cell.
     */
    public Cell getCellFromLocation(Location location) {
        if(location.getWorld() != this.plotWorld) {
            throw new IllegalArgumentException("World must be PlotWorld!");
        }

        if(location.getChunk().getX() % 2 != 0 || location.getY() >= 91) {
            return null;
        }


        for(Cell c : cells) {
            if(c.chunkX == location.getChunk().getX() && c.chunkZ == location.getChunk().getZ()) {
                return c;
            }
        }


        return null;
    }

    public Chunk getNextOpenCell() {
        int row = 0;
        while (Math.abs(row) > 1000000) {
            for(int cell = -25; cell < 25; cell++) {
                Cell checkCell = getCellFromLocation(new Location(this.plotWorld, 16 * row, 88, 16 * cell));
                if(checkCell == null) {
                    return new Location(this.plotWorld, 16*row, 88, 16*cell).getChunk();
                }
            }

            if(row == 0) {
                row+= 2;
            } else if(row > 0) {
                row = -row;
            } else {
                row-= 2;
                row = -row;
            }
        }
        return null;
    }

    /**
     * Adds a cell to the RAM collection.
     * @param cell cell to add
     */
    public void addCell(Cell cell) {
        Cell possibleCell = getCellFromLocation(cell.cellSpawn);
        if(possibleCell == null) {
            cells.add(cell);
        } else {
            cells.remove(possibleCell);
            cells.add(cell);
        }
    }

    /**
     * Update cells in DB.
     */
    public void saveCells() {
        for(Cell c : cells) {
            addOrUpdateCellInDB(c);
        }
    }

    private void addOrUpdateCellInDB(Cell theCell) {
        String locString = theCell.chunkX+","+theCell.chunkZ;
        DBObject dbCell = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Cells", "Chunk", locString);
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
            dbCell.put("OwnerUUID", theCell.ownerUUID.toString());
            dbCell.put("Chunk", theCell.cellSpawn.getChunk().getX() + "," + theCell.cellSpawn.getChunk().getZ());
            StringBuilder sb = new StringBuilder();
            for(UUID u : theCell.additionalUUIDs) {
                sb.append(u.toString()).append(",");
            }
            dbCell.put("Members", sb.toString());
            dbCell.put("Name", theCell.name);
            dbCell.put("SpawnPoint", locationToString(theCell.cellSpawn));
            DBObject o1 = VCUtils.getInstance().getMongoDB().query(VCUtils.mongoDBName, "Cells", "Chunk", theCell.cellSpawn.getChunk().getX() + "," + theCell.cellSpawn.getChunk().getZ());
            VCUtils.getInstance().getMongoDB().update(VCUtils.mongoDBName, "Cells", o1, dbCell);
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
