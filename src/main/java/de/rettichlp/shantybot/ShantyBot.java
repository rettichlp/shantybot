package de.rettichlp.shantybot;

import de.rettichlp.shantybot.commands.VersionCommand;
import de.rettichlp.shantybot.commands.MusicPlayCommand;
import de.rettichlp.shantybot.commands.MusicSkipCommand;
import de.rettichlp.shantybot.commands.MusicStopCommand;
import de.rettichlp.shantybot.common.configuration.DiscordBotProperties;
import de.rettichlp.shantybot.listeners.GuildMemberListener;
import de.rettichlp.shantybot.listeners.GuildMessageListener;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static java.lang.System.currentTimeMillis;
import static net.dv8tion.jda.api.interactions.commands.build.Commands.slash;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static net.dv8tion.jda.api.interactions.commands.build.Commands.slash;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;
import static net.dv8tion.jda.api.requests.GatewayIntent.MESSAGE_CONTENT;
import static net.dv8tion.jda.api.utils.cache.CacheFlag.MEMBER_OVERRIDES;

@Log4j2
@SpringBootApplication
public class ShantyBot implements WebMvcConfigurer {

    public static JDA discordBot;
    public static DiscordBotProperties discordBotProperties;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(ShantyBot.class, args);
        discordBotProperties = context.getBean(DiscordBotProperties.class);

        long discordBotStartTime = currentTimeMillis();
        log.info("Discord bot starting");
        startDiscordBot();
        log.info("Discord bot started in {}ms", currentTimeMillis() - discordBotStartTime);
    }

    private static void startDiscordBot() throws InterruptedException {
        discordBot = JDABuilder
                .createDefault(discordBotProperties.getToken())
                .disableCache(MEMBER_OVERRIDES) // Disable parts of the cache
                .enableIntents(MESSAGE_CONTENT, GUILD_MEMBERS, GUILD_MESSAGES, GUILD_VOICE_STATES)
                .addEventListeners(
                        new MusicPlayCommand("play"),
                        new MusicSkipCommand("skip"),
                        new MusicStopCommand("stop"),
                        new VersionCommand("version")
                )
                .addEventListeners(
                        new GuildMessageListener(),
                        new GuildMemberListener()
                )
                .build().awaitReady();

        discordBotProperties.getGuild().updateCommands().addCommands(
                slash("play", "Lässt den Bot Deinen Channel betreten und die angegebene Musik spielen")
                        .addOption(STRING, "link", "Link oder Name des Songs", true),
                slash("skip", "Überspringt das Lied und spielt das nächste Lied in der Warteschlange (wenn vorhanden)"),
                slash("stop", "Beendet die Musikwiedergabe und leert die Warteschlange"),
                slash("version", "Zeigt die aktuelle Version des ShantyBots")
        ).queue();
    }
}
