package me.seedim.mayaMelody.Listeners;

import me.seedim.mayaMelody.Services.PlayerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionListener implements Listener {

    // Add joining player to the radio player
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        // Check if player is already in radio player
        if (PlayerService.getRsp().getPlayerUUIDs().contains(e.getPlayer().getUniqueId())) {
            return;
        }

        // Add player to the radio player
        PlayerService.getRsp().addPlayer(e.getPlayer());
    }

    // Remove player from the radio player
    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        PlayerService.getRsp().removePlayer(e.getPlayer());
    }
}
