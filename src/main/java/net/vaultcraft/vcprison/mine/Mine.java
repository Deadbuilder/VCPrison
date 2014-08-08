package net.vaultcraft.vcprison.mine;

import net.vaultcraft.vcprison.utils.Rank;
import net.vaultcraft.vcutils.protection.Area;

/**
 * Created by Connor on 8/1/14. Designed for the VCPrison project.
 */

public class Mine {

    private Area area;
    private Rank rank;
    private int totalCount = -1;
    private int blocksBroken;

    public Mine(Rank rank, Area area) {
        this.rank = rank;
        this.area = area;
    }

    public Area getArea() {
        return area;
    }

    public Rank getRank() {
        return rank;
    }

    public void setInitialBlocks(int blocks) {
        this.totalCount = blocks;
    }

    public void tickBlocks() {
        blocksBroken++;
    }

    public void reset() {
        blocksBroken = 0;
    }

    public double getPercent() {
        return (double)blocksBroken/(double)totalCount;
    }
}
