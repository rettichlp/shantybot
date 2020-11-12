package de.rettichlp.commands;

import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class TicketCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		String[] args = message.getContentDisplay().split(" ");
		
		if(args.length >= 3) {
			Guild guild = channel.getGuild();
			List<TextChannel> tcs = channel.getGuild().getTextChannelsByName("ticket-" + m.getId(), true);
			if (tcs.size() < 1) {
				guild.createTextChannel("ticket-" + m.getId())
					.setParent(guild.getCategoryById("770289166252507147"))
					.addPermissionOverride(m, EnumSet.of(Permission.VIEW_CHANNEL), null)
					.addPermissionOverride(guild.getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL))
					.addPermissionOverride(guild.getMemberById("278520516569071616"), EnumSet.of(Permission.VIEW_CHANNEL), null)
					.queue();
				channel.sendMessage("Ticket erstellt!").complete().delete().queueAfter(3, TimeUnit.SECONDS);
				List<TextChannel> tcsplayer = channel.getGuild().getTextChannelsByName("ticket-" + m.getId(), true);
				
				String mcnick = "";
				List<Role> roles = m.getRoles();
				for (int j = 0; j < roles.size(); j++) {
					if (m.getRoles().get(0).getId().equalsIgnoreCase("732202758282674237")) {
						mcnick = "(Minecraft: " + m.getNickname() + ")";
					}					
				}
				tcsplayer.get(0).sendMessage("@here **Neues Ticket von " + m.getUser().getName() + " " + mcnick + "**").queue();
				String msg = "";
				for (int i = 2; i < args.length; i++) {
					msg = msg + args[i] + " ";
				}
				tcsplayer.get(0).sendMessage(msg).queue();
				
			} else
				channel.sendMessage("Du kannst immer nur ein Ticket gleichzeitig offen haben.").complete().delete().queueAfter(3,  TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.syn_ticket).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
