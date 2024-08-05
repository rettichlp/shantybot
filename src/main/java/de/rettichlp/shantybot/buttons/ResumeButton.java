package de.rettichlp.shantybot.buttons;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.rettichlp.shantybot.common.lavaplayer.GuildMusicManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.Objects;

import static de.rettichlp.shantybot.ShantyBot.audioPlayerManager;
import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class ResumeButton extends ButtonBase {

    public ResumeButton() {
        super("btn_resume");
    }

    @Override
    public void onButtonClick(ButtonInteractionEvent event) {
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

        GuildMusicManager musicManager = audioPlayerManager.getMusicManager(requireNonNull(event.getGuild()));
        AudioPlayer audioPlayer = musicManager.getAudioPlayer();

        if (!audioPlayer.isPaused()) {
            sendSelfDeletingMessage(event, "Es ist gerade keine Musik pausiert.");
            return;
        }

        audioPlayer.setPaused(false);
        sendSelfDeletingMessage(event, "Musik fortgesetzt.");
    }
}
