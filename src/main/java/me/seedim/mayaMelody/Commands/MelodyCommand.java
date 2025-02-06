package me.seedim.mayaMelody.Commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.seedim.mayaMelody.Services.PlayerService;
import com.xxmicloxx.NoteBlockAPI.songplayer.RadioSongPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class MelodyCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> registerCommand() {

        LiteralArgumentBuilder<CommandSourceStack> command = literal("melody")
                .executes(MelodyCommand::handleMainCommand)
                .then(literal("play").requires(sender -> sender.getSender().hasPermission("MayaMelody.play"))
                        .executes(MelodyCommand::handlePlayCommand))
                .then(literal("skip")
                        .requires(sender -> sender.getSender().hasPermission("MayaMelody.skip"))
                        .executes(MelodyCommand::handleSkipCommand))
                .then(literal("mute")
                        .executes(MelodyCommand::handleMuteCommand))
                .then(literal("info")
                        .executes(MelodyCommand::handleInfoCommand));
        return command;
    }

    private static int handleMainCommand(CommandContext<CommandSourceStack> context) {
        //  Send usage to player
        context.getSource().getSender().sendRichMessage("Use <gold>/melody</gold> to manage music player.");
        context.getSource().getSender().sendRichMessage("<gold>/melody play</gold> - Play or stop the player.");
        context.getSource().getSender().sendRichMessage("<gold>/melody skip</gold> - Skip current song.");
        context.getSource().getSender().sendRichMessage("<gold>/melody mute</gold> - Mute or unmute the player.");
        context.getSource().getSender().sendRichMessage("<gold>/melody info</gold> - Display the current song info.");
        return 1;
    }

    private static int handlePlayCommand(CommandContext<CommandSourceStack> context) {
        boolean playingStatus =  !PlayerService.getRsp().isPlaying();
        PlayerService.getRsp().setPlaying(playingStatus);
        context.getSource().getSender().sendRichMessage("<gradient:light_purple:dark_purple><bold>Melody</bold></gradient> >> Player " + (playingStatus ? "<green>started</green>" : "<red>stopped</red>") + ".");

        return 1;
    }

    private static int handleSkipCommand(CommandContext<CommandSourceStack> context) {
        PlayerService.getRsp().playNextSong();
        return 1;
    }

    private static int handleMuteCommand(CommandContext<CommandSourceStack> context) {
        // Check if sender is player
        if (!(context.getSource().getSender() instanceof Player player)) {
            Bukkit.getLogger().info("You must be a player to use this command.");
        } else {

            // Check if player is muted the player
            boolean inMute = PlayerService.getRsp().getPlayerUUIDs().contains(player.getUniqueId());

            // Mute or unmute the player
            if (inMute) {
                PlayerService.getRsp().removePlayer(player);
            } else {
                PlayerService.getRsp().addPlayer(player);
            }

            player.sendRichMessage("<gradient:light_purple:dark_purple><bold>Melody</bold></gradient> >> Player " + (inMute ? "<red>muted</red>" : "<green>unmuted</green>") + ".");
        }
        return 1;
    }

    private static int handleInfoCommand(CommandContext<CommandSourceStack> context) {
        RadioSongPlayer rsp = PlayerService.getRsp();
        context.getSource().getSender().sendRichMessage("<gradient:light_purple:dark_purple><bold>Melody</bold></gradient> >> Playing <#ff55ff>" + rsp.getSong().getTitle() + "</#ff55ff> by <#ff55ff>" + rsp.getSong().getOriginalAuthor() + "</#ff55ff>.");

        return 1;
    }
}
