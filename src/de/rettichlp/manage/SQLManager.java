package de.rettichlp.manage;

public class SQLManager {

	
	public static void onCreate() {
		
		//id   guildid   channelid   messageid   emote   rollenid
		
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS player(id INTEGER NOT NULL PRIMARY KEY, guildid INTEGER, warn INTEGER, spender INTEGER, mcuuid STRING)");
		
		LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS musicchannel(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, guildid INTEGER, channelid INTEGER)");
		
	}
	
}

