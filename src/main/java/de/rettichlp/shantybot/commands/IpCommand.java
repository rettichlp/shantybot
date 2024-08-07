package de.rettichlp.shantybot.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import static de.rettichlp.shantybot.ShantyBot.api;
import static de.rettichlp.shantybot.common.services.UtilService.sendSelfDeletingMessage;
import static java.util.Optional.ofNullable;

public class IpCommand extends CommandBase {

    public IpCommand(String name) {
        super(name);
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        if (api.apiNotReachable()) {
            sendSelfDeletingMessage(event, "Die [API](https://api.mcsrvstat.us/3/shantytown.eu) ist aktuell nicht erreichbar. Bitte versuche es später erneut.");
            return;
        }

        String version = ofNullable(api.getVersion())
                .map(s -> " und ist aktuell auf der Version " + s + ".")
                .orElse(".");

        String maintenance = api.isMaintenance() ? "\n⚠️ Aktuell sind Wartungsarbeiten!" : "";

        String offline = ofNullable(api.isOffline()).map(b -> b ? "\n⛔ Der Server ist aktuell offline!" : "").orElse("");

        event.reply("ShantyTown hat die IP **shantytown.eu**%s%s%s" .formatted(version, maintenance, offline)).queue();
    }
}
