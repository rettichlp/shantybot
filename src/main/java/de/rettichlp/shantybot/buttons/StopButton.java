package de.rettichlp.shantybot.buttons;

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

public class StopButton extends ButtonBase {

    public StopButton() {
        super("btn_stop");
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

        if (Objects.equals(memberVoiceState.getChannel(), selfVoiceState.getChannel())) {
            GuildMusicManager musicManager = audioPlayerManager.getMusicManager(guild);
            musicManager.getAudioPlayer().stopTrack();
            musicManager.getQueue().clear();
            musicManager.setNowPlayingEmbedId(null);
            guild.getAudioManager().closeAudioConnection();
            event.getMessage().delete().queue();
            sendSelfDeletingMessage(event, "Du hast die Musik gestoppt.");
        }
    }
}
