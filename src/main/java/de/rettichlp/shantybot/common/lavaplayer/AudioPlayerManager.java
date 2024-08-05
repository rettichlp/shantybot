package de.rettichlp.shantybot.common.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers.registerLocalSource;
import static com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers.registerRemoteSources;
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
                musicManager.queue(audioTrack);
                //                MusicEB musicEB = new MusicEB();
                //                musicEB.getBuilder().setDescription("A new music has been added to queue.");
                //                musicEB.getBuilder().addField("Music", audioTrack.getInfo().title, false);
                //                musicEB.getBuilder().addField("Author", audioTrack.getInfo().author, false);
                //                musicEB.getBuilder().addField("Added by", data.getCommandSender().getAsMention(), false);
                //                data.getEvent().getHook().sendMessageEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
                //                sendSelfDeletingMessage(event, "Ein neuer Song wurde zur Warteschlange hinzugefügt.");
                event.getChannel().sendMessage("Ja").queue();
                // TODO send song message
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                List<AudioTrack> tracks = audioPlaylist.getTracks();
                if (!tracks.isEmpty()) {
                    musicManager.queue(tracks.get(0));
                    //                    MusicEB musicEB = new MusicEB();
                    //                    musicEB.getBuilder().setDescription("A new music has been added to queue.");
                    //                    musicEB.getBuilder().addField("Music", tracks.get(0).getInfo().title, false);
                    //                    musicEB.getBuilder().addField("Author", tracks.get(0).getInfo().author, false);
                    //                    musicEB.getBuilder().addField("Added by", data.getCommandSender().getAsMention(), false);
                    //                    data.getEvent().getHook().sendMessageEmbeds(musicEB.getBuilder().build()).setActionRow(musicEB.getActionRow()).queue();
                    event.getChannel().sendMessage("Ja list").queue();
                }
            }

            @Override
            public void noMatches() {
                if (trackUrl.startsWith("ytsearch:")) {
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
