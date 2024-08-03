package de.rettichlp.shantybot.common.configuration;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static de.rettichlp.shantybot.ShantyBot.discordBot;

@Getter
@Component
public class DiscordBotProperties {

    @Value("${discord.bot.token}")
    private String token;

    @Value("${discord.guild.id}")
    private String guildId;

    @Value("${discord.guild.channels.community-text-channel}")
    private String communityTextChannel;

    @Value("${discord.guild.channels.bot-text-channel}")
    private String botTextChannel;

    @Value("${discord.guild.roles.member-role}")
    private String memberRole;

    public Guild getGuild() {
        return discordBot.getGuildById(this.guildId);
    }

    public TextChannel getCommunityTextChannel() {
        return getGuild().getTextChannelById(this.communityTextChannel);
    }

    public TextChannel getBotTextChannel() {
        return getGuild().getTextChannelById(this.botTextChannel);
    }

    public Role getMemberRole() {
        return getGuild().getRoleById(this.memberRole);
    }
}
