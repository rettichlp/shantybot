package de.rettichlp.commands;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

public class RemoveEveryRoleCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.MANAGE_ROLES) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length == 2) {			
				List<Role> roles = m.getRoles();				
				for (int i = 0; i < roles.size(); i++) {
					m.getGuild().removeRoleFromMember(m, roles.get(i)).queue();
				}
				System.out.println("[ShantyBot] " + m.getUser().getName() + " (" + m.getEffectiveName() + ") hat seine Rollen entfernt");
			}
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
