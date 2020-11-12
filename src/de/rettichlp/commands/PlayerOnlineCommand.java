package de.rettichlp.commands;

import java.awt.Color;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class PlayerOnlineCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		//if(m.hasPermission(channel, Permission.ADMINISTRATOR) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			String[] args = message.getContentDisplay().split(" ");
			if(args.length >= 2) {
				
				String online = getServerData((JSONObject) urlToObject(), "online").toString();

				EmbedBuilder embed = new EmbedBuilder();
				
				if (online.equalsIgnoreCase("true")) {
					String version = getServerData((JSONObject) urlToObject(), "version").toString();
					int playeronline =Integer.parseInt(getServerData((JSONObject) getServerData((JSONObject) urlToObject(), "players"),"online").toString());
					int playermax =Integer.parseInt(getServerData((JSONObject) getServerData((JSONObject) urlToObject(), "players"),"max").toString());
					JSONArray playerlist = (JSONArray) getServerData((JSONObject) getServerData((JSONObject) urlToObject(), "players"),"list");
					JSONArray motd = (JSONArray) getServerData((JSONObject) getServerData((JSONObject) urlToObject(), "motd"),"clean");
					
					String players = "";
						
					if (playeronline != 0) {
						for (int i = 0; i < playerlist.size(); i++) {
							players = players + ", " + playerlist.get(i).toString();
						}
					}
					try {
						if (playerlist.contains("ScuroK")) {
							players = players.replace(", ScuroK", "");
							playeronline = playeronline -1;	
						}
					} catch (Exception e) {}
					
					players = players.replaceFirst(", ", "");
					
					embed.setColor(Color.GREEN)
					.setAuthor("Serverinfo")
					.setDescription("*Aktuelle Informationen zu ShantyTown.eu*")
					.addField("Motd", motd.get(0).toString(), false)
					.addField("Status", "```ONLINE```", true)
					.addBlankField(true)
					.addField("Version", version, true)
					.addField("Spieler", playeronline + "/" + playermax, false)
					.addField("", players, false);
					
				} else {
					embed.setColor(Color.RED)
					.setAuthor("Serverinfo")
					.setDescription("*Aktuelle Informationen zu ShantyTown.eu*")
					.addField("Status", "```OFFLINE```", true);
				}
				channel.sendMessage(embed.build()).queue();
			} else
				channel.sendMessage(ShantyBot.syn_info).complete().delete().queueAfter(3, TimeUnit.SECONDS);	
		//} else
		//	channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
	
	private Object urlToObject() {
		String url = "https://api.mcsrvstat.us/2/shantytown.eu";
		try {
        	@SuppressWarnings("deprecation")
        	String UUIDJson = IOUtils.toString(new URL(url));
        	if(!UUIDJson.isEmpty()) {
        		JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
        		return UUIDObject;
        	}
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	private Object getServerData(JSONObject obj, String key) {
		JSONObject UUIDObject = obj;
		return UUIDObject.get(key);
	}
}