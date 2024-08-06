package de.rettichlp.shantybot.common.services;

import lombok.extern.log4j.Log4j2;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static de.rettichlp.shantybot.ShantyBot.discordBot;
import static de.rettichlp.shantybot.ShantyBot.discordBotProperties;
import static java.util.Optional.ofNullable;
import static net.dv8tion.jda.api.entities.Activity.playing;

@EnableScheduling
@Log4j2
@Component
public class ActivitySyncService {

    private final String[] activities = {
            "auf ShantyTown.eu",
            "mit %count% Spielern",
    };

    private int index = 0;

    @Scheduled(cron = "0 0/5 * * * ?")
    public void updateActivity() {
        String newActivity = this.activities[this.index].replace("%count%", getMemberCount());
        discordBot.getPresence().setActivity(playing(newActivity));
        this.index = (this.index + 1) % this.activities.length;
    }

    private String getMemberCount() {
        return ofNullable(discordBotProperties.getGuild())
                .map(Guild::getMemberCount)
                .map(Object::toString).orElse("n/a");
    }
}
