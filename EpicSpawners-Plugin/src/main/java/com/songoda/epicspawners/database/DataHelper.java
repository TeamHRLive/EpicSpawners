package com.songoda.epicspawners.database;

import com.songoda.core.database.DataManager;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.api.boosts.BoostManager;
import com.songoda.epicspawners.api.boosts.types.Boosted;
import com.songoda.epicspawners.api.player.PlayerData;
import com.songoda.epicspawners.boost.BoostManagerImpl;
import com.songoda.epicspawners.boost.types.BoostedPlayerImpl;
import com.songoda.epicspawners.boost.types.BoostedSpawnerImpl;
import com.songoda.epicspawners.player.PlayerDataManagerImpl;
import com.songoda.third_party.org.jooq.Record;
import com.songoda.third_party.org.jooq.Result;
import com.songoda.third_party.org.jooq.impl.DSL;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DataHelper {
    public static void loadData(DataManager dataManager, BoostManagerImpl boostManager, PlayerDataManagerImpl playerActionManager) {
        List<Boosted> boosted = new ArrayList<>();
        String prefix = dataManager.getTablePrefix();
        dataManager.getDatabaseConnector().connectDSL(dslContext -> {
            dslContext.select().from(DSL.table(prefix + "boosted_players")).fetch().forEach(record -> {
                boosted.add(new BoostedPlayerImpl(
                        UUID.fromString(record.get("player").toString()),
                        Integer.parseInt(record.get("amount").toString()),
                        Long.parseLong(record.get("end_time").toString())
                ));
            });

            dslContext.select().from(DSL.table(prefix + "boosted_spawners")).fetch().forEach(record -> {
                Location location = new Location(
                        Bukkit.getWorld(record.get("world").toString()),
                        Double.parseDouble(record.get("x").toString()),
                        Double.parseDouble(record.get("y").toString()),
                        Double.parseDouble(record.get("z").toString()));
                boosted.add(new BoostedSpawnerImpl(
                        location,
                        Integer.parseInt(record.get("amount").toString()),
                        Long.parseLong(record.get("end_time").toString())
                ));
            });
        });
        boostManager.addBoosts(boosted);

        //Load entity kills
        dataManager.getDatabaseConnector().connectDSL(dslContext -> {
            @NotNull Result<Record> results = dslContext.select().from(dataManager.getTablePrefix() + "entity_kills").fetch();
            results.stream().iterator().forEachRemaining(record -> {
                UUID uuid = UUID.fromString(record.get("player").toString());
                EntityType entityType = EntityType.valueOf(record.get("entity_type").toString());
                int amount = 0;
                try {
                    amount = Integer.parseInt(record.get("count").toString());
                } catch (NumberFormatException ex) {
                    amount = Double.valueOf(record.get("count").toString()).intValue();
                }
                PlayerData playerData = playerActionManager.getPlayerData(uuid);
                playerData.addKilledEntity(entityType, amount);
            });
        });
    }
}
