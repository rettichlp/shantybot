package de.rettichlp.shantybot.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.managers.AudioManager;

import static de.rettichlp.shantybot.ShantyBot.audioPlayerManager;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.lang.String.join;
import static java.util.Objects.isNull;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.ofNullable;

public class MusicCommand extends CommandBase {

    public MusicCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        OptionMapping linkOptionMapping = event.getOption("link");
        if (isNull(linkOptionMapping)) {
            sendSelfDeletingMessage(event, "Es muss ein Link oder Suchbegriff angegeben werden!");
            return;
        }

        GuildVoiceState memberVoiceState = requireNonNull(event.getMember()).getVoiceState();
        if (isNull(memberVoiceState) || !memberVoiceState.inAudioChannel()) {
            sendSelfDeletingMessage(event, "Du musst in einem Audio Channel sein um diesen Befehl zu nutzen!");
            return;
        }

        Guild guild = ofNullable(event.getGuild()).orElseThrow(() -> new NullPointerException("Guild is null"));
        GuildVoiceState selfVoiceState = guild.getSelfMember().getVoiceState();
        if (isNull(selfVoiceState) || !selfVoiceState.inAudioChannel()) {
            AudioManager audioManager = guild.getAudioManager();
            VoiceChannel voiceChannel = (VoiceChannel) memberVoiceState.getChannel();
            audioManager.openAudioConnection(voiceChannel);
            audioManager.setSelfDeafened(true);
        }

        String potentialLink = linkOptionMapping.getAsString();
        String link = potentialLink.startsWith("http") ? potentialLink : ("ytmsearch:" + join(" ", potentialLink + " audio"));

        audioPlayerManager.loadAndPlay(event, link);
    }
}
