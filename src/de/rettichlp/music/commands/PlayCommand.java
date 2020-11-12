package de.rettichlp.music.commands;

import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import de.rettichlp.music.AudioLoadResult;
import de.rettichlp.music.MusicController;
import de.rettichlp.music.MusicUtil;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		String[] args = message.getContentDisplay().split(" ");
		
		message.delete().queue();
		
		if (args.length >= 3) {
			GuildVoiceState state;
			if ((state = m.getVoiceState()) != null) {
				VoiceChannel vc;
				if ((vc = state.getChannel()) != null) {
					MusicController controller = ShantyBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
					AudioPlayerManager apm = ShantyBot.INSTANCE.audioPlayerManager;
					AudioManager manager = vc.getGuild().getAudioManager();
					manager.openAudioConnection(vc);
					
					MusicUtil.updateChannel(channel);
					
					StringBuilder strBuilder = new StringBuilder();
					for (int i = 2; i < args.length; i++) {strBuilder.append(args[i] + " ");}
					
					String url = strBuilder.toString().trim();
					if (!url.startsWith("http")) {
						url = "ytsearch:" + url;
					}
										
					apm.loadItem(url, new AudioLoadResult(controller, url, channel));
				} else
					channel.sendMessage(ShantyBot.err_noVoice).complete().delete().queueAfter(3, TimeUnit.SECONDS);
			} else
				channel.sendMessage(ShantyBot.err_noVoice).complete().delete().queueAfter(3, TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.syn_play).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
