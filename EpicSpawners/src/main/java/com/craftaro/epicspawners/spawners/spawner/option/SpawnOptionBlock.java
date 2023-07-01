package com.craftaro.epicspawners.spawners.spawner.option;

import com.craftaro.core.compatibility.CompatibleMaterial;
import com.craftaro.epicspawners.EpicSpawners;
import com.craftaro.epicspawners.api.boosts.types.Boosted;
import com.craftaro.epicspawners.api.spawners.spawner.PlacedSpawner;
import com.craftaro.epicspawners.api.spawners.spawner.SpawnerStack;
import com.craftaro.epicspawners.api.spawners.spawner.SpawnerTier;
import com.craftaro.epicspawners.api.spawners.spawner.option.SpawnOption;
import com.craftaro.epicspawners.api.spawners.spawner.option.SpawnOptionType;
import com.craftaro.epicspawners.boost.types.BoostedImpl;
import com.craftaro.epicspawners.spawners.spawner.PlacedSpawnerImpl;
import com.craftaro.epicspawners.spawners.spawner.SpawnerStackImpl;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class SpawnOptionBlock implements SpawnOption {

    private static final int MAX_SEARCH_COUNT = 250;
    private static final int SPAWN_RADIUS = 3;

    private final Random random;
    private final CompatibleMaterial[] blocks;

    public SpawnOptionBlock(CompatibleMaterial... blocks) {
        this.blocks = blocks;
        this.random = new Random();
    }

    public SpawnOptionBlock(Collection<CompatibleMaterial> blocks) {
        this(blocks.toArray(new CompatibleMaterial[blocks.size()]));
    }

    @Override
    public void spawn(SpawnerTier data, SpawnerStack stack, PlacedSpawner spawner) {
        Location location = spawner.getLocation();
        if (location == null || location.getWorld() == null) return;

        int spawnerBoost = spawner.getBoosts().stream().mapToInt(Boosted::getAmountBoosted).sum();
        for (int i = 0; i < stack.getStackSize() + spawnerBoost; i++) {
            for (CompatibleMaterial material : blocks) {
                int searchIndex = 0;
                while (searchIndex++ <= MAX_SEARCH_COUNT) {
                    spawner.setSpawnCount(spawner.getSpawnCount() + 1);
                    EpicSpawners.getInstance().getDataManager().save(spawner);
                    double xOffset = random.nextInt((SPAWN_RADIUS * 2) + 1) - SPAWN_RADIUS;
                    double yOffset = random.nextInt((SPAWN_RADIUS * 2) + 1) - SPAWN_RADIUS;
                    double zOffset = random.nextInt((SPAWN_RADIUS * 2) + 1) - SPAWN_RADIUS;

                    // Get block at offset
                    location.add(xOffset, yOffset, zOffset);
                    Block spawnBlock = location.getBlock();
                    location.subtract(xOffset, yOffset, zOffset);

                    // If block isn't air, try for another block
                    if (spawnBlock.getType() != Material.AIR) continue;

                    // Set Type and data for valid air block
                    material.applyToBlock(spawnBlock);
                    break;
                }
            }
        }
    }

    @Override
    public SpawnOptionType getType() {
        return SpawnOptionType.BLOCK;
    }

    @Override
    public int hashCode() {
        return 31 * (blocks != null ? blocks.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof SpawnOptionBlock)) return false;

        SpawnOptionBlock other = (SpawnOptionBlock) object;
        return Arrays.equals(blocks, other.blocks);
    }

}