package de.rettichlp.shantybot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static de.rettichlp.shantybot.ShantyBot.api;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;

public class PlayersCommand extends CommandBase {

    public PlayersCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (api.apiNotReachable()) {
            sendSelfDeletingMessage(event, "Die [API](https://api.mcsrvstat.us/3/shantytown.eu) ist aktuell nicht erreichbar. Bitte versuche es später erneut.");
            return;
        }

        String maxPlayers = ofNullable(api.getMaxPlayers())
                .map(s -> " von **" + s + "**")
                .orElse("");

        String areString = api.getOnlinePlayers() == 1 ? "ist" : "sind";
        String playerString = (nonNull(api.getMaxPlayers()) && api.getMaxPlayers() != 1) ? "Spielern" : "Spieler";

        event.reply("Aktuell %s **%d**%s %s online." .formatted(areString, api.getOnlinePlayers(), maxPlayers, playerString)).queue();
    }
}
