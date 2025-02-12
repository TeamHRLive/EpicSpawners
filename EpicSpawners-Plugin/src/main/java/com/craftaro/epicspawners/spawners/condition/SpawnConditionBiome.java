package com.craftaro.epicspawners.spawners.condition;

import com.craftaro.third_party.com.cryptomorin.xseries.XBiome;
import com.craftaro.third_party.org.apache.commons.text.WordUtils;
import com.craftaro.epicspawners.EpicSpawners;
import com.craftaro.epicspawners.api.spawners.condition.SpawnCondition;
import com.craftaro.epicspawners.api.spawners.spawner.PlacedSpawner;
import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class SpawnConditionBiome implements SpawnCondition {
    private final Set<XBiome> biomes;

    public SpawnConditionBiome(XBiome... biomes) {
        this.biomes = Arrays.stream(biomes).collect(Collectors.toSet());
    }

    public SpawnConditionBiome(Set<XBiome> biomes) {
        this.biomes = biomes;
    }

    @Override
    public String getName() {
        return "biome";
    }

    @Override
    public String getDescription() {
        return (this.biomes.size() == 1)
                ? EpicSpawners.getInstance().getLocale().getMessage("interface.spawner.conditionBiome1")
                .processPlaceholder("biome", getFriendlyBiomeName()).toString()
                : EpicSpawners.getInstance().getLocale().getMessage("interface.spawner.conditionBiome2")
                .toString();
    }

    @Override
    public boolean isMet(PlacedSpawner spawner) {
        String biomeName = spawner.getLocation().getBlock().getBiome().name();
        XBiome resolvedBiome = getBiomeFromName(biomeName);
        return resolvedBiome != null && this.biomes.contains(resolvedBiome);
    }

    private XBiome getBiomeFromName(String biomeName) {
        for (XBiome xBiome : XBiome.values()) {
            if (xBiome.name().equalsIgnoreCase(biomeName)) {
                return xBiome;
            }
        }
        return null;
    }

    private String getFriendlyBiomeName() {
        return WordUtils.capitalizeFully(Iterables.get(this.biomes, 0).name().replace("_", " "));
    }

    public Set<XBiome> getBiomes() {
        return this.biomes;
    }
}
