package com.suslovila.common.tileEntity;

import com.suslovila.api.lasers.ILaserTarget;
import com.suslovila.api.crafting.AssemblyTableRecipes;
import com.suslovila.client.gui.GuiAssemblyTable;
import com.suslovila.common.inventory.container.SimpleInventory;
import com.suslovila.network.PacketHandler;
import com.suslovila.network.packet.PacketUpdateEnergy;
import com.suslovila.network.packet.PacketUpdatePatterns;
import com.suslovila.utils.InventoryUtils;
import com.suslovila.utils.SusVec3;
import com.suslovila.utils.collection.CollectionUtils;
import com.suslovila.utils.nbt.INBTStoreable;
import com.suslovila.utils.nbt.NBTHelper;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

public class TileAssemblyTable extends TileSynchronised implements ISidedInventory, ILaserTarget {
    //todo: сделать поля-маркеры на разные виды синхронизации, дабы не вызывать несколько раз одно и то же
    //todo: вызывать обновление нынешнего паттерна
    SimpleInventory inventory = new SimpleInventory(getSizeInventory(), getOutputSlotsAmount(), "inv", 64);
    private static final int inventorySize = 24;
    private static final int inputSlotsAmount = 12;
    public static final int patternAmount = 8;
    public double euBuffer = 0.0;
    public static final int euBufferCapacity = 1000;
    public static final int maxAvailableDistanceToPlayer = 64;
    public AssemblyTablePattern currentPattern = null;
    public List<AssemblyTablePattern> patterns = new ArrayList<>();
    private double energyClientDelta = 0;
    private final double energyClientPixels = GuiAssemblyTable.energyClientPixels;

    public TileAssemblyTable() {
        super();
        inventory.addListener(this);
    }

    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) {
            return;
        }

        TESTING();

        updateAvailablePatterns(new ArrayList<>());
        observeAvailableCrafts();
        updateCurrentPattern();
    }

    private void TESTING() {
//        euBuffer+=20;
//        forceSyncEnergy();
    }


    //excludedRecipes will not be added while this check (used to walk throw available recipes)
    public boolean updateAvailablePatterns(Collection<String> excludedRecipes) {
        if (worldObj.isRemote) return false;
        boolean changed = false;
        List<AssemblyTablePattern> copyPatterns = new ArrayList<>(patterns);
        for (int i = 0; i < copyPatterns.size(); i++) {
            AssemblyTablePattern pattern = copyPatterns.get(i);
            boolean canStillCraft = this.canStillCraft(pattern.recipeId);
            if (!pattern.isActive && !canStillCraft) {
                removePatternNoUpdate(pattern);
                changed = true;
            }
        }
        for (String recipeId : AssemblyTableRecipes.instance().recipes.keySet()) {
            boolean tileAlreadyContainsRecipe = this.patterns.stream().anyMatch(pattern -> pattern.recipeId.equals(recipeId));
            if (this.canAddMorePatterns() && !tileAlreadyContainsRecipe && !excludedRecipes.contains(recipeId) && canStillCraft(recipeId)) {
                addPattern(recipeId);
                changed = true;
            }
        }
        if (changed) {
            markForSaveAndForcePatterns();
        }
        return changed;
    }


    private void observeAvailableCrafts() {
        if (currentPattern == null) return;
        AssemblyTableRecipes.AssemblyTableRecipe recipe = AssemblyTableRecipes.instance().recipes.get(currentPattern.recipeId);
        if (!canStillCraft(recipe)) {
            setNextCurrentPattern();
            return;
        }
        if (euBuffer >= recipe.energyCost) {
            craft(recipe);
            setNextCurrentPattern();
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
        if (worldObj.isRemote) return;
        ItemStack toOutput = recipe.result.copy();
        List<ForgeDirection> offsets = Arrays.asList(Arrays.copyOf(ForgeDirection.VALID_DIRECTIONS, ForgeDirection.VALID_DIRECTIONS.length));
        Collections.shuffle(offsets);
        for (int i = 0; i < offsets.size(); i++) {
            ForgeDirection offset = offsets.get(i);
            TileEntity tile = worldObj.getTileEntity(xCoord + offset.offsetX, yCoord + offset.offsetY, zCoord + offset.offsetZ);
            if (tile instanceof IInventory) {
                int side = offset.getOpposite().ordinal();
                toOutput = InventoryUtils.placeItemStackIntoInventory(toOutput, (IInventory) tile, side, true, false, 0);
                //if empty
                if (toOutput == null) {
                    return;
                }
            }
        }
        //if something is left
        toOutput = InventoryUtils.placeItemStackIntoInventory(toOutput, this, 1, true, true, getOutputSlotsAmount());
        if (toOutput == null) {
            return;
        }

        EntityItem entityitem = new EntityItem(worldObj, xCoord + 0.5, yCoord + 0.7, zCoord + 0.5, toOutput);
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

    public boolean canAddMorePatterns() {
        return patterns.size() < patternAmount;
    }

    //removes pattern and updates patterns (removed pattern is forbidden while update in order to roll through patterns)
    public String removePatternWithUpdate(int index) {
        String removed = removePatternNoUpdate(index);
        //rolling through
        boolean changed = updateAvailablePatterns(Collections.singletonList(removed));
        //double check if nothing changed (getting removed recipe back to screen without blinking in gui)
        if (!changed) {
            changed = updateAvailablePatterns(new ArrayList<>());
            if (!changed) {
                markForSaveAndForcePatterns();
            }
        }
        return removed;
    }

    public String removePatternNoUpdate(AssemblyTablePattern patternToRemove) {
        patterns.remove(patternToRemove);
        if (patternToRemove.equals(currentPattern)) {
            currentPattern = null;
        }
        return patternToRemove.recipeId;
    }

    public String removePatternNoUpdate(int index) {
        String removed = patterns.get(index).recipeId;
        AssemblyTablePattern removedPattern = patterns.remove(index);
        if (removedPattern.equals(currentPattern)) {
            currentPattern = null;
        }
        //should not fire sync
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
        markForSaveAndForcePatterns();
    }

    public void addPattern(String recipeId) {
        this.patterns.add(new AssemblyTablePattern(recipeId, false));
    }


    public void updateCurrentPattern() {
        if (currentPattern == null) {
            for (AssemblyTablePattern pattern : patterns) {
                if (canStillCraft(pattern.recipeId) && pattern.isActive) {
                    currentPattern = pattern;
                    markForSaveAndForcePatterns();
                    return;
                }
            }
        }
    }

    //TRIES to set next pattern from active ones
    //returns true if pattern was changed
    //false - nothing was changed
    public boolean setNextCurrentPattern() {
        List<Integer> patternIndices;
        //in order to provide cyclic iteration, list is initialised in different ways
        if (currentPattern == null) {
            patternIndices = CollectionUtils.getNumbersInRange(0, patterns.size());
        } else {
            int currentPatternIndex = patterns.indexOf(currentPattern);
            patternIndices = CollectionUtils.getNumbersInRange(currentPatternIndex + 1, patterns.size());
            patternIndices.addAll(CollectionUtils.getNumbersInRange(0, currentPatternIndex));
        }
        for (int patternIndex : patternIndices) {
            AssemblyTablePattern iteratePattern = patterns.get(patternIndex);
            if (iteratePattern.isActive && canStillCraft(iteratePattern.recipeId)) {
                currentPattern = iteratePattern;
                markForSaveAndForcePatterns();
                return true;
            }
        }
        //if we are here, it means currentPattern wasn't switched. Maybe now we can't craft anything
        if (currentPattern != null) {
            if (!canStillCraft(currentPattern.recipeId)) {
                currentPattern = null;
                markForSaveAndForcePatterns();
                return true;
            }
        }
        return false;
    }

    //used to instantly update patterns on client to avoid annoying 1 tick delay (caused by tileEntity sync delay)
    public void forceSyncPatterns() {
        if (!worldObj.isRemote) {
            PacketHandler.INSTANCE.sendToAllAround(
                    new PacketUpdatePatterns(xCoord, yCoord, zCoord, patterns, currentPattern),
                    new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, maxAvailableDistanceToPlayer));
        }
    }

    public void forceSyncEnergy() {
        if (!worldObj.isRemote) {
            PacketHandler.INSTANCE.sendToAllAround(
                    new PacketUpdateEnergy(xCoord, yCoord, zCoord, euBuffer),
                    new NetworkRegistry.TargetPoint(worldObj.provider.dimensionId, xCoord, yCoord, zCoord, maxAvailableDistanceToPlayer));
        }
    }

    public void markForSaveAndForcePatterns() {
        markForSave();
        forceSyncPatterns();
    }

    public int getEnergyScaled() {
        return (int) (euBuffer / euBufferCapacity * energyClientPixels);
    }


    @Override
    public int getSizeInventory() {
        return inventorySize;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        inventory.setInventorySlotContents(slot, stack);
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
        return worldObj.getTileEntity(xCoord, yCoord, zCoord) == this && !isInvalid() &&
                (player.getDistanceSq(this.xCoord + 0.5D, this.yCoord + 0.5D, this.zCoord + 0.5D) <= maxAvailableDistanceToPlayer);
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


    @Override
    public int[] getSlotsForFace(int side) {
        return InventoryUtils.createSlotArray(0, inventory.getSizeInventory());
    }

    @Override
    public boolean canInsertItem(int slotIndex, ItemStack stack, int side) {
        return slotIndex < getSizeInventory() - getInputSlotAmount();
    }

    @Override
    public boolean canExtractItem(int slotIndex, ItemStack stack, int side) {
        return slotIndex >= getSizeInventory() - getInputSlotAmount();
    }


    @Override
    public boolean requiresLaserEnergy() {
        return currentPattern != null;
    }

    @Override
    public double receiveLaserEnergy(TileEntityLaser laser, double amount, double voltage) {
        double toAdd = Math.min(amount, TileAssemblyTable.euBufferCapacity - this.euBuffer);
        this.euBuffer += toAdd;
        energyClientDelta += toAdd;
        boolean isDifferenceMoreThanOnePixel = (energyClientDelta / euBufferCapacity) > (1 / energyClientPixels);
        if (isDifferenceMoreThanOnePixel) {
            forceSyncEnergy();
            energyClientDelta = 0;
        }
        markForSave();
        return amount - toAdd;
    }

    @Override
    public boolean isValidTarget() {
        return true;
    }

    @Override
    public SusVec3 getLaserStreamPos() {
        return new SusVec3(xCoord, yCoord, zCoord);
    }


    public static final String PATTERNS_NBT = "patterns";
    public static final String CURRENT_PATTERN_NBT = "currentPattern";
    public static final String ENERGY_NBT = "energyAmount";

    public void writeCustomNBT(NBTTagCompound rootNbt) {
        inventory.writeToNBT(rootNbt);
        NBTTagList patternsNbtList = new NBTTagList();
        for (AssemblyTablePattern pattern : patterns) {
            NBTTagCompound patternNbt = new NBTTagCompound();
            pattern.writeToNBT(patternNbt);
            patternsNbtList.appendTag(patternNbt);
        }
        rootNbt.setTag(PATTERNS_NBT, patternsNbtList);
        if (currentPattern != null) {
            NBTTagCompound currentPatternNbt = new NBTTagCompound();
            currentPattern.writeToNBT(currentPatternNbt);
            rootNbt.setTag(CURRENT_PATTERN_NBT, currentPatternNbt);
        }
        rootNbt.setDouble(ENERGY_NBT, euBuffer);
    }

    public void readCustomNBT(NBTTagCompound rootNbt) {
        inventory.readFromNBT(rootNbt);
        if (rootNbt.hasKey(PATTERNS_NBT)) {
            ArrayList<AssemblyTablePattern> readPatterns = new ArrayList<>();
            NBTTagList patternsNbt = rootNbt.getTagList(PATTERNS_NBT, NBTHelper.TAG_COMPOUND);
            for (int i = 0; i < patternsNbt.tagCount(); i++) {
                NBTTagCompound patternNbt = patternsNbt.getCompoundTagAt(i);
                AssemblyTablePattern pattern = new AssemblyTablePattern(patternNbt);
                readPatterns.add(pattern);
            }
            this.patterns = readPatterns;
            if (rootNbt.hasKey(CURRENT_PATTERN_NBT)) {
                currentPattern = new AssemblyTablePattern((NBTTagCompound) rootNbt.getTag(CURRENT_PATTERN_NBT));
            } else {
                currentPattern = null;
            }
            euBuffer = rootNbt.getDouble(ENERGY_NBT);
        }
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

        @Override
        public boolean equals(Object pattern) {
            if (!(pattern instanceof AssemblyTablePattern)) return false;
            return Objects.equals(this.recipeId, ((AssemblyTablePattern) pattern).recipeId) &&
                    this.isActive == ((AssemblyTablePattern) pattern).isActive;
        }

    }

    public boolean hasEnough(ItemStack requiredItemStack) {
        return inventory.hasEnough(requiredItemStack);
    }
}
