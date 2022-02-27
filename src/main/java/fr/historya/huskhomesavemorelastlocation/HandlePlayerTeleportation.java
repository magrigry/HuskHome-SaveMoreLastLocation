package fr.historya.huskhomesavemorelastlocation;

import me.william278.huskhomes2.HuskHomes;
import me.william278.huskhomes2.api.HuskHomesAPI;
import me.william278.huskhomes2.data.DataManager;
import me.william278.huskhomes2.teleport.points.TeleportationPoint;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;

public class HandlePlayerTeleportation implements Listener {

    public HandlePlayerTeleportation(HuskHomesAPI huskHomesAPI) {

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTeleport(PlayerTeleportEvent event)
    {
        final Player player = event.getPlayer();
        final Location playerLocation = event.getPlayer().getLocation();

        if (player.hasMetadata("NPC") || ! (event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN || event.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND)) {
            return;
        }

        Bukkit.getScheduler().runTaskAsynchronously(HuskHomeSaveMoreLastLocation.plugin, () -> {

            // Cancel last position saving if last position is the current position
            try (Connection connection = HuskHomes.getConnection()) {
                TeleportationPoint teleportationPoint = DataManager.getPlayerLastPosition(event.getPlayer(), connection);
                if (
                        player.getLocation().getBlockX() == teleportationPoint.getLocation().getBlockX()
                                && playerLocation.getBlockY() == teleportationPoint.getLocation().getBlockY()
                                && playerLocation.getBlockZ() == teleportationPoint.getLocation().getBlockZ()
                                && playerLocation.getWorld() == teleportationPoint.getLocation().getWorld()
                ) {
                    return;
                }

                Bukkit.getLogger().log(Level.INFO, "Saving " + player.getName() + " last location. Cause : " + event.getCause());
                DataManager.setPlayerLastPosition(player, new TeleportationPoint(playerLocation, HuskHomes.getSettings().getServerID()), connection);
            } catch (SQLException e) {
                Bukkit.getLogger().log(Level.SEVERE, "An SQL exception occurred teleporting a player.", e);
            }
        });
    }

}
