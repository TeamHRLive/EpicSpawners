package com.songoda.epicspawners.api.spawners.spawner.option;

import com.songoda.epicspawners.api.spawners.spawner.PlacedSpawner;
import com.songoda.epicspawners.api.spawners.spawner.SpawnerStack;
import com.songoda.epicspawners.api.spawners.spawner.SpawnerTier;

public interface SpawnOption {
    void spawn(SpawnerTier data, SpawnerStack stack, PlacedSpawner spawner);

    SpawnOptionType getType();
}
