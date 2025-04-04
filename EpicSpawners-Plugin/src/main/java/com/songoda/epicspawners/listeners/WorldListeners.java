package com.songoda.epicspawners.listeners;

import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.api.spawners.spawner.PlacedSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;

import java.util.ArrayList;
import java.util.List;

public class WorldListeners implements Listener {
    private final EpicSpawners plugin;

    public WorldListeners(EpicSpawners plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent e) {
        // Unload previous spawners belonging to this world.
        List<PlacedSpawner> remove = new ArrayList<>();
        for (PlacedSpawner ps : this.plugin.getSpawnerManager().getSpawners()) {
            if (ps.getWorld() == null || e.getWorld().getName().equals(ps.getWorld().getName())) {
                remove.add(ps);
            }
        }
        this.plugin.getSpawnerManager().removeSpawnersFromWorld(remove);
    }
}
