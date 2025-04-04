package com.songoda.epicspawners.api.spawners.spawner;

import com.songoda.core.database.Data;
import com.songoda.epicspawners.api.utils.CostType;
import org.bukkit.entity.Player;

public interface SpawnerStack extends Data {
    PlacedSpawner getSpawner();

    int getStackSize();

    void setStackSize(int stackSize);

    SpawnerTier getCurrentTier();

    SpawnerData getSpawnerData();

    SpawnerStack setTier(SpawnerTier tier);

    void upgrade(Player player, CostType type);

    void convert(SpawnerData data, Player player, boolean forced);
}
