package me.seedim.mayaMelody.Listeners;

import com.xxmicloxx.NoteBlockAPI.event.SongNextEvent;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import me.seedim.mayaMelody.Services.PlayerService;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerListener implements Listener {

    @EventHandler
    public void onSongNext(SongNextEvent e) {
        RadioSongPlayer rsp = PlayerService.getRsp();

        // Notify that the song has changed
        for (Player player : Bukkit.getOnlinePlayers()) {

            // Return if player has not the player enabled
            if (!PlayerService.getRsp().getPlayerUUIDs().contains(player.getUniqueId())) {
                return;
            }

            player.sendActionBar(MiniMessage.miniMessage().deserialize("Playing: <#ff55ff>" + rsp.getSong().getTitle() + "</#ff55ff> by <#ff55ff>" + rsp.getSong().getOriginalAuthor() + "</#ff55ff>."));
        }
    }
}
