package net.vaultcraft.vcprison.plots;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.data.DataException;
import net.vaultcraft.vcprison.VCPrison;
import net.vaultcraft.vcutils.logging.Logger;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by tacticalsk8er on 8/13/2014.
 */
public class PlotGen extends ChunkGenerator{

    CuboidClipboard cells;
    CuboidClipboard hallway;

    public PlotGen() {
        try {
            cells = CuboidClipboard.loadSchematic(new File(VCPrison.getInstance().getDataFolder(), "cells.schematic"));
            hallway = CuboidClipboard.loadSchematic(new File(VCPrison.getInstance().getDataFolder(), "hallway.schematic"));
        } catch (DataException | IOException e) {
            Logger.error(VCPrison.getInstance(), e);
        }
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    @Override
    public byte[] generate(World world, Random rand, int chunkX, int chunkY) {
        byte[] result = new byte[32768];
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 128; y++)  {
                for(int z = 0; z < 16; z++) {
                    if(chunkX % 2 == 0) {
                        result[xyzToByte(x, y, z)] = (byte) cells.getBlock(new Vector(x, y, z)).getType();
                        if(cells.getBlock(new Vector(x, y, z)).getType() != 0)
                            Logger.debug(VCPrison.getInstance(), cells.getBlock(new Vector(x, y, z)).getType() + " | x: " + x + " y: " + y + " z: " + z);
                    } else {
                        result[xyzToByte(x, y, z)] = (byte) hallway.getBlock(new Vector(x, y, z)).getType();
                        if(hallway.getBlock(new Vector(x, y, z)).getType() != 0)
                            Logger.debug(VCPrison.getInstance(), hallway.getBlock(new Vector(x, y, z)).getType() + " | x: " + x + " y: " + y + " z: " + z);
                    }
                }
            }
        }
        return result;
    }

    public int xyzToByte(int x, int y, int z) {
        return (x * 16 + z) * 128 + y;
    }
}
