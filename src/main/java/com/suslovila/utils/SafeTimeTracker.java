
package com.suslovila.utils;

import net.minecraft.world.World;

public class SafeTimeTracker {

    private long lastMark = Long.MIN_VALUE;
    private long duration = -1;
    private long randomRange = 0;
    private long lastRandomDelay = 0;
    private long internalDelay = 1;

    public SafeTimeTracker(long delay) {
        internalDelay = delay;
    }


    public SafeTimeTracker(long delay, long random) {
        internalDelay = delay;
        randomRange = random;
    }

    public boolean markTimeIfDelay(World world) {
        return markTimeIfDelay(world, internalDelay);
    }

    /**
     * Return true if a given delay has passed since last time marked was called
     * successfully.
     */
    public boolean markTimeIfDelay(World world, long delay) {
        if (world == null) {
            return false;
        }

        long currentTime = world.getTotalWorldTime();

        if (currentTime < lastMark) {
            lastMark = currentTime;
            return false;
        } else if (lastMark + delay + lastRandomDelay <= currentTime) {
            duration = currentTime - lastMark;
            lastMark = currentTime;
            lastRandomDelay = (int) (Math.random() * randomRange);

            return true;
        } else {
            return false;
        }
    }

    public long durationOfLastDelay() {
        return duration > 0 ? duration : 0;
    }

    public void markTime(World world) {
        lastMark = world.getTotalWorldTime();
    }
}