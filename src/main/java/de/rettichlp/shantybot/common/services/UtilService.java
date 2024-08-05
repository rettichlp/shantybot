package de.rettichlp.shantybot.common.services;

import net.dv8tion.jda.api.events.Event;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.SECONDS;

public class UtilService {

    public static void sendSelfDeletingMessage(Event event, String message) {
        sendSelfDeletingMessage(event, message, 10);
    }

    public static void sendSelfDeletingMessage(Event event, String message, int seconds) {
        if (event instanceof IReplyCallback iReplyCallback) {
            iReplyCallback.reply(message + "\n-# 🚮 <t:" + (currentTimeMillis() / 1000 + seconds) + ":R>").setEphemeral(true).queue();
            iReplyCallback.getHook().deleteOriginal().queueAfter(seconds - 1, SECONDS);
        }
    }

    public static String millisecondsToMMSS(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return format("%02d:%02d", minutes, seconds);
    }
}
