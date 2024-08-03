package de.rettichlp.shantybot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import static de.rettichlp.shantybot.ShantyBot.discordBot;
import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static java.awt.Color.GREEN;
import static java.awt.Color.YELLOW;
import static java.util.Objects.nonNull;

public class GuildMemberListener extends ListenerAdapter {

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent e) {
        Guild guild = e.getGuild();
        Member member = e.getMember();

        TextChannel communityTextChannel = discordBotProperties.getCommunityTextChannel();
        if (nonNull(member) && nonNull(communityTextChannel)) {
            // remove message embed
            MessageEmbed messageEmbed = new EmbedBuilder()
                    .setColor(YELLOW)
                    .setTitle("Auf Wiedersehen!")
                    .setAuthor("ShantyBot", null, discordBot.getSelfUser().getEffectiveAvatarUrl())
                    .setDescription(member.getEffectiveName() + " hat **ShantyTown** verlassen!")
                    .setFooter("Beigetreten am", member.getEffectiveAvatarUrl())
                    .setTimestamp(member.getTimeJoined())
                    .build();

            communityTextChannel.sendMessageEmbeds(messageEmbed).queue();
            communityTextChannel.getManager().setTopic("Willkommen! Du bist zwar nur einer von " + guild.getMemberCount() + ", aber dieser Text wurde nur für dich angepasst. :D").queue();
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e) {
        Guild guild = e.getGuild();
        Member member = e.getMember();

        TextChannel communityTextChannel = discordBotProperties.getCommunityTextChannel();
        if (nonNull(communityTextChannel)) {
            // join message embed
            MessageEmbed messageEmbed = new EmbedBuilder()
                    .setColor(GREEN)
                    .setTitle("Willkommen!")
                    .setAuthor("ShantyBot", null, discordBot.getSelfUser().getEffectiveAvatarUrl())
                    .setDescription(member.getEffectiveName() + " ist **ShantyTown** beigetreten!")
                    .setFooter("Account erstellt am", member.getEffectiveAvatarUrl())
                    .setTimestamp(member.getTimeCreated())
                    .build();

            communityTextChannel.sendMessageEmbeds(messageEmbed).queue();
            communityTextChannel.getManager().setTopic("Willkommen! Du bist zwar nur einer von " + guild.getMemberCount() + ", aber dieser Text wurde nur für dich angepasst. :D").queue();
        }

        guild.addRoleToMember(member, discordBotProperties.getMemberRole()).queue();
    }
}
