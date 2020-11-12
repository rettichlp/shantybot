package de.rettichlp.commands;

import java.util.concurrent.TimeUnit;

import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class TicketDeleteCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.ADMINISTRATOR) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			if (channel.getParent().getId().equalsIgnoreCase("770289166252507147")) {
				channel.delete().queue();
			} else
				channel.sendMessage("Dieser Kanal kann nicht geschlossen werden, da er nicht zur Ticket-Kategorie gehört!").complete().delete().queueAfter(3,  TimeUnit.SECONDS);
		} else
			channel.sendMessage("syntax").complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
