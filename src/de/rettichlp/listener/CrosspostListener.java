package de.rettichlp.listener;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CrosspostListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		if(event.isFromType(ChannelType.TEXT)) {
			if (event.getChannel().getId().equalsIgnoreCase("625094112068108299")) { //Mitteilungen
				event.getMessage().crosspost().queue();
				System.out.println("[ShantyBot] Nachricht aus '#!-mitteilungen' veröffentlich");
			}
		}		
	}	
}
