package de.rettichlp;

import java.util.concurrent.ConcurrentHashMap;

import de.rettichlp.commands.BotActivityCommand;
import de.rettichlp.commands.ClearCommand;
import de.rettichlp.commands.NickCommand;
import de.rettichlp.commands.PlayerOnlineCommand;
import de.rettichlp.commands.RemoveEveryRoleCommand;
import de.rettichlp.commands.SayCommand;
import de.rettichlp.commands.SpenderCommand;
import de.rettichlp.commands.StatsCommand;
import de.rettichlp.commands.UnnickCommand;
import de.rettichlp.commands.VerifyCommand;
import de.rettichlp.commands.VerifyUpdateCommand;
import de.rettichlp.commands.VersionCommand;
import de.rettichlp.commands.VoteCommand;
import de.rettichlp.commands.WarnCommand;
import de.rettichlp.commands.types.ServerCommand;
import de.rettichlp.music.commands.PlayCommand;
import de.rettichlp.music.commands.ShuffleCommand;
import de.rettichlp.music.commands.SkipCommand;
import de.rettichlp.music.commands.StopCommand;
import de.rettichlp.music.commands.TrackInfoCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

public class CommandManager {

	public ConcurrentHashMap<String, ServerCommand> commands;
	
	public CommandManager() {
		this.commands = new ConcurrentHashMap<>();
		
		this.commands.put("clear", new ClearCommand());
		this.commands.put("warn", new WarnCommand());
		this.commands.put("spender", new SpenderCommand());
		this.commands.put("stats", new StatsCommand());
		this.commands.put("unnick", new UnnickCommand());
		this.commands.put("info", new PlayerOnlineCommand());
		this.commands.put("vote", new VoteCommand());
		this.commands.put("activity", new BotActivityCommand());
		this.commands.put("verify", new VerifyCommand());
		this.commands.put("version", new VersionCommand());
		this.commands.put("update", new VerifyUpdateCommand());
		
		//this.commands.put("ticket", new TicketCommand());
		//this.commands.put("close", new TicketDeleteCommand());
		
		this.commands.put("play", new PlayCommand());
		this.commands.put("stop", new StopCommand());
		this.commands.put("shuffle", new ShuffleCommand());
		this.commands.put("skip", new SkipCommand());
		this.commands.put("ti", new TrackInfoCommand());
		
		this.commands.put("say", new SayCommand());
		this.commands.put("nick", new NickCommand());
		this.commands.put("unnick", new UnnickCommand());
		this.commands.put("removeroles", new RemoveEveryRoleCommand());
	}
	
	public boolean perform(String command, Member m, TextChannel channel, Message message) {
		
		ServerCommand cmd;
		if((cmd = this.commands.get(command.toLowerCase())) != null) {
			cmd.performCommand(m, channel, message);
			return true;
		}
		
		return false;
	}
}

