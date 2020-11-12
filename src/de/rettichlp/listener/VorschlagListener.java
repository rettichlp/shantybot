package de.rettichlp.listener;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VorschlagListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String message = event.getMessage().getContentDisplay();
		
		if(event.isFromType(ChannelType.TEXT)) {
			TextChannel channel = event.getTextChannel();
			
			if (channel.getId().equalsIgnoreCase("699028569778552882") && !event.getMember().getId().equalsIgnoreCase("725076053495644201")) {
				event.getMessage().delete().queue();
				
				EmbedBuilder builder = new EmbedBuilder();
				builder.setTitle("Vorschlag");
				builder.setDescription(message);
				builder.setColor(Color.blue);
				builder.setFooter(event.getMember().getEffectiveName(), event.getMember().getUser().getAvatarUrl());
				
				channel.sendMessage(builder.build()).queue(msg -> {
					msg.addReaction("👍").queue();
					msg.addReaction("👎").queue();
				});
			}
		}		
	}	
}