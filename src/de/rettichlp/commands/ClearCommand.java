package de.rettichlp.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

public class ClearCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		if(m.hasPermission(channel, Permission.MESSAGE_MANAGE) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			String[] args = message.getContentDisplay().split(" ");
		
			if(args.length == 3) {
				if (args[2].equalsIgnoreCase("all")) {
					channel.purgeMessages(getAll(channel));
					channel.sendMessage("Alle Nachrichten gelöscht").complete().delete().queueAfter(3, TimeUnit.SECONDS);
					System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat alle Nachrichten gelöscht");										
				} else {
					try {
						int amount = Integer.parseInt(args[2]);
						channel.purgeMessages(getNumber(channel, amount));
						if (amount == 1) {
							channel.sendMessage(amount + " Nachricht gelöscht.").complete().delete().queueAfter(3, TimeUnit.SECONDS);
							System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat " + amount + " Nachricht gelöscht");
						} else {
							channel.sendMessage(amount + " Nachrichten gelöscht.").complete().delete().queueAfter(3, TimeUnit.SECONDS);
							System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat " + amount + " Nachrichten gelöscht");
						}						
						return;
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
			} else
				channel.sendMessage(ShantyBot.syn_clear).complete().delete().queueAfter(3, TimeUnit.SECONDS);				
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}

	
	public List<Message> getNumber(MessageChannel channel, int amount) {
		List<Message> messages = new ArrayList<>();
		int i = amount + 1;
		
		for(Message message : channel.getIterableHistory().cache(false)) {
			if(!message.isPinned()) {
				messages.add(message);
			}
			if(--i <= 0) break;
		}
		return messages;
	}
	
	public List<Message> getAll(MessageChannel channel) {
		List<Message> messages = new ArrayList<>();
		
		for(Message message : channel.getIterableHistory().cache(false)) {
			messages.add(message);
		}
		return messages;
	}
}
