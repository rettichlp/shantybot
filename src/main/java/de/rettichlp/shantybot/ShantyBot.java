package de.rettichlp.shantybot;

import de.rettichlp.dclogging.logging.DiscordLogging;
import de.rettichlp.shantybot.buttons.PauseButton;
import de.rettichlp.shantybot.buttons.QueueButton;
import de.rettichlp.shantybot.buttons.ResumeButton;
import de.rettichlp.shantybot.buttons.SkipButton;
import de.rettichlp.shantybot.buttons.StopButton;
import de.rettichlp.shantybot.commands.CommandsCommand;
import de.rettichlp.shantybot.commands.DeleteMessageCommand;
import de.rettichlp.shantybot.commands.IpCommand;
import de.rettichlp.shantybot.commands.MusicCommand;
import de.rettichlp.shantybot.commands.PlayersCommand;
import de.rettichlp.shantybot.commands.VersionCommand;
import de.rettichlp.shantybot.common.api.API;
import de.rettichlp.shantybot.common.configuration.DiscordBotProperties;
import de.rettichlp.shantybot.common.lavaplayer.AudioPlayerManager;
import de.rettichlp.shantybot.listeners.GuildMemberListener;
import de.rettichlp.shantybot.listeners.GuildMessageListener;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static java.lang.Runtime.getRuntime;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.setDefaultUncaughtExceptionHandler;
import static java.util.Optional.ofNullable;
import static net.dv8tion.jda.api.Permission.MESSAGE_MANAGE;
import static net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions.enabledFor;
import static net.dv8tion.jda.api.interactions.commands.OptionType.INTEGER;
import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;
import static net.dv8tion.jda.api.interactions.commands.build.Commands.slash;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;
import static net.dv8tion.jda.api.requests.GatewayIntent.MESSAGE_CONTENT;
import static net.dv8tion.jda.api.utils.cache.CacheFlag.MEMBER_OVERRIDES;
import static org.springframework.boot.SpringApplication.run;

@Log4j2
@SpringBootApplication
public class ShantyBot implements WebMvcConfigurer {

    public static JDA discordBot;
    public static DiscordBotProperties discordBotProperties;
    public static AudioPlayerManager audioPlayerManager;
    public static API api;
    public static DiscordLogging discordLogging;

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = run(ShantyBot.class, args);

        setDefaultUncaughtExceptionHandler((thread, exception) -> {
            String exceptionMessage = exception.getMessage();
            log.error(exceptionMessage, exception);
            discordLogging.error(exceptionMessage, exception);
        });

        discordBotProperties = context.getBean(DiscordBotProperties.class);

        discordLogging = DiscordLogging.getBuilder()
                .botToken(discordBotProperties.getLoggingToken())
                .guildId(discordBotProperties.getLoggingGuildId())
                .textChannelId(discordBotProperties.getLoggingTextChannel())
                .build();

        long discordBotStartTime = currentTimeMillis();
        log.info("Discord bot starting");
        startDiscordBot();

        getRuntime().addShutdownHook(new Thread(() -> ofNullable(discordBot).ifPresent(JDA::shutdown)));

        log.info("Discord bot started in {}ms", currentTimeMillis() - discordBotStartTime);

        api = new API();
    }

    private static void startDiscordBot() throws InterruptedException {
        discordBot = JDABuilder
                .createDefault(discordBotProperties.getToken())
                .disableCache(MEMBER_OVERRIDES) // Disable parts of the cache
                .enableIntents(MESSAGE_CONTENT, GUILD_MEMBERS, GUILD_MESSAGES, GUILD_VOICE_STATES)
                .addEventListeners(
                        new CommandsCommand("befehle"),
                        new DeleteMessageCommand("löschen"),
                        new IpCommand("ip"),
                        new MusicCommand("musik"),
                        new PlayersCommand("spieler"),
                        new VersionCommand("version")
                )
                .addEventListeners(
                        new GuildMessageListener(),
                        new GuildMemberListener()
                )
                .addEventListeners(
                        new PauseButton(),
                        new QueueButton(),
                        new ResumeButton(),
                        new SkipButton(),
                        new StopButton()
                )
                .build().awaitReady();

        discordBot.getGuilds().forEach(guild -> guild.updateCommands().addCommands(
                slash("löschen", "Löscht die angegebene Menge an Nachrichten (optional eines bestimmten Nutzers)")
                        .addOption(INTEGER, "anzahl", "Anzahl der Nachrichten, die gelöscht werden sollen", true)
                        .setDefaultPermissions(enabledFor(MESSAGE_MANAGE)),

                slash("befehle", "Zeigt alle verfügbaren Befehle des ShantyBots"),
                slash("ip", "Zeigt die IP, Version und zusätzliche Informationen über den Minecraft Server"),
                slash("musik", "Lässt den Bot Deinen Channel betreten und die angegebene Musik spielen")
                        .addOption(STRING, "link", "Link oder Name des Songs", true),
                slash("spieler", "Zeigt die Anzahl der Spieler die gerade auf dem Minecraft Server sind"),
                slash("version", "Zeigt die aktuelle Version des ShantyBots")
        ).queue());

        audioPlayerManager = new AudioPlayerManager();

        ofNullable(discordBotProperties.getGuild())
                .ifPresentOrElse(guild -> log.info("Discord bot connected to the guild requested by the properties: {}", guild.getName()), () -> {
                    log.error("Discord bot cannot create connection to guild requested by the properties");
                    discordLogging.error("Discord bot cannot create connection to guild requested by the properties");
                });
    }
}
