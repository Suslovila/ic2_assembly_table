package com.suslovila.api.crafting;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class RecipeRegistry {
    public static final RecipeRegistry instance = new RecipeRegistry();

    private RecipeRegistry() {
    }

    public void preInit() {
        AssemblyTableRecipes.instance().addRecipe("redstone_block", 500, new ItemStack(Items.diamond, 1), new ItemStack(Items.redstone, 1));
        AssemblyTableRecipes.instance().addRecipe("emerald", 500, new ItemStack(Items.emerald, 1), new ItemStack(Items.nether_star, 1));
        AssemblyTableRecipes.instance().addRecipe("netherStar", 500, new ItemStack(Items.blaze_rod, 1), new ItemStack(Items.carrot, 1));
        AssemblyTableRecipes.instance().addRecipe("trhrth", 500, new ItemStack(Items.ender_eye, 1), new ItemStack(Items.ender_pearl, 1));
        AssemblyTableRecipes.instance().addRecipe("hrthrhdfgd", 500, new ItemStack(Items.iron_ingot, 1), new ItemStack(Items.gold_ingot, 1));
        AssemblyTableRecipes.instance().addRecipe("rthrthdfgdfv", 500, new ItemStack(Items.beef, 1), new ItemStack(Items.emerald, 1));
        AssemblyTableRecipes.instance().addRecipe("ilio;op;", 500, new ItemStack(Items.chicken, 1), new ItemStack(Items.arrow, 1));
        AssemblyTableRecipes.instance().addRecipe("tgecdfgdgrg", 500, new ItemStack(Items.gunpowder, 1), new ItemStack(Items.blaze_powder, 1));
        AssemblyTableRecipes.instance().addRecipe("trhrsdwqdsqwdsq", 500, new ItemStack(Items.porkchop, 1), new ItemStack(Items.sugar, 1));

    }
}
