
package com.suslovila.api.lasers;

import com.suslovila.common.tileEntity.TileEntityLaser;
import com.suslovila.utils.SusVec3;

/**
 * This interface should be defined by any Tile which wants
 * to receive energy from lasers.
 * <p>
 * The respective Block MUST implement ILaserTargetBlock!
 */
public interface ILaserTarget {


    boolean requiresLaserEnergy();

    /**
     * Transfers energy from the laser to the target.
     *
     * @param amount
     */
    double receiveLaserEnergy(TileEntityLaser laser, double amount, double voltage);


    boolean isValidTarget();

    /**
     * Get the  coordinates of the laser destination
     *
     * @return
     */
    SusVec3 getLaserStreamPos();
}
