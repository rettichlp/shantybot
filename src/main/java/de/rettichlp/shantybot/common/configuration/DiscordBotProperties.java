package de.rettichlp.shantybot.common.configuration;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static de.rettichlp.shantybot.ShantyBot.discordBot;
import static java.util.Optional.ofNullable;

@Getter
@Component
public class DiscordBotProperties {

    @Value("${application-version}")
    private String version;

    @Value("${discord.bot.token}")
    private String token;

    @Value("${discord.guild.id}")
    private String guildId;

    @Value("${discord.guild.channels.community-text-channel}")
    private String communityTextChannel;

    @Value("${discord.guild.roles.member-role}")
    private String memberRole;

    @Nullable
    public Guild getGuild() {
        return discordBot.getGuildById(this.guildId);
    }

    @Nullable
    public TextChannel getCommunityTextChannel() {
        return ofNullable(getGuild())
                .map(guild -> guild.getTextChannelById(this.communityTextChannel))
                .orElse(null);
    }

    @Nullable
    public Role getMemberRole() {
        return ofNullable(getGuild())
                .map(guild -> guild.getRoleById(this.memberRole))
                .orElse(null);
    }
}
