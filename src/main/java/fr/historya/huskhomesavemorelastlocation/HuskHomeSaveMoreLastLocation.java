package fr.historya.huskhomesavemorelastlocation;

import me.william278.huskhomes2.HuskHomes;
import me.william278.huskhomes2.api.HuskHomesAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class HuskHomeSaveMoreLastLocation extends JavaPlugin {

    public static HuskHomeSaveMoreLastLocation plugin;

    @Override
    public void onEnable() {

        plugin = this;

        HuskHomes huskHomes = (HuskHomes) Bukkit.getPluginManager().getPlugin("HuskHomes");

        if (huskHomes != null) {
            if (!huskHomes.isEnabled()) {
                return;
            }
            HuskHomesAPI huskHomesAPI = HuskHomesAPI.getInstance();
            getServer().getPluginManager().registerEvents(new HandlePlayerTeleportation(huskHomesAPI), this);
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
