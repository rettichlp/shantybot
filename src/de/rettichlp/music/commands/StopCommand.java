package de.rettichlp.music.commands;

import java.util.concurrent.TimeUnit;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import de.rettichlp.music.MusicController;
import de.rettichlp.music.MusicUtil;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class StopCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		GuildVoiceState state;
		if ((state = m.getVoiceState()) != null) {
			VoiceChannel vc;
			if ((vc = state.getChannel()) != null) {
				MusicController controller = ShantyBot.INSTANCE.playerManager.getController(vc.getGuild().getIdLong());
				AudioManager manager = vc.getGuild().getAudioManager();
				AudioPlayer player = controller.getPlayer();
				MusicUtil.updateChannel(channel);
				player.stopTrack();
				controller.getQueue().getQueuelist().clear();
				manager.closeAudioConnection();
				message.addReaction("U+1F44C").queue();
			} else
				channel.sendMessage(ShantyBot.err_noVoice).complete().delete().queueAfter(3, TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.err_noVoice).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
