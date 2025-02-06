package me.seedim.mayaMelody.Services;

import com.xxmicloxx.NoteBlockAPI.model.FadeType;
import com.xxmicloxx.NoteBlockAPI.model.Playlist;
import com.xxmicloxx.NoteBlockAPI.model.RepeatMode;
import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.songplayer.Fade;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;
import me.seedim.mayaMelody.MayaMelody;
import org.bukkit.Bukkit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    private static Playlist playlist;
    private static RadioSongPlayer rsp;

    public PlayerService() {
        // load playlist
        if (loadPlayList()) {
            // Enable player
            play();
        }
    }

    private boolean loadPlayList() {
        List<Song> songs = new ArrayList<>();
        File songDirectory = new File(MayaMelody.getInstance().getDataFolder(), "songs");

        if (!songDirectory.exists() && !songDirectory.mkdirs()) {
            Bukkit.getLogger().warning("Failed to create song directory.");
            return false;
        }

        // Add songs in directory to the playlist
        for (File file : songDirectory.listFiles()) {
            if (file.isFile() && file.getName().endsWith(".nbs")) {

                // Try to parse the file
                Song song = NBSDecoder.parse(file);

                if (song == null) {
                    Bukkit.getLogger().warning("Failed to load song: " + file.getName());
                    return false;
                }
                songs.add(song); // Add song to the playlist
            }
        }

        // Check if playlist is empty
        if (songs.isEmpty()) {
            return false;
        }

        playlist = new Playlist(songs.toArray(new Song[0]));
        return true;
    }

    // Start the song player
    public static void play() {
        if (playlist == null) {
            return;
        }

        // Create RadioPlayer to play the song for all players
        rsp = new RadioSongPlayer(playlist);

        rsp.setRepeatMode(RepeatMode.ALL); // Enable looping playlist
        rsp.setRandom(true); // Enable Shuffle play

        // Set fade in
        Fade fadeIn = rsp.getFadeIn();
        fadeIn.setType(FadeType.LINEAR);
        fadeIn.setFadeDuration(60); // Duration in ticks

        // Start the player
        rsp.setPlaying(true);
    }

    // Getter for radio song player
    public static RadioSongPlayer getRsp() {return rsp;}

}
