package de.rettichlp.shantybot.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static java.util.Objects.nonNull;

public class GuildMemberListener extends ListenerAdapter {

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        Guild guild = e.getGuild();

        TextChannel communityTextChannel = discordBotProperties.getCommunityTextChannel();
        if (nonNull(communityTextChannel)) {
            communityTextChannel.getManager().setTopic("Willkommen! Du bist zwar nur einer von " + guild.getMemberCount() + ", aber dieser Text wurde nur für dich angepasst. :D").queue();
        }

        guild.addRoleToMember(e.getMember(), discordBotProperties.getMemberRole()).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        Guild guild = e.getGuild();

        TextChannel communityTextChannel = discordBotProperties.getCommunityTextChannel();
        if (nonNull(communityTextChannel)) {
            communityTextChannel.getManager().setTopic("Willkommen! Du bist zwar nur einer von " + guild.getMemberCount() + ", aber dieser Text wurde nur für dich angepasst. :D").queue();
        }
    }
}
