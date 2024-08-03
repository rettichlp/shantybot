package de.rettichlp.shantybot.common.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class DiscordBotProperties {

    @Value("${discord.bot.token}")
    private String token;
}
