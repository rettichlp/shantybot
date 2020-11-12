package de.rettichlp.listener;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BanListener extends ListenerAdapter {

	@Override
	public void onGuildBan(GuildBanEvent event) {
		
		Guild guild = event.getGuild();
		TextChannel channel = guild.getSystemChannel();
		User member = event.getUser();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(member.getAsTag());
		embed.setColor(Color.red);
		embed.setThumbnail(member.getAvatarUrl());
		embed.setDescription("Wurde vom **" + guild.getName() + "** Discord Server gebannt! \n Hier sind nun " + (guild.getMembers().size() - 1) + " Spieler.\n \n");
				
		channel.sendMessage(embed.build()).queue();
		System.out.println("[ShantyBot] " + member.getName() + " wurde vom Server gebannt");
	}	
}