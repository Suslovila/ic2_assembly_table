package com.suslovila.common.tileEntity;

public class TileLaser extends TileSynchronised {
    public void updateEntity() {
        super.updateEntity();
        if (worldObj.isRemote) return;

    }
}
