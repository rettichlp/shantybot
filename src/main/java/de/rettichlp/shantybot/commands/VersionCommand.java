package de.rettichlp.shantybot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static de.rettichlp.shantybot.ShantyBot.discordBot;
import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;

public class VersionCommand extends CommandBase {

    public VersionCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        User user = discordBot.retrieveUserById("278520516569071616").complete();
        SelfUser botUser = discordBot.getSelfUser();

        MessageEmbed messageEmbed = new EmbedBuilder()
                .setTitle("ShantyBot by " + user.getEffectiveName(), "https://i.redd.it/gc2m1tdq22w81.jpg")
                .addField("Version", discordBotProperties.getVersion() + " [Changelog ↗](https://github.com/rettichlp/shantybot/releases/latest)", false)
                .addField("GitHub", "https://github.com/rettichlp/shantybot", false)
                .setAuthor(botUser.getName(), null, botUser.getAvatarUrl())
                .build();

        event.replyEmbeds(messageEmbed).setEphemeral(true).queue();
    }
}
