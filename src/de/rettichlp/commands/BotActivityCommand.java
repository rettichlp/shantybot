package de.rettichlp.commands;

import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class BotActivityCommand implements ServerCommand{
	
	public static String[] botActivity = new String[] {"ShantyTown", "Minecraft", "mit %members Spielern", "shantytown.eu", "mit ScuroK", "mit euch!", "!sb", "Vote-Seiten durch", 
			"Paintball auf Shanty", "Fuﬂball auf ShantyTown", "Survival auf ShantyTown", "Creative auf ShantyTown", "AcidIsland", 
			"shantytown.eu:8123", "!sb play Musik", "mit Rettich"};
	
	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.ADMINISTRATOR) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			
			String[] args = message.getContentDisplay().split(" ");
		
			if(args.length >= 3) {
				
				if (args[2].equalsIgnoreCase("standard")) {
					botActivity = new String[] {"ShantyTown", "Minecraft", "mit %members Spielern", "shantytown.eu", "mit ScuroK", "mit euch!", "!sb", "Vote-Seiten durch", 
							"Paintball auf Shanty", "Fuﬂball auf ShantyTown", "Survival auf ShantyTown", "Creative auf ShantyTown", "AcidIsland", 
							"shantytown.eu:8123", "!sb play Musik", "mit Rettich"};
				} else {
					String activity = "";
					for (int i = 2; i < args.length; i++) {
						activity = activity + args[i] + " ";
					}
					botActivity = new String[] {activity};
				}
			} else
				channel.sendMessage(ShantyBot.syn_activity).complete().delete().queueAfter(3, TimeUnit.SECONDS);				
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}