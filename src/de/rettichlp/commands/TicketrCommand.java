package de.rettichlp.commands;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import de.rettichlp.manage.LiteSQL;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TicketrCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.MANAGE_ROLES) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			String[] args = message.getContentDisplay().split(" ");
		
			if(args.length >= 4) {
				
				List<Member> members = message.getMentionedMembers();
				if (!members.isEmpty()) {
					int years = 0;
					try {
						years = Integer.parseInt(args[2]);
					} catch (Exception e) {
						channel.sendMessage(ShantyBot.syn_spender).complete().delete().queueAfter(3, TimeUnit.SECONDS);
					}
					Member member = members.get(0);
					
					ResultSet set = LiteSQL.onQuery("SELECT * FROM player WHERE guildid = " + message.getGuild().getIdLong() + " AND id = " + member.getIdLong());
					try {
						if(set.next()) {
							if (years != 0) {
								LiteSQL.onUpdate("UPDATE player SET spender = " + years + " WHERE id = " + member.getIdLong());
							} else {
								channel.sendMessage(ShantyBot.err_error).complete().delete().queueAfter(3, TimeUnit.SECONDS);
							}
						} else
							LiteSQL.onUpdate("INSERT INTO player(id, guildid, warn, spender, mcuuid) VALUES(" + member.getIdLong() + "," + message.getGuild().getIdLong() + "," + 0 + "," + years + ",'')");
					} catch(SQLException ex) { }		
					
					List<Role> roles = member.getGuild().getRolesByName("- [S] Spender", true);
					if (years >= 2) {
						channel.sendMessage(member.getAsMention() + " hat für den Server gespendet und kann für " + years + " Jahre das Premium-Feature nutzen!").queue();
						System.out.println("[ShantyBot] " + member.getUser().getName() + " (" + m.getEffectiveName() + ") hat für " + years + " Jahre gespendet");
						member.getGuild().addRoleToMember(member, roles.get(0)).queue();
					} else if(years == 1) {
						channel.sendMessage(member.getAsMention() + " hat für den Server gespendet und kann für " + years + " Jahr das Premium-Feature nutzen!").queue();
						System.out.println("[ShantyBot] " + member.getUser().getName() + " (" + m.getEffectiveName() + ") hat für " + years + " Jahr gespendet");
						member.getGuild().addRoleToMember(member, roles.get(0)).queue();
					} else
						channel.sendMessage(ShantyBot.syn_spender).complete().delete().queueAfter(3, TimeUnit.SECONDS);
				} else
					channel.sendMessage(ShantyBot.err_asMention).complete().delete().queueAfter(3, TimeUnit.SECONDS);
			} else
				channel.sendMessage(ShantyBot.syn_spender).complete().delete().queueAfter(3, TimeUnit.SECONDS);				
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
