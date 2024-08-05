package de.rettichlp.shantybot.commands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Objects.isNull;

public class DeleteMessageCommand extends CommandBase {

    public DeleteMessageCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        OptionMapping amountOptionMapping = event.getOption("anzahl");
        if (isNull(amountOptionMapping)) {
            sendSelfDeletingMessage(event, "Es muss die Menge der zu löschenden Nachrichten angegeben werden!");
            return;
        }

        int amount = amountOptionMapping.getAsInt();

        if (amount < 2 || amount > 100) {
            sendSelfDeletingMessage(event, "Die Anzahl der zu löschenden Nachrichten muss zwischen 2 und 100 liegen!");
            return;
        }

        TextChannel channel = (TextChannel) event.getChannel();
        channel.getIterableHistory().takeAsync(amount).thenAccept(messages -> channel.deleteMessages(messages)
                .queue(unused -> sendSelfDeletingMessage(event, "Es wurden **" + amount + "** Nachrichten gelöscht.")));
    }
}
