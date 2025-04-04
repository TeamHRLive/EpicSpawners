package com.songoda.epicspawners.listeners;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicspawners.api.EpicSpawnersApi;
import com.songoda.epicspawners.settings.Settings;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public class SpawnerListeners implements Listener {
    @EventHandler
    public void onSpawn(SpawnerSpawnEvent event) {

        //Respect vanilla spawners
        if (!EpicSpawnersApi.getSpawnerManager().isSpawner(event.getSpawner().getLocation())) {
            return;
        }

        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.FIREWORK) {
            return;
        }
        if (entity.getVehicle() != null) {
            entity.getVehicle().remove();
            entity.remove();
        }

        if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_11)) {
            if (!entity.getPassengers().isEmpty()) {
                for (Entity e : entity.getPassengers()) {
                    e.remove();
                }
                entity.remove();
            }
        }
        entity.remove();
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onTarget(EntityTargetLivingEntityEvent event) {
        if (!Settings.HOSTILE_MOBS_ATTACK_SECOND.getBoolean()) {
            return;
        }
        if (event.getEntity().getLastDamageCause() != null && event.getEntity().getLastDamageCause().getCause().name().equals("ENTITY_ATTACK")) {
            return;
        }

        event.setCancelled(true);
    }
}
