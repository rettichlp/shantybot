package de.rettichlp.music.commands;

import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import de.rettichlp.music.MusicController;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class SkipCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		GuildVoiceState state;
		if ((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if ((vc = state.getChannel()) != null) {
				MusicController controller = ShantyBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
				AudioPlayer player = controller.getPlayer();
				player.getPlayingTrack().setPosition(player.getPlayingTrack().getDuration());
				message.addReaction("U+1F44C").queue();
			} else
				channel.sendMessage(ShantyBot.err_noVoice).complete().delete().queueAfter(3, TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.err_noVoice).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
