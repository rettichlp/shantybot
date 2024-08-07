package de.rettichlp.shantybot.common.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static de.rettichlp.shantybot.common.services.UtilService.millisecondsToMMSS;
import static java.nio.ByteBuffer.wrap;
import static java.util.Objects.nonNull;
import static net.dv8tion.jda.api.interactions.components.buttons.Button.danger;
import static net.dv8tion.jda.api.interactions.components.buttons.Button.primary;
import static net.dv8tion.jda.api.interactions.components.buttons.Button.secondary;
import static net.dv8tion.jda.api.interactions.components.buttons.Button.success;

@Getter
@Setter
public class GuildMusicManager extends AudioEventAdapter implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private final BlockingQueue<AudioTrack> queue;
    private AudioFrame lastFrame;
    private TextChannel musicTextChannel;
    private String nowPlayingEmbedId;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.audioPlayer.addListener(this);
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(Channel channel, AudioTrack audioTrack) {
        this.musicTextChannel = (TextChannel) channel;
        this.queue.add(audioTrack);
        this.audioPlayer.startTrack(this.queue.poll(), true);
    }

    public void nextTrack() {
        this.audioPlayer.startTrack(this.queue.poll(), false);
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        if (nonNull(this.nowPlayingEmbedId)) {
            this.musicTextChannel.retrieveMessageById(this.nowPlayingEmbedId).queue(message -> message.editMessageEmbeds(message.getEmbeds().getFirst()).setActionRow(
                    primary("btn_queue", "📑"),
                    success("btn_resume", "▶️"),
                    secondary("btn_pause", "⏸️"),
                    primary("btn_skip", "⏭️"),
                    danger("btn_stop", "⏹️")
            ).queue());
        }
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        if (nonNull(this.nowPlayingEmbedId)) {
            this.musicTextChannel.retrieveMessageById(this.nowPlayingEmbedId).queue(message -> message.editMessageEmbeds(message.getEmbeds().getFirst()).setActionRow(
                    primary("btn_queue", "📑"),
                    secondary("btn_resume", "▶️"),
                    primary("btn_pause", "⏸️"),
                    primary("btn_skip", "⏭️"),
                    danger("btn_stop", "⏹️")
            ).queue());
        }
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack audioTrack) {
        if (nonNull(this.nowPlayingEmbedId)) {
            this.musicTextChannel.retrieveMessageById(this.nowPlayingEmbedId).queue(message -> message.delete().queue());
        }

        AudioTrackInfo audioTrackInfo = audioTrack.getInfo();
        MessageEmbed messageEmbed = new EmbedBuilder()
                .setColor(0xcece80)
                .setAuthor("📀 Jetzt" + (audioTrackInfo.isStream ? " LIVE 🔴" : ""))
                .setTitle("🎶 • " + audioTrackInfo.title, audioTrackInfo.uri)
                .addField("Author", audioTrackInfo.author, true)
                .addField("Länge", millisecondsToMMSS(audioTrackInfo.length), true)
                .setThumbnail(audioTrackInfo.artworkUrl)
                .build();

        this.musicTextChannel.sendMessageEmbeds(messageEmbed).addActionRow(
                primary("btn_queue", "📑"),
                (player.isPaused() ? success("btn_resume", "▶️") : secondary("btn_resume", "▶️")),
                (player.isPaused() ? secondary("btn_pause", "⏸️") : primary("btn_pause", "⏸️")),
                primary("btn_skip", "⏭️"),
                danger("btn_stop", "⏹️")
        ).queue(message -> this.nowPlayingEmbedId = message.getId());
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }

        // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
        // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
        // endReason == STOPPED: The player was stopped.
        // endReason == REPLACED: Another track started playing while this had not finished
        // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a clone of this back to your queue
    }

    @Override
    public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
        // An already playing track threw an exception (track end event will still be received separately)
    }

    @Override
    public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
        // Audio track has been unable to provide us any audio, might want to just start a new track
    }

    @Override
    public boolean canProvide() {
        this.lastFrame = this.audioPlayer.provide();
        return lastFrame != null;
    }

    @Override
    public ByteBuffer provide20MsAudio() {
        return wrap(this.lastFrame.getData());
    }

    @Override
    public boolean isOpus() {
        return true;
    }
}
