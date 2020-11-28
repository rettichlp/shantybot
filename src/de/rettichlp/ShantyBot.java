package de.rettichlp;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import de.rettichlp.commands.BotActivityCommand;
import de.rettichlp.listener.*;
import de.rettichlp.manage.DONOTOPEN;
import de.rettichlp.manage.LiteSQL;
import de.rettichlp.manage.SQLManager;
import de.rettichlp.music.PlayerManager;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class ShantyBot {
	
	public static ShantyBot INSTANCE;
	
	public ShardManager shardMan;
	private CommandManager cmdMan;
	private Thread loop;
	public AudioPlayerManager audioPlayerManager;
	public PlayerManager playerManager;
	
	
	public static void main(String[] args) {
		try {
			new ShantyBot();
		} catch (LoginException | IllegalArgumentException e) {
			e.printStackTrace();
		}
		System.setProperty("http.agent", "Chrome");
	}
	
	public ShantyBot() throws LoginException, IllegalArgumentException {
		INSTANCE = this;
		
		LiteSQL.connect();
		SQLManager.onCreate();
		System.out.println("[ShantyBot] Datenbank verbunden");

		@SuppressWarnings("deprecation")
		DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
		
		builder.setToken(DONOTOPEN.token);
		builder.setActivity(Activity.playing("shantytown.eu"));
		builder.setStatus(OnlineStatus.ONLINE);
		
		this.audioPlayerManager = new DefaultAudioPlayerManager();
		this.playerManager = new PlayerManager();
						
		this.cmdMan = new CommandManager();
		
		builder.addEventListeners(new CommandListener());
		builder.addEventListeners(new JoinListener());
		builder.addEventListeners(new LeaveListener());
		builder.addEventListeners(new BanListener());
		builder.addEventListeners(new HelpListener());
		builder.addEventListeners(new VorschlagListener());
		builder.addEventListeners(new CrosspostListener());
		
		shardMan = builder.build();
		System.out.println("[ShantyBot] Online");
		
		AudioSourceManagers.registerRemoteSources(audioPlayerManager);
		
		shutdown();
		runLoop();
	}
	
	public void shutdown() {
		new Thread(() -> {
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			
			try {
				if (reader.readLine().equalsIgnoreCase("stop")) {
					shutdown = true;
					if (shardMan != null) {
						shardMan.setStatus(OnlineStatus.OFFLINE);
						System.out.println("[ShantyBot] Offline");
						LiteSQL.disconnect();
						System.out.println("[ShantyBot] Datenbank getrennt");
						shardMan.shutdown();
						System.out.println("[ShantyBot] Heruntergefahren");
					}
					
					if (loop != null) {
						loop.interrupt();
					}
					
					reader.close();											
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public boolean shutdown = false;
	
	public void runLoop() {
		this.loop = new Thread(() -> {
			
			long time = System.currentTimeMillis();
			
			while(!shutdown) {
				if (System.currentTimeMillis() >= time + 1000) {
					time = System.currentTimeMillis();
					onSecond();
				}
			}
		});
		this.loop.setName("Loop");
		this.loop.start();
	}
	
	String[] status = BotActivityCommand.botActivity;
	
	int next = 5;
	public void onSecond() {
		
		status = BotActivityCommand.botActivity;
		
		if (next <= 0) {
			Random rand = new Random();
			
			int i = rand.nextInt(status.length);
			
			shardMan.getShards().forEach(jda -> {
				String text = status[i].replaceAll("%members", "" + jda.getUsers().size());
				jda.getPresence().setActivity(Activity.playing(text));
			});
			next = 5;
		} else
			next--;
	}
	
	public CommandManager getCmdMan() {
		return cmdMan;
	}
	
	public static String version =				"2.14.2";
	
	public static String err_error =			"**FEHLER:** Systemfehler";
	public static String err_noRights =			"**FEHLER:** Du hast keine Rechte für diesen Befehl!";
	public static String err_noRole =			"**FEHLER:** Die ausgewählte Rolle konnte nicht gefunden werden!";
	public static String err_noVoice = 			"**FEHLER:** Du musst in einem Voice-Channel sein!";
	public static String err_noMusic =			"**FEHLER:** Es wird gerade keine Musik gespielt!";
	public static String err_asMention =		"**FEHLER:** Der Nutzer muss als @Mention angegeben werden!";
	
	public static String syn_activity = 		"**HILFE:** !sb activity <Was soll angezeigt werden? | standard>";
	public static String syn_clear = 			"**HILFE:** !sb clear <Anzahl der Nachrichten | all>";
	public static String syn_nick = 			"**HILFE:** !sb nick <Nickname>";
	public static String syn_info = 			"**HILFE:** !sb info";
	public static String syn_say =				"**HILFE:** !sb say <Inhalt>";
	public static String syn_spender =			"**HILFE:** !sb spender <Jahre> <@Nutzer>";
	public static String syn_stats =			"**HILFE:** !sb stats <@Nutzer>";
	public static String syn_ticket = 			"**HILFE:** !sb ticket <Thema>";
	public static String syn_unnick = 			"**HILFE:** !sb unnick";
	public static String syn_verify = 			"**HILFE:** !sb verify <Minecraft Name> <@Discord Name>";
	public static String syn_verifyupdate =		"**HILFE:** !sb update";
	public static String syn_vote = 			"**HILFE:** !sb vote";
	public static String syn_warn = 			"**HILFE:** !sb warn <@Nutzer> <Grund>";
	
	public static String syn_play = 			"**HILFE:** !sb play <Link>";
	public static String syn_shuffle = 			"**HILFE:** !sb shuffle";
}
