package de.rettichlp.shantybot;

import de.rettichlp.shantybot.common.configuration.DiscordBotProperties;
import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.ZoneId;

import static java.lang.System.currentTimeMillis;
import static java.time.ZoneId.of;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MEMBERS;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;
import static net.dv8tion.jda.api.requests.GatewayIntent.MESSAGE_CONTENT;
import static net.dv8tion.jda.api.utils.Compression.NONE;
import static net.dv8tion.jda.api.utils.cache.CacheFlag.MEMBER_OVERRIDES;

@Log4j2
@SpringBootApplication
public class ShantyBot implements WebMvcConfigurer {

    public static JDA discordBot;
    public static DiscordBotProperties discordBotProperties;
    public static ZoneId ZONE_ID;

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ShantyBot.class, args);
        discordBotProperties = context.getBean(DiscordBotProperties.class);

        ZONE_ID = of("Europe/Berlin");

        long discordBotStartTime = currentTimeMillis();
        log.info("Discord bot starting");
        startDiscordBot();
        log.info("Discord bot started in {}ms", currentTimeMillis() - discordBotStartTime);
    }

    private static void startDiscordBot() {
        discordBot = JDABuilder
                .createDefault(discordBotProperties.getToken())
                .disableCache(MEMBER_OVERRIDES) // Disable parts of the cache
                .setBulkDeleteSplittingEnabled(false) // Enable the bulk delete event
                .setCompression(NONE) // Disable compression (not recommended)
                .enableIntents(MESSAGE_CONTENT)
                .enableIntents(GUILD_MEMBERS)
                .enableIntents(GUILD_MESSAGES)
                .enableIntents(GUILD_VOICE_STATES)
                .addEventListeners(
                        //                        new GuildMessageListener(),
                        //                        new GuildMemberListener(),
                        //                        new GuildVoiceUpdateListener()
                )
                .build();
    }
}
