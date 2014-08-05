package net.vaultcraft.vcprison.pickaxe;

import org.bukkit.Material;

/**
 * Created by tacticalsk8er on 8/4/2014.
 */
public enum BlockExp {

    STONE(1, Material.STONE, Material.COBBLESTONE),
    COAL(2, Material.COAL, Material.COAL_ORE, Material.COAL_BLOCK),
    IRON(3, Material.IRON_ORE, Material.IRON_INGOT, Material.IRON_BLOCK),
    GOLD(4, Material.GOLD_ORE, Material.GOLD_INGOT, Material.GOLD_BLOCK),
    LAPIS(4, Material.LAPIS_BLOCK, Material.LAPIS_ORE, Material.INK_SACK),
    REDSTONE(5, Material.REDSTONE_ORE, Material.REDSTONE, Material.REDSTONE_BLOCK),
    DIAMOND(6, Material.DIAMOND, Material.DIAMOND_ORE, Material.DIAMOND_BLOCK),
    EMERALD(7, Material.EMERALD, Material.EMERALD_ORE, Material.EMERALD_BLOCK);


    private Material[] materials;
    private int exp;

    private BlockExp(int exp, Material... materials) {
        this.materials = materials;
        this.exp = exp;
    }

    public Material[] getMaterials() {
        return materials;
    }

    public int getExp() {
        return exp;
    }

    public static BlockExp fromMaterial(Material material) {
        for(BlockExp blockExp : values()) {
            for(Material material1 : blockExp.getMaterials()) {
                if(material1 == material)
                    return blockExp;
            }
        }
        return STONE;
    }
}
