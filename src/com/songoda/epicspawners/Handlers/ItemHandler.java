package com.songoda.epicspawners.Handlers;

import com.songoda.arconix.Arconix;
import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Events.SpawnerListeners;
import com.songoda.epicspawners.Spawners.Spawner;
import com.songoda.epicspawners.Spawners.SpawnerItem;
import com.songoda.epicspawners.Utils.Debugger;
import com.songoda.epicspawners.Utils.Methods;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by songo on 9/3/2017.
 */
public class ItemHandler {
    EpicSpawners plugin = EpicSpawners.pl();

    public ItemHandler() {
        try {
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> dropItems(), 10L, 20L);
        } catch (Exception e) {
            Debugger.runReport(e);
        }
    }

    public static void dropItems() {
        try {
            EpicSpawners plugin = EpicSpawners.pl();

            if (plugin.dataFile.getConfig().contains("data.spawner")) {
                ConfigurationSection cs3 = plugin.dataFile.getConfig().getConfigurationSection("data.spawner");
                for (String key2 : cs3.getKeys(false)) {
                    Location location2 = Arconix.pl().serialize().unserializeLocation(key2);
                    Spawner sp = new Spawner(location2);
                    //if (plugin.hooksFile.getConfig().contains("Entities." + sp.spawnedTypeU + ".items") || sp.spawnedTypeU.equalsIgnoreCase("OMNI")) {
                    if (plugin.spawnerFile.getConfig().contains("Entities." + sp.spawnedType + ".items")) {
                        try {
                            String type = Methods.getType(sp.getSpawner().getSpawnedType());
                            if (plugin.dataFile.getConfig().contains("data.spawnerstats." + Arconix.pl().serialize().serializeLocation(location2) + ".type")) {
                                if (!plugin.dataFile.getConfig().getString("data.spawnerstats." + Arconix.pl().serialize().serializeLocation(location2) + ".type").equals("OMNI")) {
                                    type = Methods.getTypeFromString(plugin.dataFile.getConfig().getString("data.spawnerstats." + Arconix.pl().serialize().serializeLocation(location2) + ".type"));
                                }
                            }
                            List<SpawnerItem> list = plugin.getApi().convertFromList(plugin.dataFile.getConfig().getStringList("data.spawnerstats." + Arconix.pl().serialize().serializeLocation(location2) + ".entities"));
                            if (list.size() == 0)
                                list.add(new SpawnerItem(type, 1));
                            for (SpawnerItem omni : list) {
                                if (plugin.spawnerFile.getConfig().contains("Entities." + Methods.getTypeFromString(omni.getType()) + ".items")) {
                                    String key3 = key2 + omni.getType();

                                    int rate = plugin.spawnerFile.getConfig().getInt("Entities." + Methods.getTypeFromString(omni.getType()) + ".itemTickRate");
                                    if (plugin.api.getSpawnerMultiplier(location2) != 1)
                                        rate = rate / plugin.api.getSpawnerMultiplier(location2);

                                    long cur = (System.currentTimeMillis() / 1000);
                                    long next = cur + rate;

                                    if (plugin.tickTracker.containsKey(key3)) {
                                        long goal = plugin.tickTracker.get(key3);
                                        if (cur >= goal) {
                                            if (!sp.getSpawner().getBlock().isBlockPowered() && plugin.getConfig().getBoolean("settings.redstone-activate")) {
                                                for (ItemStack item : (ArrayList<ItemStack>) plugin.spawnerFile.getConfig().getList("Entities." + Methods.getTypeFromString(omni.getType()) + ".items")) {
                                                    Location loc = location2.clone();
                                                    loc.add(.5, .9, .5);
                                                    Item it = loc.getWorld().dropItemNaturally(loc, item);

                                                    Random r = new Random();
                                                    double rx = -.2 + (.2 - -.2) * r.nextDouble();
                                                    r = new Random();
                                                    double ry = 0 + (.5 - 0) * r.nextDouble();
                                                    r = new Random();
                                                    double rz = -.2 + (.2 - -.2) * r.nextDouble();
                                                    it.setVelocity(new Vector(rx, ry, rz));
                                                }
                                            }
                                            plugin.tickTracker.put(key3, next);
                                        }
                                    } else
                                        plugin.tickTracker.put(key3, next);
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                    if (plugin.spawnerFile.getConfig().contains("Entities." + Methods.getTypeFromString(sp.spawnedType) + ".commands")) {
                        try {
                            String key3 = key2 + sp.spawnedType;

                            int rate = plugin.spawnerFile.getConfig().getInt("Entities." + Methods.getTypeFromString(sp.spawnedType) + ".commandTickRate");
                            if (plugin.api.getSpawnerMultiplier(location2) != 1)
                                rate = rate / plugin.api.getSpawnerMultiplier(location2);

                            long cur = (System.currentTimeMillis() / 1000);
                            long next = cur + rate;

                            if (plugin.tickTracker2.containsKey(key3)) {
                                long goal = plugin.tickTracker2.get(key3);
                                if (cur >= goal) {
                                    if (!sp.getSpawner().getBlock().isBlockPowered() && plugin.getConfig().getBoolean("settings.redstone-activate")) {

                                        for (String cmd : (ArrayList<String>) plugin.spawnerFile.getConfig().getList("Entities." + Methods.getTypeFromString(sp.spawnedType) + ".commands")) {
                                            Location loc = location2.clone();
                                            loc.add(.5, 0, .5);

                                            double x = 1;
                                            double y = 1;
                                            double z = 1;

                                            boolean good = false;
                                            while (!good) {
                                                double testX = ThreadLocalRandom.current().nextDouble(-1, 1);
                                                double testY = ThreadLocalRandom.current().nextDouble(-1, 2);
                                                double testZ = ThreadLocalRandom.current().nextDouble(-1, 1);

                                                x = loc.getX() + testX * (double) 3;
                                                y = loc.getY() + testY;
                                                z = loc.getZ() + testZ * (double) 3;

                                                Location loc2 = new Location(loc.getWorld(), x, y, z);
                                                Methods.isAir(loc.getBlock().getType());
                                                Location loc3 = loc2.clone().subtract(0, 1, 0);
                                                if (Methods.isAir(loc2.getBlock().getType()) && !loc3.getBlock().getType().equals(Material.AIR))
                                                    good = true;
                                            }

                                            if (cmd.toLowerCase().contains("@x") || cmd.toLowerCase().contains("@y") || cmd.toLowerCase().contains("@z")) {
                                                if (Methods.countEntitiesAroundLoation(location2) > plugin.spawnerFile.getConfig().getInt("Entities." + sp.spawnedType + ".commandSpawnLimit") &&
                                                        plugin.spawnerFile.getConfig().getInt("Entities." + sp.spawnedType + ".commandSpawnLimit") != 0)
                                                    return;
                                            }
                                            boolean uP = true;
                                            if (cmd.toLowerCase().contains("@p")) {
                                                uP = false;
                                                if (!plugin.v1_7) {
                                                    List<String> arr = Arrays.asList(plugin.getConfig().getString("settings.Search-Radius").split("x"));
                                                    Collection<Entity> nearbyEntite = loc.getWorld().getNearbyEntities(loc.clone().add(0.5, 0.5, 0.5), Integer.parseInt(arr.get(0)), Integer.parseInt(arr.get(1)), Integer.parseInt(arr.get(2)));
                                                    if (nearbyEntite.size() >= 1) {
                                                        for (Entity ee : nearbyEntite) {
                                                            if (ee instanceof LivingEntity) {
                                                                if (ee instanceof Player) {
                                                                    uP = true;
                                                                    cmd = cmd.replace("@p", ee.getName());
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                            cmd = cmd.replace("@x", Integer.toString((int) Math.ceil(x)));
                                            cmd = cmd.replace("@y", Integer.toString((int) Math.ceil(y)));
                                            cmd = cmd.replace("@z", Integer.toString((int) Math.ceil(z)));
                                            cmd = cmd.replace("@X", Integer.toString((int) Math.ceil(x)));
                                            cmd = cmd.replace("@Y", Integer.toString((int) Math.ceil(y)));
                                            cmd = cmd.replace("@Z", Integer.toString((int) Math.ceil(z)));

                                            if (uP)
                                                Bukkit.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);

                                        }
                                    }
                                    plugin.tickTracker2.put(key3, next);
                                }
                            } else
                                plugin.tickTracker2.put(key3, next);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }
}
