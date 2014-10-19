package net.vaultcraft.vcprison.utils;

import org.bukkit.Material;

/**
 * Created by Connor on 7/30/14. Designed for the VCPrison project.
 */
public enum Rank {
    A("&7&l<&fA&7&l>", 0.0, Material.DIRT),
    B("&7&l<&7B&7&l>", 300000.0, Material.COBBLESTONE),
    C("&7&l<&8C&7&l>", 540000.0, Material.STONE),
    D("&7&l<&dD&7&l>", 972000.0, Material.WOOD),
    E("&7&l<&eE&7&l>", 1749600.0, Material.SAND),
    F("&7&l<&bF&7&l>", 3149280.0, Material.LOG),
    G("&7&l<&9G&7&l>", 5668704.0, Material.COAL_ORE),
    H("&7&l<&3H&7&l>", 10203667.0, Material.CLAY),
    I("&7&l<&1I&7&l>", 18366600.0, Material.SANDSTONE),
    J("&7&l<&aJ&7&l>", 33059880.0, Material.LAPIS_ORE),
    K("&7&l<&2K&7&l>", 59507784.0, Material.BRICK),
    L("&7&l<&6L&7&l>", 107114000.0, Material.IRON_ORE),
    M("&7&l<&cM&7&l>", 192805200.0, Material.SOUL_SAND),
    N("&7&l<&4N&7&l>", 347049400.0, Material.NETHERRACK),
    O("&7&l<&0O&7&l>", 624688900.0, Material.GOLD_ORE),
    P("&7&l<&5P&7&l>", 1124440000.0, Material.GLOWSTONE),
    Q("&f&l<&6Q&f&l>", 2023992000.0, Material.COAL_BLOCK),
    R("&f&l<&aR&f&l>", 3643186000.0, Material.NETHER_BRICK),
    S("&f&l<&dS&f&l>", 6557734000.0, Material.LAPIS_BLOCK),
    T("&f&l<&cT&f&l>", 11803920000.0, Material.QUARTZ_BLOCK),
    U("&f&l<&4U&f&l>", 21247060000.0, Material.IRON_BLOCK),
    V("&f&l<&9V&f&l>", 38244710000.0, Material.GOLD_BLOCK),
    W("&f&l<&bW&f&l>", 68840470000.0, Material.DIAMOND_BLOCK),
    X("&f&l<&8X&f&l>", 123912800000.0, Material.EMERALD_BLOCK),
    Y("&6&l<&eY&6&l>", 223043100000.0, Material.OBSIDIAN),
    Z("&8&l<&5Z&8&l>", 401477600000.0, Material.BEDROCK),
    FREE("&5&l<&7&lFREE&5&l>", 722659700000.0, Material.BEACON);

    private String prefix;
    private double cost;
    private Material mascot;

    private Rank(String prefix, double cost, Material mascot) {
        this.prefix = prefix;
        this.cost = cost;
        this.mascot = mascot;
    }

    public String getPrefix() {
        return prefix;
    }

    public double getCost() {
        return cost;
    }

    public Material getMascot() {
        return mascot;
    }

    public boolean higherThan(Rank other) {
        return other.getCost() <= this.getCost();
    }

    public static Rank next(Rank previous) {
        for (Rank r : values()) {
            if (r.getCost() > previous.getCost())
                return r;
        }

        return previous;
    }

    public static Rank last(Rank current) {
        Rank remember = Rank.A;
        for (Rank r : values()) {
            if (r == current)
                return remember;
            remember = r;
        }

        return remember;
    }

    public static Rank fromName(String name) {
        for (Rank rank : values()) {
            if (rank.toString().equals(name.toUpperCase()))
                return rank;
        }
        return A;
    }
}
