package de.rettichlp.shantybot.buttons;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.List;
import java.util.StringJoiner;
import java.util.concurrent.BlockingQueue;

import static de.rettichlp.shantybot.ShantyBot.audioPlayerManager;
import static de.rettichlp.shantybot.common.services.UtilService.millisecondsToMMSS;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.lang.Math.min;
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
            List<AudioTrack> audioTracks = queue.stream()
                    .toList()
                    .subList(0, min(queue.size(), 20));

            for (int i = 0; i < audioTracks.size(); i++) {
                AudioTrackInfo audioTrackInfo = audioTracks.get(i).getInfo();
                stringJoiner.add((i + 1) + ") [" + audioTrackInfo.title + "](" + audioTrackInfo.uri + ") von " + audioTrackInfo.author + " (" + millisecondsToMMSS(audioTrackInfo.length) + ")");
            }

            String andMoreString = queue.size() > 20 ? "... und " + (queue.size() - 20) + " weitere" : "";

            MessageEmbed messageEmbed = new EmbedBuilder()
                    .setColor(0xcece80)
                    .setAuthor("📑 Warteschlange")
                    .setDescription(stringJoiner + "\n" + andMoreString)
                    .build();

            event.replyEmbeds(messageEmbed).queue();
        }
    }
}
