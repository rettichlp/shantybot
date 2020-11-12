package de.rettichlp.listener;

import java.awt.Color;
import java.time.OffsetDateTime;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@SuppressWarnings("deprecation")
public class LeaveListener extends ListenerAdapter {
	
	@Override
	public void onGuildMemberLeave(GuildMemberLeaveEvent event) {
		
		Guild guild = event.getGuild();
		TextChannel channel = guild.getSystemChannel();
		Member member = event.getMember();
		
		EmbedBuilder embed = new EmbedBuilder();
		embed.setTitle(member.getUser().getAsTag());
		embed.setColor(Color.orange);
		embed.setThumbnail(member.getUser().getAvatarUrl());
		embed.setDescription("Hat **" + guild.getName() + "** verlassen! \n Hier sind nun " + guild.getMembers().size()+ " Spieler. \n \n");
		
		if (member.hasTimeJoined()) {
			OffsetDateTime datejoin = member.getTimeJoined();
			embed.appendDescription("Spieler ist am " + datejoin.getDayOfMonth() + "." + datejoin.getMonthValue() + "." + datejoin.getYear() + " um " + datejoin.getHour() + ":" + datejoin.getMinute() + " gejoined.");
		}
		
		channel.sendMessage(embed.build()).queue();
		channel.getManager().setTopic("Willkommen! Aktuelle Spieler: " + guild.getMembers().size()).queue();
		System.out.println("[ShantyBot] " + member.getUser().getName() + " (" + member.getEffectiveName() + ") hat den Server verlassen");
	}	
}