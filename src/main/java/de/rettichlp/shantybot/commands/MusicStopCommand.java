package de.rettichlp.shantybot.commands;

import de.rettichlp.shantybot.common.lavaplayer.GuildMusicManager;
import de.rettichlp.shantybot.common.lavaplayer.PlayerManager;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.Objects;

import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;

public class MusicStopCommand extends CommandBase {

    public MusicStopCommand(String name) {
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

        if (Objects.equals(memberVoiceState.getChannel(), selfVoiceState.getChannel())) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
            musicManager.getAudioPlayer().stopTrack();
            musicManager.getQueue().clear();
            guild.getAudioManager().closeAudioConnection();
            sendSelfDeletingMessage(event, "Musik wurde gestoppt.");
        }
    }
}
