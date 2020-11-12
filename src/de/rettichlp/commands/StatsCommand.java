package de.rettichlp.commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import de.rettichlp.manage.LiteSQL;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class StatsCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.NICKNAME_MANAGE) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if (args.length == 2) {
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.blue);
				embed.setThumbnail(m.getUser().getAvatarUrl());
				embed.setTitle("**Informationen**");
				embed.setDescription("über: __" + m.getEffectiveName() + "__");
				embed.addField("ID", m.getId(), false);
				embed.addField("Name", m.getUser().getName(), false);
				
				if (m.getNickname() != null) {
					embed.addField("Nickname", m.getNickname().toString(), false);
				} else
					embed.addField("Nickname","-", false);
				
				if (m.getTimeCreated() != null) {
					OffsetDateTime datecreate = m.getTimeCreated();
					embed.addField("Account existiert seit", datecreate.getDayOfMonth() + "." + datecreate.getMonthValue() + "." + datecreate.getYear() + " " + datecreate.getHour() + ":" + datecreate.getMinute(), false);
				} else
					embed.addField("Account existiert seit","-", false);
				
				if (m.hasTimeJoined()) {
					OffsetDateTime datejoin = m.getTimeJoined();
					embed.addField("Auf diesem Server seit", datejoin.getDayOfMonth() + "." + datejoin.getMonthValue() + "." + datejoin.getYear() + " " + datejoin.getHour() + ":" + datejoin.getMinute(), false);
				} else
					embed.addField("Auf diesem Server seit","-", false);
				
				if (m.getTimeBoosted() != null) {
					OffsetDateTime dateboostet = m.getTimeBoosted();
					embed.addField("Server Booster seit", dateboostet.getDayOfMonth() + "." + dateboostet.getMonthValue() + "." + dateboostet.getYear() + " " + dateboostet.getHour() + ":" + dateboostet.getMinute(), false);
				} else
					embed.addField("Server Booster seit","", false);
				
				String rollen = "";
				for (int i = 0; i < m.getRoles().size(); i++) {
					rollen = rollen + m.getRoles().get(i).getName() + "\n" ;
				}
				embed.addField("Rollen", rollen, false);
				embed.addField("Verwarnungen", getNumberOfWarns(channel.getGuild(), m) + "", false);
				
				channel.sendMessage(embed.build()).queue();
				System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat seine Statistik aufgerufen");
			} else if(args.length >= 3) {	
				List<Member> members = message.getMentionedMembers();
				Member member = members.get(0);
				
				if (!members.isEmpty()) {
					EmbedBuilder embed = new EmbedBuilder();
					embed.setColor(Color.blue);
					embed.setThumbnail(member.getUser().getAvatarUrl());
					embed.setTitle("**Informationen**");
					embed.setDescription("über: __" + member.getEffectiveName() + "__");
					embed.addField("ID", member.getId(), false);
					embed.addField("Name", member.getUser().getName(), false);
					
					if (member.getNickname() != null) {
						embed.addField("Nickname", member.getNickname().toString(), false);
					} else
						embed.addField("Nickname","-", false);
					
					if (member.getTimeCreated() != null) {
						OffsetDateTime datecreate = member.getTimeCreated();
						embed.addField("Account existiert seit", datecreate.getDayOfMonth() + "." + datecreate.getMonthValue() + "." + datecreate.getYear() + " " + datecreate.getHour() + ":" + datecreate.getMinute(), false);
					} else
						embed.addField("Account existiert seit","-", false);
					
					if (member.hasTimeJoined()) {
						OffsetDateTime datejoin = member.getTimeJoined();
						embed.addField("Auf diesem Server seit", datejoin.getDayOfMonth() + "." + datejoin.getMonthValue() + "." + datejoin.getYear() + " " + datejoin.getHour() + ":" + datejoin.getMinute(), false);
					} else
						embed.addField("Auf diesem Server seit","-", false);
					
					if (member.getTimeBoosted() != null) {
						OffsetDateTime dateboostet = member.getTimeBoosted();
						embed.addField("Server Booster seit", dateboostet.getDayOfMonth() + "." + dateboostet.getMonthValue() + "." + dateboostet.getYear() + " " + dateboostet.getHour() + ":" + dateboostet.getMinute(), false);
					} else
						embed.addField("Server Booster seit","", false);
					
					String rollen = "";
					for (int i = 0; i < member.getRoles().size(); i++) {
						rollen = rollen + member.getRoles().get(i).getName() + "\n" ;
					}
					embed.addField("Rollen", rollen, false);
					embed.addField("Verwarnungen", getNumberOfWarns(channel.getGuild(), member) + "", false);
					
					channel.sendMessage(embed.build()).queue();
					System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat die Statistik von " + member.getUser().getName() + " (" + member.getEffectiveName() + ") aufgerufen");
				} else
					channel.sendMessage(ShantyBot.err_asMention).complete().delete().queueAfter(3, TimeUnit.SECONDS);				
			} else
				channel.sendMessage(ShantyBot.syn_stats).complete().delete().queueAfter(3, TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
	
	public int getNumberOfWarns(Guild guild, Member member) {
		
		ResultSet set = LiteSQL.onQuery("SELECT * FROM player WHERE guildid = " + guild.getIdLong() + " AND id = " + member.getIdLong());
		int warns = 0;
		try {
			if(set.next()) {
				warns = set.getInt("warn");
			} else
				LiteSQL.onUpdate("INSERT INTO player(id, guildid, warn, spender) VALUES(" + member.getIdLong() + "," + guild.getIdLong() + "," + 0 + "," + 0 + ")");
		} catch(SQLException ex) { }
		return warns;
	}
}
