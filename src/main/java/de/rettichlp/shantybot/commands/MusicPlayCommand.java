package de.rettichlp.shantybot.commands;

import de.rettichlp.shantybot.common.lavaplayer.PlayerManager;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;

import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.lang.String.join;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Objects.requireNonNull;

public class MusicPlayCommand extends CommandBase {

    public MusicPlayCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        OptionMapping linkOptionMapping = event.getOption("link");

        if (nonNull(linkOptionMapping)) {
            GuildVoiceState memberVoiceState = requireNonNull(event.getMember()).getVoiceState();
            if (isNull(memberVoiceState) || !memberVoiceState.inAudioChannel()) {
                sendSelfDeletingMessage(event, "Du musst in einem Audio Channel sein um diesen Befehl zu nutzen.");
                return;
            }

            Guild guild = discordBotProperties.getGuild();
            GuildVoiceState selfVoiceState = guild.getSelfMember().getVoiceState();
            if (isNull(selfVoiceState) || !selfVoiceState.inAudioChannel()) {
                AudioManager audioManager = guild.getAudioManager();
                VoiceChannel voiceChannel = (VoiceChannel) memberVoiceState.getChannel();
                audioManager.openAudioConnection(voiceChannel);
            }

            String potentialLink = linkOptionMapping.getAsString();
            String link = potentialLink.startsWith("http") ? potentialLink : ("ytmsearch:" + join(" ", potentialLink + " audio"));

            PlayerManager.getInstance().loadAndPlay(event, link);
        } else {
            sendSelfDeletingMessage(event, "Es muss ein Link oder Suchbegriff angegeben werden.");
        }
    }
}
