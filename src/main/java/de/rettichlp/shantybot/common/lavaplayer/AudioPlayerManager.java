package de.rettichlp.shantybot.common.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers.registerLocalSource;
import static com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers.registerRemoteSources;
import static de.rettichlp.shantybot.common.services.UtilService.millisecondsToMMSS;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Objects.requireNonNull;

public class AudioPlayerManager {

    private final Map<Long, GuildMusicManager> musicManagers;
    private final com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager audioPlayerManager;

    public AudioPlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        registerRemoteSources(this.audioPlayerManager);
        registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager);
            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandInteractionEvent event, String trackUrl) {
        GuildMusicManager musicManager = getMusicManager(requireNonNull(event.getGuild()));
        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.queue(event.getChannel(), audioTrack);

                AudioTrackInfo audioTrackInfo = audioTrack.getInfo();
                MessageEmbed messageEmbed = new EmbedBuilder()
                        .setColor(0xcece80)
                        .addField("Musikwunsch von " + event.getUser().getEffectiveName(), "📬 **[" + audioTrackInfo.title + "](" + audioTrackInfo.uri + ")** von **" + audioTrackInfo.author + "** (" + millisecondsToMMSS(audioTrackInfo.length) + ")", false)
                        .build();

                event.replyEmbeds(messageEmbed).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {
                    AudioTrack audioTrack = tracks.get(0);
                    musicManager.queue(event.getChannel(), audioTrack);

                    AudioTrackInfo audioTrackInfo = audioTrack.getInfo();
                    MessageEmbed messageEmbed = new EmbedBuilder()
                            .setColor(0xcece80)
                            .addField("Musikwunsch von " + event.getUser().getEffectiveName(), "📬 **[" + audioTrackInfo.title + "](" + audioTrackInfo.uri + ")** von **" + audioTrackInfo.author + "** (" + millisecondsToMMSS(audioTrackInfo.length) + ") und " + (audioPlaylist.getTracks().size() - 1) + " weitere", false)
                            .build();

                    event.replyEmbeds(messageEmbed).queue();
                } else {
                    sendSelfDeletingMessage(event, "Die Playlist ist leer.");
                }
            }

            @Override
            public void noMatches() {
                if (trackUrl.startsWith("ytmsearch:")) {
                    sendSelfDeletingMessage(event, "Es wurde kein Song mit dem Namen `" + trackUrl.substring(9) + "` gefunden.");
                } else {
                    sendSelfDeletingMessage(event, "Es wurde kein Song mit der URL `" + trackUrl + "` gefunden.");
                }
            }

            @Override
            public void loadFailed(FriendlyException e) {
                sendSelfDeletingMessage(event, "Fehler beim Laden des Songs.");
            }
        });
    }
}
