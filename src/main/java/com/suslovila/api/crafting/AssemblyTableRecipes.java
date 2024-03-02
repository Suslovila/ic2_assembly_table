package com.suslovila.api.crafting;

import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.HashMap;

public class AssemblyTableRecipes {
    private static final AssemblyTableRecipes INSTANCE = new AssemblyTableRecipes();
    public final HashMap<String, AssemblyTableRecipe> recipes = new HashMap<>();

    public static AssemblyTableRecipes instance() {
        return INSTANCE;
    }
    public AssemblyTableRecipe addRecipe(String id, double cost, ItemStack res, ItemStack... inputs){
        AssemblyTableRecipe recipe = new AssemblyTableRecipe(cost, res, inputs);
        if(recipes.containsKey(id)){
            System.out.println("recipe with id " + id + " is already registered!!!");
            return null;
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
            this.inputs = new ArrayList<ItemStack>(Arrays.asList(inputs));
            result = res;
            energyCost = cost;
        }

        private boolean isMatch(ItemStack a, ItemStack b) {
//            return a.isItemEqual(b)
            return ((a == null && b == null) || (a != null && b != null && a.getItem() == b.getItem() && (!a.getHasSubtypes() || a.getMetadata() == b.getMetadata())));
        }
    }
}
