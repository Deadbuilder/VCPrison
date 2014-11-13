package net.vaultcraft.vcprison.candy;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Nicholas Peterson
 */
public class CandyShapelessRecipe implements CandyRecipe {

    private ItemStack result;
    private HashMap<ItemStack, Integer> parts = new HashMap<>();

    public CandyShapelessRecipe(ItemStack result) {
        this.result = result;
    }

    public void addIngredient(int amount, ItemStack itemStack) {
        if (amount > 9)
            throw new IllegalArgumentException("The amount can not be greater than 9");

        parts.put(itemStack, amount);
    }

    @Override
    public ItemStack getResult() {
        return result;
    }

    @Override
    public Recipe getRecipe() {
        ShapelessRecipe recipe = new ShapelessRecipe(this.getResult());

        for (Map.Entry<ItemStack, Integer> entry : this.parts.entrySet())
            recipe.addIngredient(entry.getValue(), new MaterialData(entry.getKey().getType(), entry.getKey().getData().getData()));
        return recipe;
    }

    @Override
    public boolean isRecipe(ItemStack[] recipe) {
        HashMap<ItemStack, Integer> clone = (HashMap<ItemStack, Integer>) parts.clone();
        for(ItemStack i : recipe) {
            if(clone.get(i) == null)
                return false;

            int temp = clone.get(i);
            clone.remove(i);
            clone.put(i, --temp);
        }

        for(int i : clone.values())
            if(i != 0)
                return false;

        return true;
    }


}
