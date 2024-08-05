package de.rettichlp.shantybot.buttons;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;

import static de.rettichlp.shantybot.ShantyBot.audioPlayerManager;
import static de.rettichlp.shantybot.common.services.UtilService.millisecondsToMMSS;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Objects.requireNonNull;

public class QueueButton extends ButtonBase {

    public QueueButton() {
        super("btn_queue");
    }

    @Override
    public void onButtonClick(ButtonInteractionEvent event) {
        BlockingQueue<AudioTrack> queue = audioPlayerManager.getMusicManager(requireNonNull(event.getGuild())).getQueue();

        if (queue.isEmpty()) {
            sendSelfDeletingMessage(event, "Die Warteschlange ist leer!");
        } else {
            StringJoiner stringJoiner = new StringJoiner("\n");
            int index = 1;
            for (AudioTrack audioTrack : queue) {
                AudioTrackInfo audioTrackInfo = audioTrack.getInfo();
                stringJoiner.add(index + ") [" + audioTrackInfo.title + "](" + audioTrackInfo.uri + ") von " + audioTrackInfo.author + " (" + millisecondsToMMSS(audioTrackInfo.length) + ")");
                index++;
            }

            MessageEmbed messageEmbed = new EmbedBuilder()
                    .setColor(0xcece80)
                    .setAuthor("📑 Warteschlange")
                    .setDescription(stringJoiner.toString())
                    .build();

            event.replyEmbeds(messageEmbed).queue();
        }
    }
}
