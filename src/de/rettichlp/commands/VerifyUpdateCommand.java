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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class VerifyUpdateCommand implements ServerCommand {

	@Override
	public void performCommand(Member m, TextChannel channel, Message message) {
		
		message.delete().queue();
		
		if(m.hasPermission(channel, Permission.MANAGE_ROLES) || m.getUser().getId().equalsIgnoreCase("278520516569071616")) {
			String[] args = message.getContentDisplay().split(" ");
			
			if(args.length >= 2) {
				Guild guild = channel.getGuild();
				
				ResultSet set = LiteSQL.onQuery("SELECT * FROM player WHERE guildid = " + guild.getIdLong());
				try {
					if(set.next()) {						
						while (set.next()) {
							if (!set.getString("mcuuid").equalsIgnoreCase("")) {
								String mcname = getMinecraftUsername(set.getString("mcuuid"));
								Member member = guild.getMemberById(set.getLong("id"));
								if (member != null) {
									if (mcname.equalsIgnoreCase("")) {
										System.out.println("DC Name: " + member.getEffectiveName());
										System.out.println("MC Name: nicht gefunden");
										Role role = guild.getRoleById("732202758282674237");
										if (role != null) {
											guild.removeRoleFromMember(member.getIdLong(), role).queue();
											member.modifyNickname(member.getUser().getName()).queue();
											System.out.println("Minecraft Name nicht gefunden - Rolle 'Verified User' entfernt! <<<");
										} else {
											channel.sendMessage(ShantyBot.err_noRole).queue();
										}
									} else {
										System.out.println("DC Name: " + member.getEffectiveName());
										System.out.println("MC Name: " + mcname);
										try {
											member.modifyNickname(mcname).queue();
										} catch (Exception e) {
											System.out.println("Keine Rechte für: Nicknameänderung");
										}									
									}
								} else {
									System.out.println("MC Name: " + mcname);
									System.out.println("Nutzer ist nicht mehr auf dem Discord");
									LiteSQL.onUpdate("DELETE FROM player WHERE mcuuid = '" + set.getString("mcuuid") + "'");
									System.out.println("Nutzer aus Datenbank gelöscht");
								}
								System.out.println("---------------------------------------------");
							}												
						}						
					}
					channel.getGuild().getMemberById("278520516569071616").modifyNickname("Rettich 'Ret' Rettington").queue();
				} catch(SQLException ex) {
					ex.printStackTrace();
				}
			} else
				channel.sendMessage(ShantyBot.syn_verifyupdate).complete().delete().queueAfter(3, TimeUnit.SECONDS);				
		} else
			channel.sendMessage(ShantyBot.err_noRights).complete().delete().queueAfter(3, TimeUnit.SECONDS);
	}
	
	public String getMinecraftUsername(String mcUUID) {
			
		String url = "https://api.mojang.com/user/profiles/" + mcUUID + "/names";	
        try {
        	@SuppressWarnings("deprecation")
        	String UUIDJson = IOUtils.toString(new URL(url));
        	if(!UUIDJson.isEmpty()) {
        		
        		String name = "";
        		JSONArray UUIDObject = (JSONArray) JSONValue.parseWithException(UUIDJson);
        		for(int n = 0; n < UUIDObject.size(); n++) {
        			JSONObject object = (JSONObject) UUIDObject.get(n);
        			//System.out.println(object.get("name").toString());
        		    name = object.get("name").toString();
        		}
        		return name;
        	}
        } catch (IOException | org.json.simple.parser.ParseException e) {}
        return "";
	}
}
