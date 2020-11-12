package de.rettichlp.commands;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;

import de.rettichlp.ShantyBot;
import de.rettichlp.commands.types.ServerCommand;
import de.rettichlp.manage.LiteSQL;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class VerifyCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.MANAGE_ROLES) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			String[] args = message.getContentDisplay().split(" ");
		
			if(args.length >= 4) {
				Member dcUser = message.getMentionedMembers().get(0);
				String mcUserUUID = getMinecraftUUID(args[2]);
				Guild guild = channel.getGuild();
				
				ResultSet set = LiteSQL.onQuery("SELECT * FROM player WHERE guildid = " + guild.getIdLong() + " AND id = " + dcUser.getIdLong());
				try {
					if(set.next()) {
						LiteSQL.onUpdate("UPDATE player SET mcuuid = '" + mcUserUUID + "' WHERE id = " + dcUser.getIdLong());
					} else
						LiteSQL.onUpdate("INSERT INTO player(id, guildid, warn, spender, mcuuid) VALUES(" + dcUser.getIdLong() + "," + guild.getIdLong() + "," + 0 + "," + 0 + ",'" + mcUserUUID + "')");
				} catch(SQLException ex) { }			
								
				Role role = guild.getRoleById("732202758282674237");
				if (role != null) {
					guild.addRoleToMember(dcUser, role).queue();
					dcUser.modifyNickname(args[2]).queue();
				} else
					channel.sendMessage(ShantyBot.err_noRole).complete().delete().queueAfter(3, TimeUnit.SECONDS);		
			} else
				channel.sendMessage(ShantyBot.syn_verify).complete().delete().queueAfter(3, TimeUnit.SECONDS);				
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
	
	public String getMinecraftUUID(String mcUsername) {
		String url = "https://api.mojang.com/users/profiles/minecraft/" + mcUsername;
        try {
        	@SuppressWarnings("deprecation")
        	String UUIDJson = IOUtils.toString(new URL(url));
        	if(!UUIDJson.isEmpty()) {
        		JSONObject UUIDObject = (JSONObject) JSONValue.parseWithException(UUIDJson);
        		return UUIDObject.get("id").toString();
        	}
        } catch (IOException | org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }
        return "";
	}
}
