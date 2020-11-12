package de.rettichlp.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import de.rettichlp.ShantyBot;

public class TrackScheduler extends AudioEventAdapter {
	
	@Override
	public void onPlayerPause(AudioPlayer player) {
		
	}
	
	@Override
	public void onPlayerResume(AudioPlayer player) {
		
	}
	
	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {

	}
	
	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		long guildid = ShantyBot.INSTANCE.playerManager.getGuildByPlayerHash(player.hashCode());
		MusicController controller = ShantyBot.INSTANCE.playerManager.getController(guildid);
		Queue queue = controller.getQueue();
		queue.next();
	}
}
