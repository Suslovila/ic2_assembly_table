/**
 * Copyright (c) 2011-2017, SpaceToad and the BuildCraft Team
 * http://www.mod-buildcraft.com
 * <p>
 * The BuildCraft API is distributed under the terms of the MIT License.
 * Please check the contents of the license, which should be located
 * as "LICENSE.API" in the BuildCraft source code distribution.
 */
package com.suslovila.api;

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
