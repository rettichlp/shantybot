package de.rettichlp.shantybot.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.currentTimeMillis;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.regex.Pattern.compile;
import static net.dv8tion.jda.api.Permission.MESSAGE_MANAGE;

public class GuildMessageListener extends ListenerAdapter {

    private final Pattern inviteLinkPattern = compile("(https?://)?(www\\.)?discord\\.(gg|io|me|li|com)/(invite/\\w+|\\w+)");
    private final String[] linkDeletedMessages = {
            "Autsch, dieser Link war verboten. Keine Sorge, ich hab ihn unschädlich gemacht.",
            "Dieser Invite-Link war wohl auf Abwegen. Jetzt ist er weg!",
            "Dieser Invite-Link? Nicht heute, Freundchen!",
            "Dieser Link war wohl nicht ganz richtig. Ab in den digitalen Papierkorb!",
            "Du weißt, dass verbotene Invite-Links hier keinen Platz haben, oder?",
            "Huch, hast du da gerade einen verbotenen Invite-Link fallen lassen? Weg damit!",
            "Ich hab diesen Link mal schnell durch den digitalen Aktenvernichter gejagt.",
            "Link entfernt. Versuch's doch mal mit Katzenvideos, die sind immer erlaubt!",
            "Link gelöscht. Keine Panik, das Internet ist immer noch da (größtenteils).",
            "Link gelöscht. Wie wär's mit einem schönen GIF stattdessen? Die sind lustiger!",
            "Netter Versuch, aber dieser Invite-Link gehört in den Müll!",
            "Nice try, aber der Invite-Link fliegt raus!",
            "Oh nein, ein verirrter Invite-Link! Keine Sorge, hab ihn entsorgt.",
            "Oha, ein verbotener Invite-Link. Nicht mit mir!",
            "Oops! Das war wohl der falsche Link, versuch's doch nochmal ohne!",
            "Oops, ein falscher Link! Keine Sorge, ich hab ihn beseitigt. Weitermachen!",
            "Oops, ein verbotener Invite-Link! Weggeblasen wie Staub im Wind.",
            "Pech gehabt, dein verbotener Invite-Link hat hier nichts verloren.",
            "Raus mit dem Invite-Link! Versuchs mal ohne Schleichwerbung.",
            "Schön versucht, aber der Invite-Link ist jetzt Geschichte.",
            "Schön, dass du uns besuchst, aber der Invite-Link bleibt draußen.",
            "Sorry, dein Invite-Link hat's nicht durch die Sicherheitskontrolle geschafft.",
            "Ups, dieser Invite-Link ist wohl aus Versehen hier gelandet. Gelöscht!",
            "Verbotener Invite-Link aufgespürt und entsorgt!",
            "Verbotener Invite-Link? Nicht heute, nicht hier!",
            "Verbotener Link erkannt und entfernt! Lass uns den Server sauber halten.",
            "Verbotener Link gesichtet und eliminiert!",
            "Versuch's nochmal ohne den verbotenen Invite-Link. Danke!",
            "Wir mögen Gäste, aber dieser Invite-Link war nicht willkommen."
    };
    private long lastAnsweredWhyQuestion = 0;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        Message message = event.getMessage();
        boolean deleted = handleInviteLink(event.getMember(), message, event.getChannel());

        if (!deleted) {
            String contentRaw = message.getContentRaw().toLowerCase();

            if (currentTimeMillis() - this.lastAnsweredWhyQuestion > MINUTES.toMillis(5) && (contentRaw.startsWith("warum ") || contentRaw.startsWith("warum?"))) {
                event.getChannel().sendMessage("Darum.").queue();
                this.lastAnsweredWhyQuestion = currentTimeMillis();
            }
        }
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        handleInviteLink(event.getMember(), event.getMessage(), event.getChannel());
    }

    private boolean handleInviteLink(Member member, Message message, MessageChannelUnion channel) {
        boolean isAllowedToSendInviteLinks = ofNullable(member).map(m -> m.hasPermission(MESSAGE_MANAGE)).orElse(false);
        Matcher matcher = inviteLinkPattern.matcher(message.getContentRaw());
        if (!isAllowedToSendInviteLinks && matcher.find()) {
            message.delete().queue();
            channel.sendMessage(this.linkDeletedMessages[new Random().nextInt(this.linkDeletedMessages.length)]).queue();
            return true;
        }

        return false;
    }
}
