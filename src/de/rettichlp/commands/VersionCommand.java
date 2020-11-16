package de.rettichlp.commands;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class VersionCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.ADMINISTRATOR) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			
			String[] args = message.getContentDisplay().split(" ");
			
			if (args.length == 2) {
				Guild guild = channel.getGuild();
				
				String newestversion = ShantyBot.version;
				
				try {
					URL url = new URL("https://lholl.github.io/");
					BufferedReader r = new BufferedReader(new InputStreamReader(url.openStream()));
					String line = null;
					while((line=r.readLine())!=null) {
						newestversion = line;
					}
				} catch (Exception e) {}
				
				EmbedBuilder embed = new EmbedBuilder();
				embed.setColor(Color.MAGENTA);
				embed.setThumbnail(guild.getMemberById("725076053495644201").getUser().getAvatarUrl());
				embed.setTitle("**Aktuelle Version: " + ShantyBot.version + "\nNeuste Version: " + newestversion + "**");
				
				if (!ShantyBot.version.equalsIgnoreCase(newestversion)) {
					embed.addField("**Download: " + newestversion + "**", "** [https://github.com/lholl/shantybot](https://github.com/lholl/shantybot) **", false);
				}

				embed.setFooter("© ShantyBot by " + guild.getMemberById("278520516569071616").getUser().getName() , guild.getMemberById("278520516569071616").getUser().getAvatarUrl());
				channel.sendMessage(embed.build()).queue();			
			} else
				channel.sendMessage(ShantyBot.syn_stats).complete().delete().queueAfter(3, TimeUnit.SECONDS);
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
}
