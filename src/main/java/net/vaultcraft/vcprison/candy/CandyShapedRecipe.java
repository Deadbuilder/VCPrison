package net.vaultcraft.vcprison.candy;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Nicholas Peterson
 */
public class CandyShapedRecipe implements CandyRecipe {
    private ItemStack result;
    private char[] parts = new char[0];
    private String[] shape = new String[0];
    private HashMap<Character, ItemStack> items = new HashMap<>();

    public CandyShapedRecipe(ItemStack result) {
        this.result = result;
    }

    public void shape(char... parts) {
        if(parts.length > 9)
            throw new IllegalArgumentException("Parts in a recipe cannot be longer than 9");
        this.parts = parts;

        String[] tempArray = new String[3];
        for(int i = 0; i < 3; i++) {
            String temp = "";
            for(int x = 0; x < 3; x++) {
                if ((x + (i * 3)) < parts.length)
                    temp += parts[x + (i * 3)];
                else
                    temp += ' ';
            }
            tempArray[i] = temp;
        }

        this.shape = tempArray;
    }

    public void setItem(char part, ItemStack item) {
        if(items.containsKey(part))
            items.remove(part);
        items.put(part, item);
    }

    public char[] getParts() {
        return parts;
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public Recipe getRecipe() {
        ShapedRecipe recipe = new ShapedRecipe(this.result);
        recipe.shape(this.shape);
        for(Map.Entry<Character, ItemStack> entry : items.entrySet())
            recipe.setIngredient(entry.getKey(), new MaterialData(entry.getValue().getType(), entry.getValue().getData().getData()));
        return recipe;
    }

    @Override
    public boolean isRecipe(ItemStack[] recipe) {
        List<ItemStack> r = new ArrayList<>();
        for(int i = 0; i < 9; i++) {
            if(i <= parts.length)
                if(items.get(parts[i]) != null)
                    r.set(i, items.get(parts[i]));
                else
                    r.set(i, new ItemStack(Material.AIR));
            else
                r.set(i, new ItemStack(Material.AIR));
        }

        for(int i = 0; i < recipe.length; i++) {
            if(!r.get(i).equals(recipe[i + 1]))
                return false;
        }

        return true;
    }
}
