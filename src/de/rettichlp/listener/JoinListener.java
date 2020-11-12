package de.rettichlp.listener;

import java.awt.Color;
import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinListener extends ListenerAdapter  {
	
	@Override
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
				
		Guild guild = event.getGuild();
		TextChannel channel = guild.getSystemChannel();
		Member member = event.getMember();
				
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(member.getUser().getAsTag());
		embed.setColor(Color.green);
		embed.setThumbnail(member.getUser().getAvatarUrl());
		embed.setDescription("Ist **" + guild.getName() + "** beigetreten! \n Hier sind nun " + guild.getMembers().size()+ " Spieler. \n \n");
				
		if (member.getTimeCreated() != null) {
			OffsetDateTime datecreate = member.getTimeCreated();
			embed.appendDescription("Spieler erstellt am " + datecreate.getDayOfMonth() + "." + datecreate.getMonthValue() + "." + datecreate.getYear() + " um " + datecreate.getHour() + ":" + datecreate.getMinute());
		}
				
		channel.sendMessage(embed.build()).queue();	
		channel.getManager().setTopic("Willkommen! Aktuelle Spieler: " + guild.getMembers().size()).queue();
		System.out.println("[ShantyBot] " + member.getUser().getName() + " (" + member.getEffectiveName() + ") ist dem Server beigetreten");
	}	
}