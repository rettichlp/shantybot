package de.rettichlp.music;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

public class AudioLoadResult implements AudioLoadResultHandler {

	private final MusicController controller;
	private final String finalurl;
	private final TextChannel channel;
	
	public AudioLoadResult(MusicController controller, String finalurl, TextChannel channel) {
		this.finalurl = finalurl;
		this.controller = controller;
		this.channel = channel;
	}
	
	@Override
	public void trackLoaded(AudioTrack track) {
		
		if(controller.getPlayer().getPlayingTrack() == null) {
			controller.getPlayer().playTrack(track);
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.magenta);
			AudioTrackInfo info = track.getInfo();
			
			//if (channel.getId().equalsIgnoreCase("623861436585869312")) {
			//	channel.getManager().setTopic(info.title).queue();
			//}	
			
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
			embed.addField(info.title, "[" + url + "](" + url + ")", false);
			
			embed.addField("Länge", info.isStream ? ":red_circle: STREAM" : (stunden > 0 ? stunden + ":" : "") + min + minuten + ":" + sek + sekunden, true);
			embed.addField("Interpret", info.author, true);
			
			if (url.startsWith("https://www.youtube.com/watch?v=")) {
				String videoID = url.replace("https://www.youtube.com/watch?v=", "");
				
				InputStream file;
				try {
					file = new URL("https://img.youtube.com/vi/" + videoID + "/hqdefault.jpg").openStream();
					embed.setImage("attachment://thumbnail.png");

					channel.sendTyping().queue();
					channel.sendFile(file, "thumbnail.png").embed(embed.build()).queue();
					
				} catch (IOException e) { }
			} else
				MusicUtil.sendEmbed(channel.getGuild().getIdLong(), embed);
		} else {
			Queue queue = controller.getQueue();
			queue.addTrackToQueue(track);
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.magenta);
			embed.setTitle("ZUR WARTESCHLANGE HINZUGEFÜGT");
			AudioTrackInfo info = track.getInfo();
			String url = info.uri;
			embed.addField(info.title, "[" + url + "](" + url + ")", false);
			
			MusicUtil.sendEmbed(channel.getGuild().getIdLong(), embed);
		}
	}
	
	@Override
	public void playlistLoaded(AudioPlaylist playlist) {
		Queue queue = controller.getQueue();
		
		if(finalurl.startsWith("ytsearch: ")) {
			queue.addTrackToQueue(playlist.getTracks().get(0));
			return;
		}
		
		@SuppressWarnings("unused")
		int added = 0;
		
		for(AudioTrack track : playlist.getTracks()) {
			queue.addTrackToQueue(track);
			added++;
		}
		
		EmbedBuilder builder = new EmbedBuilder();
		builder.setColor(Color.magenta);
		builder.setTitle("ZUR WARTESCHLANGE HINZUGEFÜGT");
		int weitere = queue.getQueuelist().size() - 1;
		builder.addField(queue.getQueuelist().get(0).getInfo().title, " und " + weitere + " weitere Titel", false);
		
		MusicUtil.sendEmbed(controller.getGuild().getIdLong(), builder);

	}
	
	@Override
	public void noMatches() {
	}
	
	@Override
	public void loadFailed(FriendlyException exception) {
	}

}
