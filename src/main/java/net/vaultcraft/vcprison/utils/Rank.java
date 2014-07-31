package net.vaultcraft.vcprison.utils;

import org.bukkit.ChatColor;

/**
 * Created by Connor on 7/30/14. Designed for the VCPrison project.
 */
public enum Rank {
    A("&f<A>", 0.0),
    B("&f<&7B&f>", 400000.0),
    C("&f<&8C&f>", 600000.0),
    D("&f<&dD&f>", 900000.0),
    E("&f<&eE&f>", 1500000.0),
    F("&f<&bF&f>", 2500000.0),
    G("&f<&9G&f>", 4000000.0),
    H("&f<&3H&f>", 6700000.0),
    I("&f<&1I&f>", 11000000.0),
    J("&f<&aJ&f>", 20000000.0),
    K("&f<&2K&f>", 35000000.0),
    L("&f<&6L&f>", 65000000.0),
    M("&f<&cM&f>", 120000000.0),
    N("&f<&4N&f>", 250000000.0),
    O("&f<&0O&f>", 500000000.0),
    P("&f<&5P&f>", 1000000000.0),
    Q("&7&l<&6Ⓠ&7&l>", 2000000000.0),
    R("&7&l<&aⓇ&7&l>", 4000000000.0),
    S("&7&l<&dⓈ&7&l>", 10000000000.0),
    T("&7&l<&cⓉ&7&l>", 20000000000.0),
    U("&7&l<&4Ⓤ&7&l>", 50000000000.0),
    V("&7&l<&9Ⓥ&7&l>", 120000000000.0),
    W("&7&l<&bⓌ&7&l>", 300000000000.0),
    X("&7&l<&8Ⓧ&7&l>", 500000000000.0),
    Y("&6&l<&eⓎ&6&l>", 800000000000.0),
    Z("&8&l<&5Ⓩ&8&l>", 1000000000000.0), //now I know my ABC's next time won't you sing with me
    FREE("&5&l<&7&nⒻⓇⒺⒺ&5&l>", 2000000000000.0);

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
}
