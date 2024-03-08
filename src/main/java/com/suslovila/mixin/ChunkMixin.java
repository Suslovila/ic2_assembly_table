package com.suslovila.mixin;

//import com.suslovila.mixinUtils.IMixinNbtTagProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = Chunk.class)
public class ChunkMixin {
    @Shadow public World worldObj;

    @Shadow @Final public int xPosition;

    @Shadow @Final public int zPosition;

    @Inject(method = "addTileEntity", at = @At(value = "HEAD"))
    public void addTileEntity(TileEntity tile, CallbackInfo ci){
        System.out.println("Now adding: " + tile + "    cords are: " + tile.xCoord + " " + tile.yCoord + " " + tile.zCoord + "   the block is: " + worldObj.getBlock(xPosition, tile.yCoord,zPosition));
    }
}
