package de.rettichlp.shantybot.common.services;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.stereotype.Component;

import java.util.Timer;
import java.util.TimerTask;

import static de.rettichlp.shantybot.ShantyBot.discordBot;
import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static java.lang.System.currentTimeMillis;
import static java.util.concurrent.TimeUnit.MINUTES;
import static net.dv8tion.jda.api.entities.Activity.playing;

@Log4j2
@Component
public class ActivitySyncService {

    private final String[] activities = {
            "auf ShantyTown.eu",
            "mit %count% Spielern",
    };

    public ActivitySyncService() {
        long oneMinuteInMillis = MINUTES.toMillis(1);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                String activityString = ActivitySyncService.this.activities[(int) (currentTimeMillis() / oneMinuteInMillis) % ActivitySyncService.this.activities.length];

                Activity activity = playing(activityString.replace("%count%", String.valueOf(discordBotProperties.getGuild().getMemberCount())));
                discordBot.getPresence().setActivity(activity);
            }
        }, oneMinuteInMillis - currentTimeMillis() % oneMinuteInMillis, oneMinuteInMillis);
        log.info("Activity synchronising: scheduled");
    }
}
