package me.seedim.mayaMelody;

import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import me.seedim.mayaMelody.Commands.MelodyCommand;
import me.seedim.mayaMelody.Listeners.ConnectionListener;
import me.seedim.mayaMelody.Listeners.PlayerListener;
import me.seedim.mayaMelody.Services.MetricsService;
import me.seedim.mayaMelody.Services.PlayerService;
import me.seedim.mayaMelody.Services.UpdateService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class MayaMelody extends JavaPlugin {

    private static MayaMelody instance;

    @Override
    public void onEnable() {
        instance = this;

        // Check updates
        new UpdateService(this, "OwSKfAzP");

        // Enable metrics
        new MetricsService(this, 24640);

        // Load default config
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        // Commands
        LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
        manager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(MelodyCommand.registerCommand().build(),
                    "Manage MAYA: Melody music player.");
        });

        // Services
        new PlayerService();

        // Tasks
        // Enable radio song player if it is enabled in config
        if (this.getConfig().getBoolean("radio-song-player.enable-radio-song-player", true)) {
            Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
            Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        }
    }

    // Getter for plugin instance
    public static MayaMelody getInstance() {return instance;}
}
