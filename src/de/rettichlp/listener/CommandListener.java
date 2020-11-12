package de.rettichlp.listener;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

import de.rettichlp.ShantyBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		String message = event.getMessage().getContentDisplay();
		
		if(event.isFromType(ChannelType.TEXT)) {
			TextChannel channel = event.getTextChannel();
			
			if(message.startsWith("!sb ")) {
				
				String[] args = message.substring(4).split(" ");
					
				if(args.length > 0) {
					if(!ShantyBot.INSTANCE.getCmdMan().perform(args[0], event.getMember(), channel, event.getMessage())) {
						channel.sendMessage("Unbekannter Befehl").complete().delete().queueAfter(3, TimeUnit.SECONDS);
					}
				}
			} else if (message.equalsIgnoreCase("!sb")) {
				EmbedBuilder embed = new EmbedBuilder();
				if(event.getMember().hasPermission(channel, Permission.MESSAGE_MANAGE)) {
					embed.setColor(Color.magenta);
					embed.setTitle("**ShantyBot Hilfe**");
					embed.setDescription("Hilfeseite für den offiziellen ShantyTown-Bot");
					
					embed.addField("Spielerzahl auf ShantyTown", "```!sb online```", false);
					embed.addField("Vote-Links anzeigen", "```!sb vote```", false);
					embed.addField("", "", false);
					embed.addField("Musik spielen", "```!sb play <Musik>```", false);
					embed.addField("Musik stoppen", "```!sb stop```", false);
					embed.addField("Musik Trackinfo", "```!sb ti```", false);
					embed.addField("Musik Zufallswiedergabe", "```!sb shuffle```", false);
					embed.addField("", "", false);
					embed.addField("Nachrichten löschen", "```!sb clear <Anzahl>```", false);
					embed.addField("Chatverlauf löschen", "```!sb clear all```", false);
					embed.addField("Verwarnungen erstellen", "```!sb warn <Nutzer als Mention> <Grund>```", false);
					embed.addField("Spender hinzufügen", "```!sb spender <Jahre> <@Nutzer>```", false);
					embed.addField("Statistiken anzeigen", "```!sb stats <@Nutzer> (wird @Nutzer weggelassen werden eigene Statistiken angezeigt)```", false);
					embed.addField("Spieler verifizieren", "```!sb verify <Minecraft-Name> <@Nutzer>```", false);
					embed.addField("Spieler update durchführen", "```!sb update```", false);
					
					embed.setFooter("ShantyTown-Bot by Rettich 🥬  ©2020", "https://cdn.discordapp.com/avatars/278520516569071616/7890f0550bce7900786ca9761482e39e.png");
				} else {
					embed.setColor(Color.magenta);
					embed.setTitle("**ShantyBot Hilfe**");
					embed.setDescription("Hilfeseite für den offiziellen ShantyTown-Bot");
								
					embed.addField("Spielerzahl auf ShantyTown", "```!sb online```", false);
					embed.addField("Vote-Links anzeigen", "```!sb vote```", false);
					embed.addField("", "", false);
					embed.addField("Musik spielen", "```!sb play <Musik>```", false);
					embed.addField("Musik stoppen", "```!sb stop```", false);
					embed.addField("Musik Trackinfo", "```!sb ti```", false);
					embed.addField("Musik Zufallswiedergabe", "```!sb shuffle```", false);
					
					embed.setFooter("ShantyTown-Bot by Rettich 🥬  ©2020", "https://cdn.discordapp.com/avatars/278520516569071616/7890f0550bce7900786ca9761482e39e.png");
				}
				channel.sendMessage(event.getMember().getAsMention()).queue();
				channel.sendMessage(embed.build()).queue();
				System.out.println("[ShantyBot] " + event.getMessage().getMember().getUser().getName() + " (" + event.getMessage().getMember().getEffectiveName() + ") hat sich den Hilfetext anzeigen lassen");
			}
		}		
	}	
}