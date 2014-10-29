package net.vaultcraft.vcprison.cells;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.UUID;

public class Cell {

    public UUID ownerUUID;
    public int chunkX;
    public int chunkZ;
    public Location cellSpawn;
    public ArrayList<UUID> additionalUUIDs = new ArrayList<>();
    public String name;

    public Cell() {
    }

}
