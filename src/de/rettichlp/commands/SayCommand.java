package de.rettichlp.commands;

import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class SayCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();

		if(m.hasPermission(channel, Permission.MESSAGE_MANAGE) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			String[] args = message.getContentDisplay().split(" ");
		
			if(args.length >= 3) {
				String say = "";
				for (int i = 2; i < args.length; i++) {
					say = say + args[i]+" ";
				}
				channel.sendMessage(say).queue();
				System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat '" + say + "' gesagt");
			} else
				channel.sendMessage(ShantyBot.syn_say).complete().delete().queueAfter(3, TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
