package de.rettichlp.shantybot.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.rettichlp.shantybot.common.lavaplayer.GuildMusicManager;
import de.rettichlp.shantybot.common.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class MusicSkipCommand extends CommandBase {

    public MusicSkipCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        GuildVoiceState memberVoiceState = requireNonNull(event.getMember()).getVoiceState();
        if (isNull(memberVoiceState) || !memberVoiceState.inAudioChannel()) {
            sendSelfDeletingMessage(event, "Du musst in einem Audio Channel sein um diesen Befehl zu nutzen.");
            return;
        }

        Guild guild = discordBotProperties.getGuild();
        GuildVoiceState selfVoiceState = guild.getSelfMember().getVoiceState();
        if (isNull(selfVoiceState) || !selfVoiceState.inAudioChannel()) {
            sendSelfDeletingMessage(event, "Es wird gerade keine Musik gespielt.");
            return;
        }

        if (!Objects.equals(memberVoiceState.getChannel(), selfVoiceState.getChannel())) {
            sendSelfDeletingMessage(event, "Du musst in dem selben Audio Channel wie der Bot sein.");
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(requireNonNull(event.getGuild()));
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (audioPlayer.getPlayingTrack() == null) {
            sendSelfDeletingMessage(event, "Es wird gerade keine Musik gespielt.");
            return;
        }

        musicManager.nextTrack();

        // TODO send song message
    }
}
