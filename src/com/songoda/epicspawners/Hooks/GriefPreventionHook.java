package com.songoda.epicspawners.Hooks;

import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Utils.Debugger;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * Created by songoda on 3/17/2017.
 */
public class GriefPreventionHook implements Hooks {

    private EpicSpawners plugin = EpicSpawners.pl();
    private String pluginName = "GriefPrevention";

    GriefPreventionHook() {
        if (plugin.getServer().getPluginManager().getPlugin(pluginName) != null) {
            plugin.hooks.hooksFile.getConfig().addDefault("hooks." + pluginName, true);
            if (!plugin.hooks.hooksFile.getConfig().contains("hooks." + pluginName) || plugin.hooks.hooksFile.getConfig().getBoolean("hooks." + pluginName)) {
                plugin.hooks.GriefPreventionHook = this;
            }
        }
    }

    @Override
    public boolean canBuild(Player p, Location location) {
        try {
            if (p.hasPermission(plugin.getDescription().getName() + ".bypass")) {
                return true;
            } else {
                Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, null);
                if (claim != null) {
                    if (claim.allowBuild(p, Material.STONE) == null) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return false;
    }

    @Override
    public boolean isInClaim(String id, Location location) {
        return false;
    }

    @Override
    public String getClaimId(String name) {
        return null;
    }
}
