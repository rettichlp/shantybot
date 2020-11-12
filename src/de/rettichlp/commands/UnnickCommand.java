package de.rettichlp.commands;

import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class UnnickCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.NICKNAME_MANAGE) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			String[] args = message.getContentDisplay().split(" ");
		
			if(args.length == 2) {
				m.modifyNickname(null).queue();
				channel.sendMessage("Nickname entfernt").complete().delete().queueAfter(3, TimeUnit.SECONDS);
				System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat seinen Nickname zurückgesetzt");
			} else
				channel.sendMessage(ShantyBot.syn_unnick).complete().delete().queueAfter(3, TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
