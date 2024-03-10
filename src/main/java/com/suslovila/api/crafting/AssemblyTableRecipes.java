package com.suslovila.api.crafting;

import com.suslovila.ExampleMod;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AssemblyTableRecipes {
    private static final AssemblyTableRecipes INSTANCE = new AssemblyTableRecipes();
    public final HashMap<String, AssemblyTableRecipe> recipes = new HashMap<>();

    public static AssemblyTableRecipes instance() {
        return INSTANCE;
    }

    public AssemblyTableRecipe addRecipe(String id, double energyCost, ItemStack res, ItemStack... inputs) throws Exception {
        AssemblyTableRecipe recipe = new AssemblyTableRecipe(energyCost, res, inputs);
        if (recipes.containsKey(id)) {
            throw new Exception(ExampleMod.MOD_ID + ": recipe with id " + id + " is already registered!!!");
        }
        recipes.put(id, recipe);
        return recipe;
    }

    private AssemblyTableRecipes() {
    }

    public static class AssemblyTableRecipe {
        public final ArrayList<ItemStack> inputs;
        public final ItemStack result;
        public final double energyCost;

        private AssemblyTableRecipe(double cost, ItemStack res, ItemStack... inputs) {
            this.inputs = new ArrayList<>(Arrays.asList(inputs));
            result = res;
            energyCost = cost;
        }
    }
}
