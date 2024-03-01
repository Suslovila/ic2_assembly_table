package com.suslovila.common.tileEntity;

import com.suslovila.api.crafting.AssemblyTableRecipes;
import com.suslovila.common.inventory.container.SimpleInventory;
import com.suslovila.utils.nbt.INBTStoreable;
import com.suslovila.utils.nbt.NBTHelper;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

public class TileAssemblyTable extends TileSynchronised implements IInventory {
    SimpleInventory inventory = new SimpleInventory(getSizeInventory(), "inv", 64);
    private static final int inventorySize = 24;
    private static final int inputSlotsAmount = 12;
    public static final int patternAmount = 8;
    public double euBuffer = 0.0;
    public int bufferCapacity;
    public static final String PATTERNS_NBT = "patterns";
    public ArrayList<AssemblyTablePattern> patterns = new ArrayList<>();

    public TileAssemblyTable() {
        inventory.addListener(this);
    }

    public void updateEntity() {
        if (this.worldObj.getTotalWorldTime() % 10 == 0) {
            if (worldObj.isRemote) System.out.println("On client:    " + patterns);
            else System.out.println("On server:     " + patterns);

        }
        super.updateEntity();
        if (worldObj.isRemote) return;
        updatePatterns(new ArrayList<>());
        if(worldObj.getTotalWorldTime() % 100 == 0) observeAvailableCrafts();
        TESTING();
    }

    private void TESTING() {
        euBuffer += 1000;
    }

    //excludedRecipes will not be added while this check (used to walk throw available recipes)
    public void updatePatterns(Collection<String> excludedRecipes) {
        if (worldObj.isRemote) return;
        List<AssemblyTablePattern> copyPatterns = new ArrayList<>(patterns);
        for(int i = 0; i < copyPatterns.size(); i++){
            AssemblyTablePattern pattern = copyPatterns.get(i);
            boolean canStillCraft = this.canStillCraft(pattern.recipeId);
            if(!pattern.isActive && !canStillCraft){
                removePatternNoUpdate(i);
            }
        }

        AssemblyTableRecipes.instance().recipes.forEach((recipeId, recipe) -> {
            boolean tileAlreadyContainsRecipe = this.patterns.stream().anyMatch(pattern -> pattern.recipeId.equals(recipeId));
            if (this.canAddPattern() && !tileAlreadyContainsRecipe && !excludedRecipes.contains(recipeId) && canStillCraft(recipeId)) {
                addPattern(recipeId);
            }
        });
    }


    private void observeAvailableCrafts() {
        for (AssemblyTablePattern pattern : patterns) {
            if (pattern.isActive) {
                AssemblyTableRecipes.AssemblyTableRecipe recipe = AssemblyTableRecipes.instance().recipes.get(pattern.recipeId);
                if (canStillCraft(recipe) && euBuffer >= recipe.energyCost) {
                    craft(recipe);
                }
            }
        }
    }

    private void craft(AssemblyTableRecipes.AssemblyTableRecipe recipe) {
        recipe.inputs.forEach(itemStack -> {
            Integer index = inventory.getIndexOfSlotWithEnoughOf(itemStack);
            if (index == null) {
                System.out.println("error handling item slot reduce");
                return;
            }
            inventory.decrStackSize(index, itemStack.stackSize);
        });
        outPutCraftingResult(recipe);
        euBuffer -= recipe.energyCost;
    }

    private void outPutCraftingResult(AssemblyTableRecipes.AssemblyTableRecipe recipe) {
        ItemStack toOutput = recipe.result.copy();
        EntityItem entityitem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.7, zCoord + 0.5,
                toOutput);
        worldObj.spawnEntityInWorld(entityitem);
    }

    public boolean canStillCraft(String recipeId) {
        AssemblyTableRecipes.AssemblyTableRecipe recipe = AssemblyTableRecipes.instance().recipes.get(recipeId);
        return canStillCraft(recipe);
    }

    public boolean canStillCraft(AssemblyTableRecipes.AssemblyTableRecipe recipe) {
        if (recipe == null) return false;
        return recipe.inputs.stream().allMatch(stack -> this.inventory.hasEnough(stack));
    }

    public void writeCustomNBT(NBTTagCompound rootNbt) {
        inventory.writeToNBT(rootNbt);
        NBTTagList patternsNbtList = new NBTTagList();
        for (AssemblyTablePattern pattern : patterns) {
            NBTTagCompound patternNbt = new NBTTagCompound();
            pattern.writeToNBT(patternNbt);
            patternsNbtList.appendTag(patternNbt);
        }
        rootNbt.setTag(PATTERNS_NBT, patternsNbtList);
        System.out.println("Patterns written:    " + patternsNbtList);
    }

    public void readCustomNBT(NBTTagCompound rootNbt) {
        inventory.readFromNBT(rootNbt);
        if (rootNbt.hasKey(PATTERNS_NBT)) {
            //this.patterns.clear();
            ArrayList<AssemblyTablePattern> readPatterns = new ArrayList<>();
            NBTTagList patternsNbt = rootNbt.getTagList(PATTERNS_NBT, NBTHelper.TAG_COMPOUND);
            for (int i = 0; i < patternsNbt.tagCount(); i++) {
                NBTTagCompound patternNbt = patternsNbt.getCompoundTagAt(i);
                AssemblyTablePattern pattern = new AssemblyTablePattern(patternNbt);
                readPatterns.add(pattern);
            }
            this.patterns = readPatterns;
            System.out.println("Patterns read:    " + readPatterns);
        }
    }


    @Override
    public int getSizeInventory() {
        return inventorySize;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
//
//        if (currentRecipe == null) {
//            setNextCurrentRecipe();
//        }
    }

    @Override
    public String getInventoryName() {
        return ("tile.assemblyTableBlock.name");
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        return inventory.decrStackSize(slot, amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot) {
        return inventory.getStackInSlotOnClosing(slot);
    }


    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && !isInvalid();
    }

    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isCustomInventoryName() {
        return false;
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return inventory.isItemValidForSlot(slot, stack);
    }

    public int getInputSlotAmount() {
        return inputSlotsAmount;
    }

    public int getOutputSlotsAmount() {
        return getSizeInventory() - getInputSlotAmount();
    }


    public boolean canAddPattern() {
        return patterns.size() < patternAmount;
    }

    //removes pattern and updates patterns (removed pattern is forbidden while update in order to roll through patterns)
    public String removePatternWithUpdate(int index) {
        String removed = removePatternNoUpdate(index);
        updatePatterns(Collections.singletonList(removed));
        return removed;
    }

    public String removePatternNoUpdate(int index) {
        String removed = patterns.get(index).recipeId;
        patterns.remove(index);
        markSaveAndSync();
        return removed;
    }

    public AssemblyTablePattern getPatternById(int id) {
        boolean doesChosenPatternExists = patterns.size() > id;
        if (doesChosenPatternExists) {
            return patterns.get(id);
        }
        return null;
    }

    public void activatePattern(int index) {
        patterns.get(index).isActive = true;
        markSaveAndSync();
    }

    public void addPattern(String recipeId) {
        this.patterns.add(new AssemblyTablePattern(recipeId, false));
        this.markSaveAndSync();
    }


    @Override
    public boolean canUpdate() {
        return true;
    }


    public static final class AssemblyTablePattern implements INBTStoreable {
        private static final String RECIPE_NBT_NAME = "recipe";
        private static final String IS_ACTIVE_NBT_NAME = "isActive";

        public String recipeId;
        public boolean isActive;

        public AssemblyTablePattern(String recipe, boolean active) {
            this.recipeId = recipe;
            isActive = active;
        }

        public AssemblyTablePattern(NBTTagCompound nbt) {
            readFromNBT(nbt);
        }

        @Override
        public void readFromNBT(NBTTagCompound data) {
            if (data.hasKey(RECIPE_NBT_NAME)) {
                recipeId = data.getString(RECIPE_NBT_NAME);
            }
            if (data.hasKey(IS_ACTIVE_NBT_NAME)) {
                isActive = data.getBoolean(IS_ACTIVE_NBT_NAME);
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound data) {
            data.setString(RECIPE_NBT_NAME, recipeId);
            data.setBoolean(IS_ACTIVE_NBT_NAME, isActive);
        }

        @Override
        public String toString() {
            return String.valueOf(isActive);
        }
    }
}
