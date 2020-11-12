package de.rettichlp.music.commands;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import de.rettichlp.music.MusicController;
import de.rettichlp.music.Queue;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class TrackInfoCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		GuildVoiceState state;
		if((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if((vc = state.getChannel()) != null) {
				MusicController controller = ShantyBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
				Queue queue = controller.getQueue();
				AudioTrackInfo info = controller.getPlayer().getPlayingTrack().getInfo();
				List<AudioTrack> queuelist = queue.getQueuelist();
				
				if (info != null) {
					
					EmbedBuilder embed = new EmbedBuilder();
					embed.setColor(Color.magenta);
					
					long sekunden = info.length / 1000;
					long minuten = sekunden / 60;
					long stunden = minuten / 60;
					sekunden %= 60;
					minuten %= 60;
					stunden %= 60;
					
					String min = "";
					String sek = "";
					if (stunden != 0) {
						if (minuten < 10) {
							min = "0";
						}
					}
					if (sekunden < 10) {
						sek = "0";
					}
					
					String url = info.uri;
					embed.setTitle("JETZT");
					embed.addField(info.title, "[" + url + "](" + url + ")", false);
					
					embed.addField("Länge", info.isStream ? ":red_circle: STREAM" : (stunden > 0 ? stunden + ":" : "") + min + minuten + ":" + sek + sekunden, true);
					embed.addField("Interpret", info.author, true);
					
					channel.sendMessage(embed.build()).queue();				
					
					EmbedBuilder embedqueue = new EmbedBuilder();
					embedqueue.setColor(Color.magenta);
					
					if (queuelist.size() > 5) {
						embedqueue.setTitle("WARTESCHLANGE (nächsten 5 Titel)");
					} else {
						embedqueue.setTitle("WARTESCHLANGE");
					}
					
					if(queuelist.size() >= 1) {
						for (int i = 0; i < 5; i++) {
							try {
								embedqueue.addField(i+1 + ". " + queuelist.get(i).getInfo().title,queuelist.get(i).getInfo().author,false);
							} catch (Exception e) {}
						}
						channel.sendMessage(embedqueue.build()).queue();
					}
				} else
					channel.sendMessage(ShantyBot.err_noMusic).complete().delete().completeAfter(3, TimeUnit.SECONDS);
			} else
				channel.sendMessage(ShantyBot.err_noVoice).complete().delete().queueAfter(3, TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.err_noVoice).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
