package com.suslovila.utils.nbt;

import net.minecraft.nbt.NBTTagCompound;

//class just for cleaner code
public interface INBTStoreable {
	void readFromNBT(NBTTagCompound tag);
	void writeToNBT(NBTTagCompound tag);
}
