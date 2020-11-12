package de.rettichlp.commands;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class VoteCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		String[] args = message.getContentDisplay().split(" ");
		if(args.length >= 2) {
			channel.sendMessage("Damit ihr voten könnt, ohne online zu sein, hier die Vote-Links:").queue();
			
			EmbedBuilder embed = new EmbedBuilder();
			embed.setColor(Color.cyan);
			embed.setTitle("**Vote-Links**");
			embed.addField("Minecraft-Server.eu", "[https://minecraft-server.eu/vote/index/12C7B](https://minecraft-server.eu/vote/index/12C7B)", false);
			embed.addField("Minecraft-Serverlist.net", "[https://www.minecraft-serverlist.net/vote/13873](https://www.minecraft-serverlist.net/vote/13873)", false);
			
			
			channel.sendMessage(embed.build()).queue();
		} else
			channel.sendMessage(ShantyBot.syn_vote).complete().delete().queueAfter(3, TimeUnit.SECONDS);				
	}
}
