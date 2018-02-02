package com.songoda.epicspawners.Hooks;

import com.songoda.epicspawners.EpicSpawners;
import com.songoda.epicspawners.Utils.Debugger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;

import java.util.List;

/**
 * Created by songoda on 3/17/2017.
 */
public class USkyBlockHook implements Hooks {

    private EpicSpawners plugin = EpicSpawners.pl();
    private String pluginName = "USkyBlock";

    USkyBlockHook() {
        if (plugin.getServer().getPluginManager().getPlugin(pluginName) != null) {
            plugin.hooks.hooksFile.getConfig().addDefault("hooks." + pluginName, true);
            if (!plugin.hooks.hooksFile.getConfig().contains("hooks." + pluginName) || plugin.hooks.hooksFile.getConfig().getBoolean("hooks." + pluginName)) {
                plugin.hooks.USkyBlockHook = this;
            }
        }
    }

    Plugin skyblock = Bukkit.getPluginManager().getPlugin("uSkyBlock");

    uSkyBlockAPI usb = (uSkyBlockAPI) skyblock;

    @Override
    public boolean canBuild(Player p, Location location) {
        try {
            if (p.hasPermission(plugin.getDescription().getName() + ".bypass")) {
                return true;
            } else {

                List<Player> list = usb.getIslandInfo(location).getOnlineMembers();

                for (Player pl : list) {
                    if (pl.equals(p)) {
                        return true;
                    }
                }

                if (usb.getIslandInfo(location).isLeader(p)) {
                    return true;
                }
            }
        } catch (Exception e) {
            Debugger.runReport(e);
        }
        return false;
    }

    @Override
    public boolean isInClaim(String uuid, Location location) {
        String owner = usb.getIslandInfo(location).getLeader();
        if (uuid.equals(owner))
            return true;
        return false;
    }

    @Override
    public String getClaimId(String name) {
        return null;
    }
}
