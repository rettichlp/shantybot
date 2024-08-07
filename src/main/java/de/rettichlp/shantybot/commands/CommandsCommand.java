package de.rettichlp.shantybot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static de.rettichlp.shantybot.ShantyBot.discordBot;
import static java.util.Objects.requireNonNull;

public class CommandsCommand extends CommandBase {

    public CommandsCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        SelfUser botUser = discordBot.getSelfUser();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setTitle("ShantyBot Befehle")
                .setAuthor(botUser.getName(), null, botUser.getAvatarUrl());

        requireNonNull(event.getGuild()).retrieveCommands().queue(commands -> {
            commands.stream()
                    .filter(command -> command.getApplicationId().equals(botUser.getId()))
                    .forEach(command -> embedBuilder.addField("/" + command.getName(), command.getDescription(), false));

            event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();
        });
    }
}
