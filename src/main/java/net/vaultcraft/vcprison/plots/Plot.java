package net.vaultcraft.vcprison.plots;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tacticalsk8er on 8/31/2014.
 */
public class Plot {

    private String ownerUUID = "";
    private List<String> canBuildUUIDs = new ArrayList<>();
    private Location plotSpawn;
    private CuboidSelection plotArea;
    private int chunkX;
    private int chunkZ;

    public Plot(CuboidSelection plotArea, int chunkX, int chunkZ) {
        this.plotArea = plotArea;
        this.plotSpawn = plotArea.getMinimumPoint();
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    public void setOwnerUUID(String ownerUUID) {
        this.ownerUUID = ownerUUID;
    }

    public void addBuildUUID(String uuid) {
        canBuildUUIDs.add(uuid);
    }

    public void removeBuildUUID(String uuid) {
        canBuildUUIDs.remove(uuid);
    }

    public boolean hasOwner() {
        return !ownerUUID.isEmpty();
    }

    public boolean canBuild(Player player) {
        String uuid = player.getUniqueId().toString();
        return uuid.equals(ownerUUID) || canBuildUUIDs.contains(uuid);
    }

    public String getOwnerUUID() {
        return ownerUUID;
    }

    public Location getPlotSpawn() {
        return plotSpawn;
    }

    public CuboidSelection getPlotArea() {
        return plotArea;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }
}
