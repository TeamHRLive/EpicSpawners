package com.songoda.epicspawners.boost.types;

import com.songoda.core.database.Data;
import com.songoda.epicspawners.api.boosts.types.Boosted;


public abstract class BoostedImpl implements Boosted, Data {
    private final int amountBoosted;
    private final long endTime;

    //Default constructor for database use
    public BoostedImpl() {
        this.amountBoosted = 0;
        this.endTime = 0;
    }

    public BoostedImpl(int amountBoosted, long endTime) {
        this.amountBoosted = amountBoosted;
        this.endTime = endTime;
    }

    @Override
    public int getAmountBoosted() {
        return amountBoosted;
    }

    @Override
    public long getEndTime() {
        return endTime;
    }
}
