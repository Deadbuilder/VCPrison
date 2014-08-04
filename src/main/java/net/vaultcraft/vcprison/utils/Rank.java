package net.vaultcraft.vcprison.utils;

import org.bukkit.ChatColor;
import org.bukkit.Color;

/**
 * Created by Connor on 7/30/14. Designed for the VCPrison project.
 */
public enum Rank {
    A("&7&l<&fA&7&l>", 0.0),
    B("&7&l<&7B&7&l>", 400000.0),
    C("&7&l<&8C&7&l>", 600000.0),
    D("&7&l<&dD&7&l>", 900000.0),
    E("&7&l<&eE&7&l>", 1500000.0),
    F("&7&l<&bF&7&l>", 2500000.0),
    G("&7&l<&9G&7&l>", 4000000.0),
    H("&7&l<&3H&7&l>", 6700000.0),
    I("&7&l<&1I&7&l>", 11000000.0),
    J("&7&l<&aJ&7&l>", 20000000.0),
    K("&7&l<&2K&7&l>", 35000000.0),
    L("&7&l<&6L&7&l>", 65000000.0),
    M("&7&l<&cM&7&l>", 120000000.0),
    N("&7&l<&4N&7&l>", 250000000.0),
    O("&7&l<&0O&7&l>", 500000000.0),
    P("&7&l<&5P&7&l>", 1000000000.0),
    Q("&f&l<&6Q&f&l>", 2000000000.0),
    R("&f&l<&aR&f&l>", 4000000000.0),
    S("&f&l<&dS&f&l>", 10000000000.0),
    T("&f&l<&cT&f&l>", 20000000000.0),
    U("&f&l<&4U&f&l>", 50000000000.0),
    V("&f&l<&9V&f&l>", 120000000000.0),
    W("&f&l<&bW&f&l>", 300000000000.0),
    X("&f&l<&8X&f&l>", 500000000000.0),
    Y("&6&l<&eY&6&l>", 800000000000.0),
    Z("&8&l<&5Z&8&l>", 1000000000000.0), //now I know my ABC's next time won't you sing with me
    FREE("&5&l<&7&lFREE&5&l>", 2000000000000.0);

    private String prefix;
    private double cost;

    private Rank(String prefix, double cost) {
        this.prefix = prefix;
        this.cost = cost;
    }

    public String getPrefix() {
        return prefix;
    }

    public double getCost() {
        return cost;
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

    public static Rank fromName(String name) {
        for (Rank rank : values()) {
            if (rank.toString().equals(name.toUpperCase()))
                return rank;
        }
        return A;
    }
}
