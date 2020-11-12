package de.rettichlp.commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;
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

public class WarnCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.KICK_MEMBERS) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
		
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length >= 4) {
				List<Member> members = message.getMentionedMembers(); //!sb warn @multi mus weil so ist
				if (!members.isEmpty()) {
					
					giveWarn(channel.getGuild(), members.get(0));
					int nrwarn = getNumberOfWarns(channel.getGuild(), members.get(0));
					
					String warn = "";
					for (int i = 2; i < args.length; i++) {
						warn = warn + " " + args[i];
					}
					warn = warn.replace("@", "");
					warn = warn.replace(members.get(0).getEffectiveName(), "");
					
					EmbedBuilder embed = new EmbedBuilder();
					embed.setColor(Color.red);
					embed.setTitle("⚠ Verwarnung");
					embed.addField("Für:", members.get(0).getEffectiveName(), true);
					embed.addField("Grund:", warn, true);
					embed.addField("Verwarnung:", nrwarn + " von 5", false);
					
					channel.sendMessage(members.get(0).getAsMention()).queue();;
					
					channel.sendMessage(embed.build()).queue();
					
					if (nrwarn == 4) {
						members.get(0).kick("Das ist deine letzte Verwarnung! Bei der 5. Verwarnung wirst du automatisch gebannt.").queue();
						//channel.sendMessage("gekickt").queue();
					} else if (nrwarn == 5) {
						members.get(0).ban(0, "Du wurdest gewarnt! ShantyBot hat dich wegen deines Fehlverhaltens gebannt.").queue();
						//channel.sendMessage("gebannt").queue();
						removeWarns(channel.getGuild(), members.get(0));
					}
					
					System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat " + members.get(0).getUser().getName() + " (" + members.get(0).getEffectiveName() + ") verwarnt mit dem Grund: " + warn);	
				} else
					channel.sendMessage(ShantyBot.err_asMention).complete().delete().queueAfter(3, TimeUnit.SECONDS);
			} else
				channel.sendMessage(ShantyBot.syn_warn).complete().delete().queueAfter(3, TimeUnit.SECONDS);				
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
				LiteSQL.onUpdate("INSERT INTO player(id, guildid, warn, spender, mcuuid) VALUES(" + member.getIdLong() + "," + guild.getIdLong() + "," + 0 + "," + 0 + ",'')");
		} catch(SQLException ex) { }
		return warns;
	}
	
	public void giveWarn(Guild guild, Member member) {
		
		int warns = getNumberOfWarns(guild, member);
		warns = warns + 1;
		LiteSQL.onUpdate("UPDATE player SET warn = " + warns + " WHERE id = " + member.getIdLong());
	}
	
	public void removeWarns(Guild guild, Member member) {
		int warns = getNumberOfWarns(guild, member) - getNumberOfWarns(guild, member);;
		LiteSQL.onUpdate("UPDATE player SET warn = " + warns + " WHERE id = " + member.getIdLong());
	}
}
