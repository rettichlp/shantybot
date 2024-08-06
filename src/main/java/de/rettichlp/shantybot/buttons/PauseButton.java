package de.rettichlp.shantybot.buttons;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.rettichlp.shantybot.common.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Objects;

import static de.rettichlp.shantybot.ShantyBot.audioPlayerManager;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class PauseButton extends ButtonBase {

    public PauseButton() {
        super("btn_pause");
    }

    @Override
    public void onButtonClick(ButtonInteractionEvent event) {
        GuildVoiceState memberVoiceState = requireNonNull(event.getMember()).getVoiceState();
        if (isNull(memberVoiceState) || !memberVoiceState.inAudioChannel()) {
            sendSelfDeletingMessage(event, "Du musst in einem Audio Channel sein um diesen Befehl zu nutzen!");
            return;
        }

        Guild guild = ofNullable(event.getGuild()).orElseThrow(() -> new NullPointerException("Guild is null"));
        GuildVoiceState selfVoiceState = guild.getSelfMember().getVoiceState();
        if (isNull(selfVoiceState) || !selfVoiceState.inAudioChannel()) {
            sendSelfDeletingMessage(event, "Es wird gerade keine Musik gespielt!");
            return;
        }

        if (!Objects.equals(memberVoiceState.getChannel(), selfVoiceState.getChannel())) {
            sendSelfDeletingMessage(event, "Du musst in dem selben Audio Channel wie der Bot sein!");
            return;
        }

        GuildMusicManager musicManager = audioPlayerManager.getMusicManager(requireNonNull(event.getGuild()));
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (audioPlayer.getPlayingTrack() == null) {
            sendSelfDeletingMessage(event, "Es wird gerade keine Musik gespielt!");
            return;
        }

        audioPlayer.setPaused(true);
        sendSelfDeletingMessage(event, "Musik pausiert.");
    }
}
