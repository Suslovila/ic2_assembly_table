package com.suslovila.utils;

import com.suslovila.api.crafting.AssemblyTableRecipes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public final class ItemStackHelper {
    public static boolean areItemStacksEqual(ItemStack first, ItemStack second) {
        return ((first == null && second == null) || (first != null && second != null && first.getItem() == second.getItem() && (!first.getHasSubtypes() || first.getMetadata() == second.getMetadata())));
    }
}

